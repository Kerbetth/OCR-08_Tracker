package tourGuideTracker;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuideTracker.helper.InternalTestHelper;
import tourGuideTracker.service.RewardsService;
import tourGuideTracker.service.TrackerService;
import tourGuideTracker.bean.UserService.UserBean;
import tourGuideTracker.user.UserReward;

public class TestRewardsService {

	@Test
	public void userGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());

		InternalTestHelper.setInternalUserNumber(0);
		TrackerService trackerService = new TrackerService(gpsUtil, rewardsService);
		
		UserBean user = new UserBean(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		trackerService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		trackerService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	@Ignore // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TrackerService trackerService = new TrackerService(gpsUtil, rewardsService);
		
		rewardsService.calculateRewards(trackerService.getAllUsers().get(0));
		List<UserReward> userRewards = trackerService.getUserRewards(trackerService.getAllUsers().get(0));
		trackerService.tracker.stopTracking();

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
	
}
