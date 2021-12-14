package com.aportefacil.backend.controllers;

import com.aportefacil.backend.controllers.dto.Login;
import com.aportefacil.backend.model.User;
import com.aportefacil.backend.services.LoginService;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final Map<String, LoginService> services = new HashMap<>();
    private final String loggedField;

    public LoginController(ListableBeanFactory beanFactory,
                           @Value("${logged.field}") String loggedField) {

        // Load all possible login providers
        Collection<LoginService> interfaces = beanFactory.getBeansOfType(LoginService.class).values();
        interfaces.forEach(bean -> services.put(bean.getProvider(), bean));

        this.loggedField = loggedField;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(HttpSession session, @RequestBody Login login) {

        User loggedUser;
        LoginService loginService = this.services.get(login.getProvider());

        try {

            if (loginService == null)
                throw new Exception("Login provider not supported: " + login.getProvider());

            loggedUser = loginService.login(login.getToken());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // Set to session
        session.setAttribute(loggedField, loggedUser.getToken());

        return ResponseEntity.ok(loggedUser.getFirstName());
    }
}