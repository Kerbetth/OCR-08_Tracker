package tourGuideTracker.bean.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Location;

/**
 * The old pojo "UserLocation" has been deleted and the "Location latLongUser;" value has been added in User Object
 */
public class UserBean {
    private final UUID userId;
    private Location latLongUser;
    private final String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private List<VisitedLocation> visitedLocations = new ArrayList<>();
    private List<UserRewardBean> userRewards = new ArrayList<>();
    private UserPreferencesBean userPreferences = new UserPreferencesBean();
    private List<ProviderBean> tripDeals = new ArrayList<>();

    public UserBean(UUID userId, String userName, String phoneNumber, String emailAddress) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Location getLatLongUser() {
        return latLongUser;
    }

    public void setLatLongUser(Location latLongUser) {
        this.latLongUser = latLongUser;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
        this.latestLocationTimestamp = latestLocationTimestamp;
    }

    public Date getLatestLocationTimestamp() {
        return latestLocationTimestamp;
    }

    public void addToVisitedLocations(VisitedLocation visitedLocation) {
        visitedLocations.add(visitedLocation);
    }

    public List<VisitedLocation> getVisitedLocations() {
        return visitedLocations;
    }

    public void clearVisitedLocations() {
        visitedLocations.clear();
    }

    public void addUserReward(UserRewardBean userReward) {
        userRewards.add(userReward);
    }

    public List<UserRewardBean> getUserRewards() {
        return userRewards;
    }

    public UserPreferencesBean getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferencesBean userPreferences) {
        this.userPreferences = userPreferences;
    }

    public VisitedLocation getLastVisitedLocation() {
        return visitedLocations.get(visitedLocations.size() - 1);
    }

    public void setTripDeals(List<ProviderBean> tripDeals) {
        this.tripDeals = tripDeals;
    }

    public List<ProviderBean> getTripDeals() {
        return tripDeals;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", latestLocationTimestamp=" + latestLocationTimestamp +
                ", visitedLocations=" + visitedLocations +
                ", userRewards=" + userRewards +
                ", userPreferences=" + userPreferences +
                ", tripDeals=" + tripDeals +
                '}';
    }
}