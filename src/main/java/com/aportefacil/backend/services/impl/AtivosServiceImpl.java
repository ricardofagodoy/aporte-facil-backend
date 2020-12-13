package com.aportefacil.backend.services.impl;

import com.aportefacil.backend.repository.CotacaoRepository;
import com.aportefacil.backend.services.AtivosService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtivosServiceImpl implements AtivosService {

    private final CotacaoRepository cotacaoRepository;

    public AtivosServiceImpl(CotacaoRepository cotacaoRepository) {
        this.cotacaoRepository = cotacaoRepository;
    }

    @Override
    public List<String> ativosDisponiveisOrdenados() {
        return this.cotacaoRepository.getAvailableTickers().stream().sorted().collect(Collectors.toList());
    }
}