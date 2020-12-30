package com.aportefacil.backend.repository;

import com.aportefacil.backend.model.InfoAtivo;

import java.util.Set;

public interface CotacaoRepository {
    InfoAtivo getInfoAtivo(String ticker);
    Set<String> getAvailableTickers();
}