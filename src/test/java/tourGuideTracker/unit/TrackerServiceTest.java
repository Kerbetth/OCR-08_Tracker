package tourGuideTracker.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuideTracker.DataTest;
import tourGuideTracker.clients.dto.UserService.User;
import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.domain.UserLocation;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Attraction;
import tourGuideTracker.domain.location.Location;
import tourGuideTracker.repository.GpsUtil;
import tourGuideTracker.clients.ServiceRewardsProxy;
import tourGuideTracker.clients.ServiceUserProxy;
import tourGuideTracker.service.TrackerService;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TrackerServiceTest {

    @Mock
    private GpsUtil gpsUtil;
    @Mock
    private ServiceRewardsProxy serviceRewardsProxy;
    @Mock
    private ServiceUserProxy serviceUserProxy;

    private final DataTest dataTest = new DataTest();
    private ArrayList<User> users;

    @InjectMocks
    TrackerService trackerService = new TrackerService();

    @BeforeEach
    void setup() {
        users = new ArrayList<>();
        User user = new User(UUID.randomUUID(), "user1", "000-555-444", "user1@mail.com");
        User user2 = new User(UUID.randomUUID(), "user2", "000-666-444", "user2@mail.com");
        user.addToVisitedLocations(new VisitedLocation(UUID.randomUUID(), new Location(1.0, 2.0), new Date()));
        users.add(user);
        users.add(user2);
        when(serviceUserProxy.getUser(anyString())).thenReturn(user);
        when(serviceUserProxy.getAllUsers()).thenReturn(users);
        when(serviceRewardsProxy.getRewards(any(), any())).thenReturn(1);
        when(gpsUtil.getAttractions()).thenReturn(dataTest.getAttractionsForTest());
        when(gpsUtil.getUserLocation(any())).thenReturn(user.getLastVisitedLocation());

    }

    @Test
    public void getLocationShouldReturnGoodVisitedLocation() {
        //ACT
        VisitedLocation visitedLocation = trackerService.getUserLocation(users.get(0).getUserName());

        //ASSERT
        assertThat(visitedLocation.location.latitude).isEqualTo(1.0);
        assertThat(visitedLocation.location.longitude).isEqualTo(2.0);
        assertThat(visitedLocation.timeVisited).isBefore(new Date());
    }

    @Test
    public void getLocationOfAllUsersShouldReturnGoodUserLocations() {
        //ACT
        List<UserLocation> userLocations = trackerService.getLocationOfAllUsers();

        //ASSERT
        assertThat(userLocations).hasSize(2);
    }

    @Test
    public void get5NearestAttractionsMethodShouldReturn5NearestAttractions() {
        Location location = new Location(1.0, 2.0);
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
        //ACT
        FiveNearestAttractions fiveNearestAttractions = trackerService.get5NearestAttractions(visitedLocation);

        //ASSERT
        assertThat(fiveNearestAttractions.getAttractionName()).hasSize(5);
        assertThat(fiveNearestAttractions.getDistance()).hasSize(5);
        assertThat(fiveNearestAttractions.getLatLongAttraction()).hasSize(5);
        assertThat(fiveNearestAttractions.getLatLongUser()).isEqualTo(location);
        assertThat(fiveNearestAttractions.getAttractionRewardPoints()).isEqualTo(5L);
    }

    @Test
    public void getDistanceShouldReturnGoodValue() {
        double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
        Location location1 = new Location(0.0, 0.0);
        Location location2 = new Location(3.0, 4.0);

        //ACT
        double distance = trackerService.getDistance(location1, location2);

        //ASSERT
        assertThat(distance).isEqualTo(299.9122127962214*STATUTE_MILES_PER_NAUTICAL_MILE);
    }

    @Test
    public void isNearAttractionShouldReturnTrueIfLessThan5Miles() {
        Location location = new Location(33.81, -117.92);
        Location location2 = new Location(33, -117);
        Attraction attraction = dataTest.getAttractionsForTest().get(0);

        //ACT
        boolean isNear = trackerService.isNearAttraction(location, attraction);
        boolean isNear2 = trackerService.isNearAttraction(location2, attraction);

        //ASSERT
        assertTrue(isNear);
        assertFalse(isNear2);
    }

    @Test
    public void trackUserLocationShouldReturnLastVisitedLocation() {
        //ACT
        VisitedLocation visitedLocation = trackerService.trackUserLocation(users.get(0));

        //ASSERT
        assertThat(visitedLocation).isEqualTo(users.get(0).getLastVisitedLocation());
    }
}