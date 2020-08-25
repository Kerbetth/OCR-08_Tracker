package tourguidetracker.domain;


import tourguidetracker.domain.location.Attraction;
import tourguidetracker.domain.location.Location;

public class NearAttraction {
    private String attractionName;
    private Location location;
    private Double distance;
    private int attractionRewardPoints;

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getAttractionRewardPoints() {
        return attractionRewardPoints;
    }

    public void setAttractionRewardPoints(int attractionRewardPoints) {
        this.attractionRewardPoints = attractionRewardPoints;
    }
}
