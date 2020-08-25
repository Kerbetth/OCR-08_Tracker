package tourguidetracker.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import tourguidetracker.domain.location.Attraction;

public class UserReward {

    public final VisitedLocation visitedLocation;
    public final tourguidetracker.domain.location.Attraction attraction;
    private int rewardPoints;

    public UserReward(VisitedLocation visitedLocation, tourguidetracker.domain.location.Attraction attraction, int rewardPoints) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
    }

    @JsonCreator
    public UserReward(VisitedLocation visitedLocation, tourguidetracker.domain.location.Attraction attraction) {
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
