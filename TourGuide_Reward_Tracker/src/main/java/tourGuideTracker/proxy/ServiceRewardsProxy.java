package tourGuideTracker.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuideTracker.bean.UserService.UserBean;

import java.util.UUID;

@FeignClient(name = "tourGuideReward", url = "localhost:8082")
public interface ServiceRewardsProxy {

    @GetMapping(value = "/Rewards/{userId}")
    Integer getRewards(@PathVariable("userId") UUID userId, @RequestParam UUID attractionID);

    void calculateRewards(UserBean user);
}