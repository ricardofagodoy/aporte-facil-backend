package com.aportefacil.backend.controllers;

import com.aportefacil.backend.controllers.dto.Login;
import com.aportefacil.backend.model.User;
import com.aportefacil.backend.services.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    private final LoginService loginService;
    private final String loggedField;

    public LoginController(LoginService loginService,
                           @Value("${logged.field}") String loggedField) {
        this.loginService = loginService;
        this.loggedField = loggedField;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(HttpSession session, @RequestBody Login login) {

        User loggedUser;

        try {
            loggedUser = loginService.login(login.getToken());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // Set to session
        session.setAttribute(loggedField, loggedUser.getToken());

        return ResponseEntity.ok(loggedUser.getFirstName());
    }
}