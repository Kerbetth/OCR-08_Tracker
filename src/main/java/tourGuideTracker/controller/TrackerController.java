package tourGuideTracker.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Attraction;
import tourGuideTracker.domain.location.Location;
import tourGuideTracker.service.TrackerService;

@RestController
public class TrackerController {

    @Autowired
    TrackerService trackerService;

    @GetMapping("/")
    public String welcome() {
        return "You are on the tracker server of TourGuide";
    }

    @GetMapping("/getLocation")
    public VisitedLocation getLocation(@RequestParam UUID userId) {
        return trackerService.trackUserLocation(userId);
    }

    @RequestMapping("/get5NearestAttractions")
    public FiveNearestAttractions get5NearestAttractions(@RequestBody Location location) {
        return trackerService.get5NearestAttractions(location);
    }

    @GetMapping("/getAllCurrentLocations")
    public Map<UUID, Location> getAllCurrentLocations(@RequestParam List<String> userId) {
        return trackerService.getCurrentLocationOfAllUsers(userId);
    }

    @RequestMapping("/getAllVisitedAttractions")
    public Set<UUID> getAllVisitedAttractions(@RequestBody List<VisitedLocation> visitedLocations) {
        return trackerService.getAllVisitedAttraction(visitedLocations);
    }
}