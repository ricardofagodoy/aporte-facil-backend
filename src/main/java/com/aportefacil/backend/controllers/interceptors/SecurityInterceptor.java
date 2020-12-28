package com.aportefacil.backend.controllers.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    private final String loggedField;
    private final Set<String> METHODS = new HashSet<>(Arrays.asList("POST", "GET", "PATCH", "DELETE"));

    public SecurityInterceptor(@Value("${logged.field}") String loggedField) {
        this.loggedField = loggedField;
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws IOException {

        HttpSession session = request.getSession(false);

        // Not logged In
        if (METHODS.contains(request.getMethod()) && (session == null || session.getAttribute(loggedField) == null)) {
            response.getWriter().write("Not authorized. Call /login");
            response.setStatus(403);

            return false;
        }

       return true;
    }
}
