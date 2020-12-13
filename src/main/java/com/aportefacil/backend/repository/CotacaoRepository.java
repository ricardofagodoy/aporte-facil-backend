package com.aportefacil.backend.repository;

import java.util.Set;

public interface CotacaoRepository {
    Double getCotacao(String ticker);
    Set<String> getAvailableTickers();
}