package tourGuideTracker.service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import tourGuideTracker.domain.Provider;
import tourGuideTracker.domain.TripPricerTask;
import tourGuideTracker.domain.UserReward;


@Service
public class RewardsService {

    private static final String tripPricerApiKey = "test-server-api-key";

    public List<Provider> getTripDeals(TripPricerTask tripPricerTask, double cumulatativeRewardPoints) {
        List<Provider> providers = getPrice(tripPricerTask, cumulatativeRewardPoints);
        return providers;
    }


    public Integer cumulatativeRewardPoints(List<UserReward> userRewards){
        return userRewards.stream().mapToInt(i -> i.getRewardPoints()).sum();
    }

    public Integer calculateRewards(Set<UUID> attractionsId, UUID userId) {
        Integer allpoints = 0;
        for (UUID attraction : attractionsId) {
            allpoints += getAttractionRewardPoints(attraction, userId);
        }
        return allpoints;
    }

    private int getAttractionRewardPoints(UUID attraction, UUID userId) {
        //TODO this method need to be completed in order to get a coherent amount of rewardPoint
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }


    public List<Provider> getPrice(TripPricerTask tripPricerTask, double rewards) {
        List<Provider> providers = new ArrayList();
        HashSet providersUsed = new HashSet();

        for (int i = 0; i < 5; ++i) {
            int multiple = ThreadLocalRandom.current().nextInt(100, 700);
            double childrenDiscount = (tripPricerTask.getChildren() / 3);
            double price =
                    (double) (multiple * tripPricerTask.getAdults())
                    + (double) multiple * childrenDiscount
                            * (double) tripPricerTask.getNightsStay()
                            + 0.99D
                            - rewards;
            if (price < 0.0D) {
                price = 0.0D;
            }

            providers.add(new Provider(
                    tripPricerTask.getAttractionId(),
                    getProviderName(tripPricerTask.getApiKey(), tripPricerTask.getAdults()),
                    price));
        }

        return providers;
    }

    public String getProviderName(String apiKey, int adults) {
        int multiple = ThreadLocalRandom.current().nextInt(1, 10);
        switch (multiple) {
            case 1:
                return "Holiday Travels";
            case 2:
                return "Enterprize Ventures Limited";
            case 3:
                return "Sunny Days";
            case 4:
                return "FlyAway Trips";
            case 5:
                return "United Partners Vacations";
            case 6:
                return "Dream Trips";
            case 7:
                return "Live Free";
            case 8:
                return "Dancing Waves Cruselines and Partners";
            case 9:
                return "AdventureCo";
            default:
                return "Cure-Your-Blues";
        }
    }
}
