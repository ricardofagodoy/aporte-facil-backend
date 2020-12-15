package com.aportefacil.backend.controllers.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    private final String loggedField;

    public SecurityInterceptor(@Value("${logged.field}") String loggedField) {
        this.loggedField = loggedField;
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws IOException {

        HttpSession session = request.getSession(false);

        // Not logged In
        if (session == null || session.getAttribute(loggedField) == null) {
            response.getWriter().write("Not authorized. Call /login");
            response.setStatus(403);

            return false;
        }

       return true;
    }
}
