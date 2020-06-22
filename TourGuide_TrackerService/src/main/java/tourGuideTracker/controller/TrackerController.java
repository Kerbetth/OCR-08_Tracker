package tourGuideTracker.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Attraction;
import tourGuideTracker.domain.location.Location;
import tourGuideTracker.service.TrackerService;

@RestController
public class TrackerController {

    @Autowired
    TrackerService trackerService;


    @GetMapping("/getLocation")
    public VisitedLocation getLocation(@RequestParam UUID userId) {
        return trackerService.trackUserLocation(userId);
    }

    @GetMapping("/get5NearestAttractions")
    public FiveNearestAttractions get5NearestAttractions(@RequestParam Location location) {
        return trackerService.get5NearestAttractions(location);
    }

    @GetMapping("/getAllCurrentLocations")
    public Map<UUID, Location> getAllCurrentLocations(List<UUID> userId) {
        return trackerService.getCurrentLocationOfAllUsers(userId);
    }

    @GetMapping("/getAllVisitedLocations")
    public Set<Attraction> getAllVisitedAttractions(List<VisitedLocation> visitedLocations) {
        return trackerService.getAllVisitedAttraction(visitedLocations);
    }
}