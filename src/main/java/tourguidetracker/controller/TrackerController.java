package tourguidetracker.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import tourguidetracker.domain.FiveNearestAttractions;
import tourguidetracker.domain.TrackerResponse;
import tourguidetracker.domain.location.Location;
import tourguidetracker.service.TrackerService;

@RestController
public class TrackerController {

    @Autowired
    TrackerService trackerService;

    @RequestMapping("/trackUserLocation")
    public TrackerResponse trackUserLocation(@RequestParam String userId) {
        return trackerService.trackUserLocation(userId);
    }

    @RequestMapping("/get5NearestAttractions")
    public FiveNearestAttractions get5NearestAttractions(@RequestBody Location location) {
        return trackerService.get5NearestAttractions(location);
    }

    @RequestMapping("/getCurrentLocationOfAllUsers")
    public Map<UUID, Location> getCurrentLocationOfAllUsers(@RequestBody List<String> userId) {
        return trackerService.getCurrentLocationOfAllUsers(userId);
    }
}