package com.buehler.server.gameservice.service;

import com.buehler.server.gameservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public User getUserById(Long userId) {
        String url = userServiceUrl + "/" + userId;
        return restTemplate.getForObject(url, User.class);
    }

    public void deductPoints(Long userId, int points) {
        String url = userServiceUrl + "/" + userId + "/deduct?points=" + points;
        restTemplate.postForLocation(url, null);
    }
}
