package com.aportefacil.backend.services;

import com.aportefacil.backend.model.Carteira;

public interface CarteiraService {
    Carteira getCarteira(String id);
    Carteira updateCarteira(String id, Carteira carteira);
}