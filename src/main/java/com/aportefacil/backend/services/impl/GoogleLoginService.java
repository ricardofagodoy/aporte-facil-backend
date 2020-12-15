package com.aportefacil.backend.services.impl;

import com.aportefacil.backend.model.User;
import com.aportefacil.backend.services.LoginService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class GoogleLoginService implements LoginService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleLoginService(@Value("${google.client_id}") String clientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Override
    public User login(String token) {

        try {
            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null)
                throw new Exception("Invalid ID token");

                Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                String name = (String) payload.get("name");

                return new User(userId, name.split(" ")[0]);

        } catch (Exception e) {
            throw new RuntimeException("Fail to verify token");
        }
    }
}
