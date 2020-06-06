package tourGuideTracker.proxy;

import gpsUtil.location.Attraction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuideTracker.bean.RewardBean;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "microservice-produits", url = "localhost:9001")
public interface MicroserviceRewardsProxy {

    @GetMapping(value = "/Rewards/{userId}")
    Integer getRewards(@PathVariable("userId") UUID userId, @RequestParam UUID attractionID);

}