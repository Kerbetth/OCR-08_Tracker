package tourGuideTracker.service;

import java.util.*;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tourGuideTracker.repository.GpsUtil;
import tourGuideTracker.domain.location.Attraction;
import tourGuideTracker.domain.location.Location;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.tracker.Tracker;


@Service
@Slf4j
public class TrackerService {

    @Autowired
    private GpsUtil gpsUtil;

    public final Tracker tracker;

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private int proximityBuffer = 10;
    private int attractionProximityRange = 200;


    public TrackerService() {
        tracker = new Tracker(this);
        addShutDownHook();
    }



    public Map<UUID, Location> getLocationOfAllUsers(List<UUID> userIds) {
        Map<UUID, Location> userLocations = new HashMap<>();
        for (UUID userId : userIds) {
            Location userLocation = trackUserLocation(userId).location;
            userLocations.put(userId, userLocation);
        }
        return userLocations;
    }


    public VisitedLocation trackUserLocation(UUID userId) {
        return gpsUtil.getUserLocation(userId);
    }

    public FiveNearestAttractions get5NearestAttractions(Location location) {
        Map<Double, Attraction> attractionsByDistance = new TreeMap<>();
        FiveNearestAttractions fiveNearestAttractions = new FiveNearestAttractions();
        List<String> attractionsName = new ArrayList<>();
        List<Location> attractionsLocation = new ArrayList<>();
        List<Double> attractionsDistance = new ArrayList<>();
        List<Integer> attractionsRewardPoints = new ArrayList<>();
        int gatheredReward = 0;
        for (Attraction attraction : gpsUtil.getAttractions()) {
            attractionsByDistance.put(getDistance(attraction, location), attraction);
        }

        attractionsByDistance.forEach((distance, attraction) -> {
            if (attractionsName.size() < 5) {
                attractionsName.add(attraction.attractionName);
                attractionsLocation.add(new Location(attraction.longitude, attraction.latitude));
                attractionsDistance.add(getDistance(attraction, location));
                //attractionsRewardPoints.add(serviceRewardsProxy.getRewards(attraction.attractionId, visitedLocation.userId));
            }
        });
        fiveNearestAttractions.setAttractionName(attractionsName);
        fiveNearestAttractions.setLatLongUser(location);
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

    public boolean isNearAttraction(Location visitedLocation, Attraction attraction) {
        if (Math.abs(attraction.longitude - visitedLocation.longitude) < proximityBuffer) {
            if (Math.abs(attraction.latitude - visitedLocation.latitude) < proximityBuffer) {
                return getDistance(attraction, visitedLocation) > proximityBuffer ? false : true;
            }
        }
        return false;
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

}
