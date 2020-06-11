package tourGuideTracker.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuideTracker.clients.dto.UserService.UserBean;

import java.util.UUID;

@Repository
public interface ServiceRewardsProxy {

    @GetMapping(value = "/Rewards/{userId}")
    Integer getRewards(@PathVariable("userId") UUID userId, @RequestParam UUID attractionID);

    void calculateRewards(UserBean user);
}