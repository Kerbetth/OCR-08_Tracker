package tourGuideTracker.clients.dto.UserService;

import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Attraction;

public class UserRewardBean {
    public final VisitedLocation visitedLocation;
    public final Attraction attraction;
    private int rewardPoints;

    public UserRewardBean(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
    }

    public UserRewardBean(VisitedLocation visitedLocation, Attraction attraction) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

}
