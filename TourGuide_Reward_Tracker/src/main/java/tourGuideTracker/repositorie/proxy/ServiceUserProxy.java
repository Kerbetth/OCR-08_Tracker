package tourGuideTracker.repositorie.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuideTracker.bean.UserService.UserBean;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@FeignClient(name = "tourGuideUser", url = "localhost:8081")
public interface ServiceUserProxy {

    @GetMapping(value = "/User/{userId}")
    Integer getRewards(@PathVariable("userId") UUID userId, @RequestParam UUID attractionID);

    ArrayList<UserBean> getAllUsers();

    UserBean getUser(String userName);
}