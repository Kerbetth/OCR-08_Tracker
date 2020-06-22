package tourGuideTracker;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;


import org.junit.jupiter.api.BeforeEach;
import tourGuideTracker.client.dto.TrackerService.VisitedLocation;
import tourGuideTracker.client.dto.TrackerService.location.Attraction;
import tourGuideTracker.client.ServiceUserProxy;
import tourGuideTracker.service.RewardsService;
import tourGuideTracker.domain.User;

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

        List<User> allUsers = new ArrayList<>();
        allUsers = serviceUserProxy.getAllUsers();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (User user : allUsers) {
            trackerService.trackUserLocation(user);
        }
        stopWatch.stop();
        trackerService.tracker.stopTracking();

        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


    @Test
    public void highVolumeGetRewards() {
        RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        trackerService.setInternalUserNumber(10_000);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TrackerService trackerService = new TrackerService(gpsUtil);

        Attraction attraction = gpsUtil.getAttractions().get(0);
        List<User> allUsers = new ArrayList<>();
        allUsers = serviceUserProxy.getAllUsers();
        allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

        allUsers.forEach(u -> rewardsService.calculateRewards(u));
        for (User user : allUsers) {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
            rewardsService.calculateRewards(user);
        }

        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);
        }
        stopWatch.stop();
        trackerService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
