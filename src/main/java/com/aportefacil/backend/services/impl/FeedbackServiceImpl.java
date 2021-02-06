package com.aportefacil.backend.services.impl;

import com.aportefacil.backend.repository.FeedbackRepository;
import com.aportefacil.backend.services.FeedbackService;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public void addFeedback(String id, String feedback) {
        this.feedbackRepository.addFeedback(id, feedback);
    }
}
