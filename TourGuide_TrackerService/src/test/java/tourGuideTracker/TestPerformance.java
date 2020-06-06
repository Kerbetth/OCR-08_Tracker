package tourGuideTracker;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;


import org.junit.jupiter.api.BeforeEach;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Attraction;
import tourGuideTracker.repository.proxy.ServiceUserProxy;
import tourGuideTracker.service.TrackerService;
import tourGuideTracker.bean.UserService.UserBean;
import tourGuideTracker.repository.GpsUtil;

public class TestPerformance {

    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */
    TrackerService trackerService;
    GpsUtil gpsUtil;
    ServiceUserProxy serviceUserProxy;


    @BeforeEach
    void before() {
        gpsUtil = new GpsUtil();
        trackerService = new TrackerService(gpsUtil);
    }


    @Test
    public void highVolumeTrackLocation() {

        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        trackerService.setInternalUserNumber(100);

        List<UserBean> allUsers = new ArrayList<>();
        allUsers = serviceUserProxy.getAllUsers();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (UserBean user : allUsers) {
            trackerService.trackUserLocation(user);
        }
        stopWatch.stop();
        trackerService.tracker.stopTracking();

        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
}
