package tourguidetracker.service;

import java.util.*;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tourguidetracker.domain.NearAttraction;
import tourguidetracker.domain.TrackerResponse;
import tourguidetracker.domain.VisitedLocation;
import tourguidetracker.domain.location.Attraction;
import tourguidetracker.repository.GpsUtil;
import tourguidetracker.domain.location.Location;
import tourguidetracker.domain.FiveNearestAttractions;


@Service
@Slf4j
public class TrackerService {

    @Autowired
    private GpsUtil gpsUtil;

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private int proximityBuffer = 1;
    private int attractionProximityRange = 200;


    public Map<UUID, Location> getCurrentLocationOfAllUsers(List<String> userIds) {
        Map<UUID, Location> userLocations = new HashMap<>();
        for (String userId : userIds) {
            Location userLocation = trackUserLocation(userId).visitedLocation.location;
            userLocations.put(UUID.fromString(userId), userLocation);
        }
        return userLocations;
    }

    public TrackerResponse trackUserLocation(String userId) {
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(UUID.fromString(userId));
        log.info("User with ID:" + userId + " has been tracked");
        tourguidetracker.domain.location.Attraction attraction = isAttractionLocation(visitedLocation.location);
        if (attraction != null) {
            log.info("User with ID:" + userId + " has visited for the first time: " + attraction.attractionName);

        }
        return new TrackerResponse(visitedLocation, attraction);

    }

    public FiveNearestAttractions get5NearestAttractions(Location location) {
        Map<Double, tourguidetracker.domain.location.Attraction> attractionsByDistance = new TreeMap<>();
        FiveNearestAttractions fiveNearestAttractions = new FiveNearestAttractions(location, new ArrayList<>());
        for (tourguidetracker.domain.location.Attraction attraction : gpsUtil.getAttractions()) {
            attractionsByDistance.put(getDistance(attraction, location), attraction);
        }

        attractionsByDistance.forEach((distance, attraction) -> {
            if (fiveNearestAttractions.getFiveNearestAttractions().size() < 5) {
                NearAttraction nearAttraction = new NearAttraction();
                nearAttraction.setAttractionName(attraction.attractionName);
                nearAttraction.setLocation(new Location(attraction.latitude, attraction.longitude));
                nearAttraction.setDistance(getDistance(attraction, location));
                fiveNearestAttractions.getFiveNearestAttractions().add(nearAttraction);
            }
        });
        fiveNearestAttractions.setLatLongUser(location);
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

    public boolean isNearAttraction(Location visitedLocation, tourguidetracker.domain.location.Attraction attraction) {
        return getDistance(attraction, visitedLocation) > proximityBuffer ? false : true;
    }


    public List<UUID> getVisitedAttraction(VisitedLocation visitedLocation) {
        List<UUID> nearbyAttractions = new ArrayList<>();

        for (tourguidetracker.domain.location.Attraction attraction : gpsUtil.getAttractions()) {
            if (isNearAttraction(visitedLocation.location, attraction)) {
                nearbyAttractions.add(attraction.attractionId);
            }
        }

        return nearbyAttractions;
    }

    public tourguidetracker.domain.location.Attraction isAttractionLocation(Location location) {
        return gpsUtil.getAttractions()
                .stream()
                .filter(attraction -> getDistance(attraction, location) < 1)
                .findFirst()
                .orElse(null);
    }
}
