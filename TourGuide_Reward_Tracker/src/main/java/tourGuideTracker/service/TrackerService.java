package tourGuideTracker.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tourGuideTracker.repositorie.proxy.ServiceUserProxy;
import tourGuideTracker.repositorie.GpsUtil;
import tourGuideTracker.domain.location.Attraction;
import tourGuideTracker.domain.location.Location;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.domain.UserLocation;
import tourGuideTracker.repositorie.proxy.ServiceRewardsProxy;
import tourGuideTracker.tracker.Tracker;
import tourGuideTracker.bean.UserService.UserBean;


@Service
@Slf4j
public class TrackerService {

    @Autowired
    private final GpsUtil gpsUtil;
    @Autowired
    private ServiceRewardsProxy serviceRewardsProxy;
    @Autowired
    private ServiceUserProxy serviceUserProxy;

    public final Tracker tracker;
    boolean testMode = true;

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private int proximityBuffer = 10;
    private int attractionProximityRange = 200;


    public TrackerService(GpsUtil gpsUtil) {
        this.gpsUtil = gpsUtil;

        if (testMode) {
            log.info("TestMode enabled");
            log.debug("Initializing users");
            initializeInternalUsers();
            log.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    public VisitedLocation getUserLocation(String userName) {
        UserBean user = serviceUserProxy.getUser(userName);
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
        return visitedLocation;
    }


    public List<UserLocation> getLocationOfAllUsers() {
        List<UserLocation> userLocations = new ArrayList<>();
        for (UserBean user : serviceUserProxy.getAllUsers()) {
            UserLocation userLocation = new UserLocation();
            userLocation.setUserID(user.getUserId());
            userLocation.setLatLongUser((user.getVisitedLocations().size() > 0) ?
                    user.getLastVisitedLocation().location :
                    null);
            userLocations.add(userLocation);
        }
        return userLocations;
    }


    public VisitedLocation trackUserLocation(UserBean user) {
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        serviceRewardsProxy.calculateRewards(user);
        return visitedLocation;
    }

    public FiveNearestAttractions get5NearestAttractions(VisitedLocation visitedLocation) {
        Map<Double, Attraction> attractionsByDistance = new TreeMap<>();
        FiveNearestAttractions fiveNearestAttractions = new FiveNearestAttractions();
        List<String> attractionsName = new ArrayList<>();
        List<Location> attractionsLocation = new ArrayList<>();
        List<Double> attractionsDistance = new ArrayList<>();
        List<Integer> attractionsRewardPoints = new ArrayList<>();
        int gatheredReward = 0;
        for (Attraction attraction : gpsUtil.getAttractions()) {
            attractionsByDistance.put(getDistance(attraction, visitedLocation.location), attraction);
        }

        attractionsByDistance.forEach((distance, attraction) -> {
            if (attractionsName.size() < 5) {
                attractionsName.add(attraction.attractionName);
                attractionsLocation.add(new Location(attraction.longitude, attraction.latitude));
                attractionsDistance.add(getDistance(attraction, visitedLocation.location));
                attractionsRewardPoints.add(serviceRewardsProxy.getRewards(attraction.attractionId, visitedLocation.userId));
            }
        });
        fiveNearestAttractions.setAttractionName(attractionsName);
        fiveNearestAttractions.setLatLongUser(visitedLocation.location);
        fiveNearestAttractions.setLatLongAttraction(attractionsLocation);
        fiveNearestAttractions.setDistance(attractionsDistance);
        for (Integer rewardPoints : attractionsRewardPoints) {
            gatheredReward += rewardPoints;
        }
        fiveNearestAttractions.setAttractionRewardPoints(gatheredReward);
        return fiveNearestAttractions;
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

    private boolean nearAttraction(Location visitedLocation, Attraction attraction) {
        if (Math.abs(attraction.longitude - visitedLocation.longitude) < proximityBuffer) {
            if (Math.abs(attraction.latitude - visitedLocation.latitude) < proximityBuffer) {
                return getDistance(attraction, visitedLocation) > proximityBuffer ? false : true;
            }
        }
        return false;
    }


    public List<Attraction> pertinenteAttractions(UserBean user) {
        List<Attraction> pertinentAttractions = new ArrayList<>();

        return pertinentAttractions;
    }

    public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
        List<Attraction> nearbyAttractions = new ArrayList<>();

        for (Attraction attraction : gpsUtil.getAttractions()) {
            if (getDistance(attraction, visitedLocation.location) <= attractionProximityRange) {
                nearbyAttractions.add(attraction);
                if (nearbyAttractions.size() > 4) {
                    return nearbyAttractions;
                }
            }
        }

        return nearbyAttractions;
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }


    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/
    private static int internalUserNumber = 100;

    public static void setInternalUserNumber(int internalUserNumber) {
        internalUserNumber = internalUserNumber;
    }

    private static final String tripPricerApiKey = "test-server-api-key";
    // Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
    private final Map<String, UserBean> internalUserMap = new HashMap<>();

    private void initializeInternalUsers() {
        IntStream.range(0, internalUserNumber).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            UserBean user = new UserBean(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            internalUserMap.put(userName, user);
        });
        log.debug("Created " + internalUserNumber + " internal test users.");
    }

    private void generateUserLocationHistory(UserBean user) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

}
