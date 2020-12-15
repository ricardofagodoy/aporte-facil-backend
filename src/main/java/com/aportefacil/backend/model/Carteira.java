package com.aportefacil.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Carteira {

    private Double saldo;
    private List<Ativo> ativos;

    public Carteira() {
        this.saldo = 0.0;
        this.ativos = new ArrayList<>();
    }

    public Carteira(String nome, Double saldo, List<Ativo> ativos) {
        this.saldo = saldo;
        this.ativos = ativos;
    }

    public Double getSaldo() {
        return saldo;
    }

    public List<Ativo> getAtivos() {
        return ativos;
    }

    @Exclude
    @JsonIgnore
    public boolean isValid() {
        return saldo >= 0 && ativos.stream().allMatch(Ativo::isValid);
    }

    public void balance() {

        // TODO: write the code
        ativos.forEach(a -> a.setAcao(4));
    }
}