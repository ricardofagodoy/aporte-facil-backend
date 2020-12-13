package com.aportefacil.backend.model;

import java.util.ArrayList;
import java.util.List;

public class Carteira {

    private Double saldo;
    private final List<Ativo> ativos;

    public Carteira() {
        this.ativos = new ArrayList<>();
    }

    public Double getSaldo() {
        return saldo;
    }

    public List<Ativo> getAtivos() {
        return ativos;
    }
}