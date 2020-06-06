package tourGuideTracker.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuideTracker.bean.UserService.UserBean;
import tourGuideTracker.user.UserReward;
import tripPricer.Provider;

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

    public void calculateRewards(UserBean user) {
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

    public List<Provider> getTripDeals(UserBean user) {
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }
    private int getRewardPoints(Attraction attraction, UserBean user) {
        //rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }





}
