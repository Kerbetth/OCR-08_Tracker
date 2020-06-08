package unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import tourGuideTracker.bean.UserService.UserBean;
import tourGuideTracker.domain.UserLocation;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Location;
import tourGuideTracker.repository.GpsUtil;
import tourGuideTracker.repository.proxy.ServiceRewardsProxy;
import tourGuideTracker.repository.proxy.ServiceUserProxy;
import tourGuideTracker.service.TrackerService;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TrackerServiceTest {

    @Mock
    GpsUtil gpsUtil;
    @Mock
    private ServiceRewardsProxy serviceRewardsProxy;
    @Mock
    private ServiceUserProxy serviceUserProxy;

    private ArrayList<UserBean> userBeans;

    @InjectMocks
    TrackerService trackerService = new TrackerService(gpsUtil);

    @BeforeEach
    void setup() {
        userBeans = new ArrayList<>();
        UserBean user = new UserBean(UUID.randomUUID(), "user1", "000-555-444", "user1@mail.com");
        UserBean user2 = new UserBean(UUID.randomUUID(), "user2", "000-666-444", "user2@mail.com");
        user.addToVisitedLocations(new VisitedLocation(UUID.randomUUID(), new Location(1.0,2.0), new Date()));
        userBeans.add(user);
        userBeans.add(user2);
        when(serviceUserProxy.getUser(any())).thenReturn(user);
        when(serviceUserProxy.getAllUsers()).thenReturn(userBeans);

    }

    @Test
    public void getLocationShouldReturnGoodVisitedLocation() {
        //ACT
        VisitedLocation visitedLocation = trackerService.getUserLocation(userBeans.get(0).getUserName());

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
}
