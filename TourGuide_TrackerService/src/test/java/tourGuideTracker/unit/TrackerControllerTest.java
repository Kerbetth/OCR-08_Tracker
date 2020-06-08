package tourGuideTracker.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuideTracker.DataTest;
import tourGuideTracker.bean.UserService.UserBean;
import tourGuideTracker.controller.TrackerController;
import tourGuideTracker.domain.FiveNearestAttractions;
import tourGuideTracker.domain.UserLocation;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Attraction;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TrackerControllerTest {

    @Mock
    private TrackerService trackerService;

    @InjectMocks
    TrackerController trackerController = new TrackerController();

    @BeforeEach
    void setup() {
        List<UserLocation> userLocations = new ArrayList<>();
        when(trackerService.getLocationOfAllUsers()).thenReturn(userLocations);

    }

    @Test
    public void getAllCurrentLocationsShouldReturnAListOfUserLocation() {
        //ACT
        List<UserLocation> userLocations = trackerController.getAllCurrentLocations();

        //ASSERT
        assertThat(userLocations).hasSize(0);
    }

}
