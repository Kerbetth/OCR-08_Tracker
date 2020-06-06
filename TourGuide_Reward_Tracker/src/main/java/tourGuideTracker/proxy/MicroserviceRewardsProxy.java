package tourGuideTracker.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "tourGuideUser", url = "localhost:8081")
public interface MicroserviceRewardsProxy {

    @GetMapping(value = "/Rewards/{userId}")
    Integer getRewards(@PathVariable("userId") UUID userId, @RequestParam UUID attractionID);

}