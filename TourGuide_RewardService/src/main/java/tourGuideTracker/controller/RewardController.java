package tourGuideTracker.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tourGuideTracker.service.RewardsService;

@RestController
public class RewardController {

    @Autowired
    RewardsService rewardsService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
/*
    @RequestMapping("/getReward")
    public String getReward(@RequestParam String userName, @RequestParam String attractionName) {
        return rewardsService.calculateRewards(userName);
    }*/

    @RequestMapping("/calculateRewards")
    public Integer calculateRewards(@RequestParam Set<UUID> attractions, @RequestParam UUID userId) {
        return rewardsService.calculateRewards(attractions, userId);
    }


}