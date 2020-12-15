package com.aportefacil.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Ativo {

    private String ticker;
    private Integer quantidade;
    private Double peso;
    private Double cotacao;
    private Integer acao;

    public String getTicker() {
        return ticker;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getPeso() {
        return peso;
    }

    @Exclude
    public Double getCotacao() {
        return cotacao;
    }

    @Exclude
    public Integer getAcao() {
        return acao;
    }

    public void setCotacao(Double cotacao) {
        this.cotacao = cotacao;
    }

    public void setAcao(Integer acao) {
        this.acao = acao;
    }

    @Exclude
    @JsonIgnore
    public boolean isValid() {
        return !ticker.isBlank()
                && quantidade >= 0
                && peso >= 0;
    }
}