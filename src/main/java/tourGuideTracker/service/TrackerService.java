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


    public Map<UUID, Location> getCurrentLocationOfAllUsers(List<String> userIds) {
        Map<UUID, Location> userLocations = new HashMap<>();
        for (String userId : userIds) {
            Location userLocation = trackUserLocation(UUID.fromString(userId)).location;
            userLocations.put(UUID.fromString(userId), userLocation);
        }
        return userLocations;
    }

    public VisitedLocation trackUserLocation(UUID userId) {
        return gpsUtil.getUserLocation(userId);
    }

    public Set<UUID> getAllVisitedAttraction(List<VisitedLocation> visitedLocations) {
        Set<UUID> attractions =new HashSet<>();
        for(VisitedLocation visitedLocation :visitedLocations) {
            attractions.addAll(getVisitedAttraction(visitedLocation));
        }
        return attractions;
    }

    public FiveNearestAttractions get5NearestAttractions(Location location) {
        Map<Double, Attraction> attractionsByDistance = new TreeMap<>();
        FiveNearestAttractions fiveNearestAttractions = new FiveNearestAttractions();
        List<String> attractionsName = new ArrayList<>();
        List<Location> attractionsLocation = new ArrayList<>();
        List<Double> attractionsDistance = new ArrayList<>();
        int gatheredReward = 0;
        for (Attraction attraction : gpsUtil.getAttractions()) {
            attractionsByDistance.put(getDistance(attraction, location), attraction);
        }

        attractionsByDistance.forEach((distance, attraction) -> {
            if (attractionsName.size() < 5) {
                attractionsName.add(attraction.attractionName);
                attractionsLocation.add(new Location(attraction.longitude, attraction.latitude));
                attractionsDistance.add(getDistance(attraction, location));
            }
        });
        fiveNearestAttractions.setAttractionName(attractionsName);
        fiveNearestAttractions.setLatLongUser(location);
        fiveNearestAttractions.setLatLongAttraction(attractionsLocation);
        fiveNearestAttractions.setDistance(attractionsDistance);
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
        return getDistance(attraction, visitedLocation) > proximityBuffer ? false : true;
    }


    public List<UUID> getVisitedAttraction(VisitedLocation visitedLocation) {
        List<UUID> nearbyAttractions = new ArrayList<>();

        for (Attraction attraction : gpsUtil.getAttractions()) {
            if (getDistance(attraction, visitedLocation.location) <= 1) {
                nearbyAttractions.add(attraction.attractionId);
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


    public boolean isAttractionLocation(Location location) {
        for (Attraction attraction : gpsUtil.getAttractions()) {
            if (getDistance(attraction, location) <= 1) {
                return true;
            }
        }
        return false;
    }
}
