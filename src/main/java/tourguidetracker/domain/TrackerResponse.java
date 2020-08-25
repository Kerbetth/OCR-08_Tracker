package tourguidetracker.domain;

import tourguidetracker.domain.location.Attraction;

public class TrackerResponse {
    public final VisitedLocation visitedLocation;
    public final tourguidetracker.domain.location.Attraction attraction;

    public TrackerResponse(VisitedLocation visitedLocation, tourguidetracker.domain.location.Attraction attraction) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
    }
}
