package tourGuideTracker.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tourGuideTracker.DataTest;
import tourGuideTracker.controller.TrackerController;
import tourGuideTracker.domain.UserReward;
import tourGuideTracker.domain.VisitedLocation;
import tourGuideTracker.domain.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class TrackerControllerTest {

    @MockBean
    private TrackerController trackerController;

    private DataTest dataTest = new DataTest();
    @Autowired
    protected MockMvc mockMvc;


    @Test
    public void getLocation() throws Exception {
        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(UUID.randomUUID());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.mockMvc.perform(post("/getLocation?userId=" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void get5NearestAttractions() throws Exception {
        Location location = new Location(33.817595D, -117.922008D);
        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(location);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.mockMvc.perform(post("/get5NearestAttractions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getCurrentLocationOfAllUsers() throws Exception {
        List<UUID> userIds = new ArrayList<>();
        userIds.add(UUID.randomUUID());
        userIds.add(UUID.randomUUID());
        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(userIds);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.mockMvc.perform(post("/getCurrentLocationOfAllUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getAllVisitedAttractions() throws Exception {
        List<VisitedLocation> userIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            userIds.add(new VisitedLocation(UUID.randomUUID(), new Location(33.817595D, -117.922008D), new Date()));
        }
        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(userIds);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.mockMvc.perform(post("/getAllVisitedAttractions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getNewVisitedAttraction() throws Exception {
        List<UserReward> userIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            userIds.add(
                    new UserReward(
                            new VisitedLocation(
                                    UUID.randomUUID(),
                                    new Location(33.817595D, -117.922008D),
                                    new Date()
                            ),
                            dataTest.getAttractionsForTest().get(0)));
        }

        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(userIds);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        this.mockMvc.perform(post("/getNewVisitedAttraction?latitude=" + 33.817595D + "&longitude=" + -117.922008D)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andExpect(status().isOk());
    }
}
