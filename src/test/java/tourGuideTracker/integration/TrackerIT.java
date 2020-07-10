package tourGuideTracker.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tourGuideTracker.controller.TrackerController;
import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.domain.TrackerResponse;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TrackerIT {

    @Autowired
    TrackerController trackerControllers;

    @Test
    public void getLocation() {
        UUID uuid = UUID.randomUUID();
        TrackerResponse visitedLocation = trackerControllers.trackUserLocation(uuid.toString(),null);
        assertThat(visitedLocation.visitedLocation.userId).isEqualTo(uuid);
    }

    @Test
    public void get5NearestAttractions() {
        Location location = new Location(2.0, 1.0);
        FiveNearestAttractions fiveNearestAttractions = trackerControllers.get5NearestAttractions(location);
        assertThat(fiveNearestAttractions.getLatLongUser()).isEqualTo(location);
        assertThat(fiveNearestAttractions.getAttractionName()).hasSize(5);
        assertThat(fiveNearestAttractions.getDistance()).hasSize(5);
        assertThat(fiveNearestAttractions.getLatLongAttraction()).hasSize(5);
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
        int iteration = 0;
        for (Map.Entry<UUID, Location> entry : uuidLocationMap.entrySet()) {
            assertThat(entry.getKey()).isEqualTo(uuids.get(iteration));
            iteration++;
        }

    }

    @Test
    public void getAllVisitedAttractions() {
        List<VisitedLocation> userIds = new ArrayList<>();
        userIds.add(new VisitedLocation(UUID.randomUUID(),
                new Location(
                        ThreadLocalRandom.current().nextDouble(-180.0D, 180.0D)
                        ,ThreadLocalRandom.current().nextDouble(-85.0D, 85.0D))
              , new Date()));
        userIds.add(new VisitedLocation(UUID.randomUUID(), new Location(33.817595D, -117.922008D), new Date()));

        Set<UUID> uuids = trackerControllers.getAllVisitedAttractions(userIds);
        assertThat(uuids).hasSize(1);
    }

    @Test
    public void getNewVisitedAttraction() {

    }
}
