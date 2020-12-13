package com.aportefacil.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Ativo {

    private String ticker;
    private Integer quantidade;
    private Double peso;
    private Double cotacao;

    public String getTicker() {
        return ticker;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getPeso() {
        return peso;
    }

    public Double getCotacao() {
        return cotacao;
    }

    public void setCotacao(Double cotacao) {
        this.cotacao = cotacao;
    }

    @JsonIgnore
    public boolean isValid() {
        return !ticker.isBlank()
                && quantidade >= 0
                && peso >= 0;
    }
}