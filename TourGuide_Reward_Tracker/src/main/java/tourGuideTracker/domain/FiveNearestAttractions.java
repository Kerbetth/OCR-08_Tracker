package tourGuideTracker.domain;

import gpsUtil.location.Location;

import java.util.List;


public class FiveNearestAttractions {
    private List<String> attractionName;
    private List<Location> latLongAttraction;
    private Location latLongUser;
    private List<Double> distance;
    private int attractionRewardPoints;


    public List<String> getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(List<String> attractionName) {
        this.attractionName = attractionName;
    }

    public List<Location> getLatLongAttraction() {
        return latLongAttraction;
    }

    public void setLatLongAttraction(List<Location> latLongAttraction) {
        this.latLongAttraction = latLongAttraction;
    }

    public Location getLatLongUser() {
        return latLongUser;
    }

    public void setLatLongUser(Location latLongUser) {
        this.latLongUser = latLongUser;
    }

    public List<Double> getDistance() {
        return distance;
    }

    public void setDistance(List<Double> distance) {
        this.distance = distance;
    }

    public int getAttractionRewardPoints() {
        return attractionRewardPoints;
    }

    public void setAttractionRewardPoints(int attractionRewardPoints) {
        this.attractionRewardPoints = attractionRewardPoints;
    }
}
