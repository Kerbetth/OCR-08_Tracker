package tourGuideTracker.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuideTracker.clients.dto.UserService.UserBean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Repository
public class ServiceUserProxy {

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public UserBean getUser(String userName) {
        HttpGet request = new HttpGet("https://localhost:8081/getUser?userName=" + userName);
        request.addHeader("MS", "Tracker");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            UserBean userBean = objectMapper.readValue(response.toString(), UserBean.class);
            return userBean;
        }
        catch (Exception e) {
            log.error("cannot send the get htttp request");
        }
        return null;
    }

    public List<UserBean> getAllUsers() {
        HttpGet request = new HttpGet("https://localhost:8081/getAllUsers");
        request.addHeader("MS", "Tracker");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<UserBean> userBeans = objectMapper.readValue(response.toString(), ArrayList.class);
            return userBeans;
        }
        catch (Exception e) {
            log.error("cannot send the get htttp request");
        }
        return null;
    }
}