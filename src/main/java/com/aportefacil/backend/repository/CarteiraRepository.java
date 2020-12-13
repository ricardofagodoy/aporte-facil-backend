package com.aportefacil.backend.repository;

import com.aportefacil.backend.model.Carteira;
import java.util.Optional;

public interface CarteiraRepository {
    Optional<Carteira> getCarteira(String id);
    void updateCarteira(String id, Carteira carteira);
}