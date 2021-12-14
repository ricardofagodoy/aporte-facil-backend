package com.aportefacil.backend.services;

import com.aportefacil.backend.model.User;

public interface LoginService {
    String getProvider();
    User login(String token);
}
