package com.aportefacil.backend.controllers;

import com.aportefacil.backend.services.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

@RestController
public class FeedbackController {

    private final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    private final FeedbackService feedbackService;
    private final String loggedField;

    public FeedbackController(FeedbackService feedbackService,
                              @Value("${logged.field}") String loggedField) {
        this.feedbackService = feedbackService;
        this.loggedField = loggedField;
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public ResponseEntity<Void> addFeedback(HttpSession session, @RequestBody String feedback) {

        String id = (String) session.getAttribute(loggedField);

        logger.info("Adding a new feedback with ID " + id);

        try {
            this.feedbackService.addFeedback(id, feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }
}