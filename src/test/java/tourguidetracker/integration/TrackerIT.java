package tourguidetracker.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourguidetracker.controller.TrackerController;
import tourguidetracker.domain.FiveNearestAttractions;
import tourguidetracker.domain.TrackerResponse;
import tourguidetracker.domain.location.Location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
public class TrackerIT {

    @Autowired
    TrackerController trackerControllers;

    @Test
    public void trackUserLocation() {
        UUID uuid = UUID.randomUUID();
        TrackerResponse visitedLocation = trackerControllers.trackUserLocation(uuid.toString());
        assertThat(visitedLocation.visitedLocation.userId).isEqualTo(uuid);
    }

    @Test
    public void get5NearestAttractions() {
        Location location = new Location(2.0, 1.0);
        FiveNearestAttractions fiveNearestAttractions = trackerControllers.get5NearestAttractions(location);
        assertThat(fiveNearestAttractions.getLatLongUser()).isEqualTo(location);
        assertThat(fiveNearestAttractions.getFiveNearestAttractions()).hasSize(5);
    }

    @Test
    public void getCurrentLocationOfAllUsers() {
        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        List<String> uuids = new ArrayList<>();
        uuids.add(uuid.toString());
        uuids.add(uuid2.toString());
        Map<UUID, Location> uuidLocationMap = trackerControllers.getCurrentLocationOfAllUsers(uuids);
        assertThat(uuidLocationMap).hasSize(2);
    }
}
