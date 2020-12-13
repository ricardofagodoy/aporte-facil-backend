package com.aportefacil.backend.services;

import com.aportefacil.backend.model.Carteira;
import java.util.Optional;

public interface CarteiraService {
    Optional<Carteira> getCarteira(String id);
    void updateCarteira(String id, Carteira carteira);
}