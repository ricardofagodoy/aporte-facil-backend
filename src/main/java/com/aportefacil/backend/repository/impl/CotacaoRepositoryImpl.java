package com.aportefacil.backend.repository.impl;

import com.aportefacil.backend.repository.CotacaoRepository;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Set;

@Repository
public class CotacaoRepositoryImpl implements CotacaoRepository {

    private final Map<String, Double> cotacoes = Map.of(
        "KNRI11", 115.99,
        "HGLG11", 445.88
    );

    @Override
    public Double getCotacao(String ticker) {
        return this.cotacoes.getOrDefault(ticker, 0.0);
    }

    @Override
    public Set<String> getAvailableTickers() {
        return this.cotacoes.keySet();
    }
}