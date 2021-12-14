package com.aportefacil.backend.services.impl;

import com.aportefacil.backend.model.User;
import com.aportefacil.backend.services.LoginService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookLoginService implements LoginService {

    private static final String FB_USER_DATA_URL = "https://graph.facebook.com/me?fields=email,name&access_token=";
    private final RestTemplate restTemplate;

    public FacebookLoginService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public User login(String token) {

        try {
            UserData userData = this.restTemplate.getForObject(FB_USER_DATA_URL + token, UserData.class);

            if (userData == null)
                throw new Exception("Facebook userData returned null");

            return new User(userData.getEmail(), userData.getName());

        } catch (Exception e) {
            throw new RuntimeException("Fail to verify token: " + e.getMessage());
        }
    }

    @Override
    public String getProvider() {
        return "FACEBOOK";
    }
}

class UserData {

    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}


