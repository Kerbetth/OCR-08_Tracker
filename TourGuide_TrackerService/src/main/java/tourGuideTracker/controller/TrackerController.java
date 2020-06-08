package tourGuideTracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.domain.UserLocation;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.service.TrackerService;

@RestController
public class TrackerController {

    @Autowired
    TrackerService trackerService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    @RequestMapping("/getLocation")
    public VisitedLocation getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = trackerService.getUserLocation(userName);
        return visitedLocation;
    }

    //  TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public FiveNearestAttractions getNearbyAttractions(@RequestParam String userName) {
        VisitedLocation visitedLocation = trackerService.getUserLocation(userName);
        return trackerService.get5NearestAttractions(visitedLocation);
    }

    @RequestMapping("/getAllCurrentLocations")
    public List<UserLocation> getAllCurrentLocations() {
        // TODO: Get a list of every user's most recent location as JSON
        //- Note: does not use gpsUtil to query for their current location,
        //        but rather gathers the user's current location from their stored location history.
        //
        // Return object should be the just a JSON mapping of userId to Locations similar to:
        //     {
        //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
        //        ...
        //     }
        return trackerService.getLocationOfAllUsers();
    }
}