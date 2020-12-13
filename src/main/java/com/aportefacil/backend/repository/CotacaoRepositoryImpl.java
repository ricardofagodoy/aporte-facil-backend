package com.aportefacil.backend.repository;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CotacaoRepositoryImpl implements CotacaoRepository {

    private final Map<String, Double> cotacoes = new HashMap<>() {{
        put("KNRI11", 115.99);
        put("HGLG11", 445.88);
    }};

    @Override
    public Double getCotacao(String ticker) {
        return this.cotacoes.getOrDefault(ticker, 0.0);
    }
}