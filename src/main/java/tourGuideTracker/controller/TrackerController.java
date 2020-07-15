package tourGuideTracker.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.domain.TrackerResponse;
import tourGuideTracker.domain.UserReward;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Attraction;
import tourGuideTracker.domain.location.Location;
import tourGuideTracker.service.TrackerService;

@RestController
public class TrackerController {

    @Autowired
    TrackerService trackerService;

    @RequestMapping("/trackUserLocation")
    public TrackerResponse trackUserLocation(@RequestParam String userId, @RequestBody List<String> attractionId) {
        return trackerService.trackUserLocation(userId, attractionId);
    }

    @RequestMapping("/get5NearestAttractions")
    public FiveNearestAttractions get5NearestAttractions(@RequestBody Location location) {
        return trackerService.get5NearestAttractions(location);
    }

    @RequestMapping("/getCurrentLocationOfAllUsers")
    public Map<UUID, Location> getCurrentLocationOfAllUsers(@RequestBody List<String> userId) {
        return trackerService.getCurrentLocationOfAllUsers(userId);
    }

    @RequestMapping("/getAllVisitedAttractions")
    public Set<UUID> getAllVisitedAttractions(@RequestBody List<VisitedLocation> visitedLocations) {
        return trackerService.getAllVisitedAttraction(visitedLocations);
    }
/*
    @RequestMapping("/getNewVisitedAttraction")
    public Attraction getNewVisitedAttraction(@RequestParam double longitude, @RequestParam double latitude, @RequestBody List<UserReward> userRewards) {
        return trackerService.getNewVisitedAttraction(longitude, latitude, userRewards);
    }*/
}