package com.aportefacil.backend.services;

import com.aportefacil.backend.model.User;

public interface LoginService {
    User login(String token);
}
