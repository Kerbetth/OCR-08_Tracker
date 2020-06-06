package tourGuideTracker.service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.user.User;
import tourGuideTracker.user.UserReward;

@Service
public class RewardsService {

    private final RewardCentral rewardsCentral;

    public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
        this.gpsUtil = gpsUtil;
        this.rewardsCentral = rewardCentral;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    public void calculateRewards(User user) {
        CopyOnWriteArrayList<Attraction> attractions = new CopyOnWriteArrayList<>() ;
        CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
        attractions.addAll(gpsUtil.getAttractions());
        userLocations.addAll(user.getVisitedLocations());

        userLocations
                .stream()
                .forEach(visitedLocation -> attractions
                        .stream()
                        .filter(attraction -> nearAttraction(visitedLocation.location, attraction))
                        .forEach(attraction -> {
                            if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                                user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                            }
                        }));

    }


    private int getRewardPoints(Attraction attraction, User user) {
        //rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }





}
