package tourguidetracker.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tourguidetracker.domain.location.Location;

import java.util.List;


public class FiveNearestAttractions {

    private Location latLongUser;
    private List<NearAttraction> fiveNearestAttractions;

    @JsonCreator
    public FiveNearestAttractions(@JsonProperty("latLongUser") Location latLongUser, @JsonProperty("fiveNearestAttractions") List<NearAttraction> fiveNearestAttractions) {
        this.latLongUser = latLongUser;
        this.fiveNearestAttractions = fiveNearestAttractions;
    }

    public Location getLatLongUser() {
        return latLongUser;
    }

    public void setLatLongUser(Location latLongUser) {
        this.latLongUser = latLongUser;
    }

    public List<NearAttraction> getFiveNearestAttractions() {
        return fiveNearestAttractions;
    }

    public void setFiveNearestAttractions(List<NearAttraction> fiveNearestAttractions) {
        this.fiveNearestAttractions = fiveNearestAttractions;
    }
}
