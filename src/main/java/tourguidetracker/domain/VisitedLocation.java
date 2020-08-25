package tourguidetracker.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tourguidetracker.domain.location.Location;

import java.util.Date;
import java.util.UUID;

public class VisitedLocation {
    public final UUID userId;
    public final Location location;
    public final Date timeVisited;

    @JsonCreator
    public VisitedLocation(@JsonProperty("userId") UUID userId, @JsonProperty("location") Location location, @JsonProperty("timeVisited") Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }
}
