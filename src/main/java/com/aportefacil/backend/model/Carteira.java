package com.aportefacil.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Carteira {

    private Double saldo;
    private final List<Ativo> ativos;

    public Carteira() {
        this.ativos = new ArrayList<>();
    }

    public Carteira(Double saldo, List<Ativo> ativos) {
        this.saldo = saldo;
        this.ativos = ativos;
    }

    public Double getSaldo() {
        return saldo;
    }

    public List<Ativo> getAtivos() {
        return ativos;
    }

    @JsonIgnore
    public boolean isValid() {
        return saldo >= 0 && ativos.stream().allMatch(Ativo::isValid);
    }
}