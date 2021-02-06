package com.aportefacil.backend.repository.impl;

import com.aportefacil.backend.repository.FeedbackRepository;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;
import java.util.Collections;

@Repository
public class FeedbackFirestoreRepository implements FeedbackRepository {

    private final CollectionReference collection;

    public FeedbackFirestoreRepository(Firestore firestore) {
        this.collection = firestore.collection("feedbacks");
    }

    @Override
    public void addFeedback(String id, String feedback) {
        this.collection.add(Collections.singletonMap(id, feedback));
    }
}
