package com.aportefacil.backend.model;

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
}