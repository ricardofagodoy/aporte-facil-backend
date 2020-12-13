package com.aportefacil.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore getFirestoreClient() throws IOException {

        FirestoreOptions firestoreOptions =
                FirestoreOptions.getDefaultInstance().toBuilder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();

        return firestoreOptions.getService();
    }
}
