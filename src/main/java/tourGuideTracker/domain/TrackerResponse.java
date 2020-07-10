package tourGuideTracker.domain;

import tourGuideTracker.domain.location.Attraction;
import tourGuideTracker.domain.location.Location;

import java.util.Date;
import java.util.UUID;

public class TrackerResponse {
    public final VisitedLocation visitedLocation;
    public final Attraction attraction;

    public TrackerResponse(VisitedLocation visitedLocation, Attraction attraction) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
    }
}
