package com.aportefacil.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import java.util.Objects;

@IgnoreExtraProperties
public class Ativo {

    private String ticker;
    private Integer quantidade;
    private Double peso;
    private Double cotacao;
    private Double desbalanco;
    private Integer acao;

    public Ativo() {}

    public Ativo(String ticker, Integer quantidade, Double peso, Double cotacao,
                 Double desbalanco, Integer acao) {
        this.ticker = ticker;
        this.quantidade = quantidade;
        this.peso = peso;
        this.cotacao = cotacao;
        this.desbalanco = desbalanco;
        this.acao = acao;
    }

    public Ativo(String ticker, Integer quantidade, Double peso, Double cotacao) {
        this(ticker, quantidade, peso, cotacao, 0d, 0);
    }

    public String getTicker() {
        return ticker;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getPeso() {
        return peso;
    }

    public void setCotacao(Double cotacao) {
        this.cotacao = cotacao;
    }

    public void setAcao(Integer acao) {
        this.acao = acao;
    }

    public void setDesbalanco(Double desbalanco) {
        this.desbalanco = desbalanco;
    }

    @Exclude
    public Double getCotacao() {
        return cotacao;
    }

    @Exclude
    public Integer getAcao() {
        return acao;
    }

    @Exclude
    public Double getDesbalanco() {
        return desbalanco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ativo ativo = (Ativo) o;
        return ticker.equals(ativo.ticker)
                && quantidade.equals(ativo.quantidade)
                && peso.equals(ativo.peso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, quantidade, peso);
    }

    @Exclude
    @JsonIgnore
    public boolean isValid() {
        return !ticker.isBlank()
                && quantidade >= 0
                && peso >= 0;
    }

    public void merge(Ativo a) {
        if (a != null) {
            this.quantidade += a.getQuantidade();
            this.peso = a.getPeso();
        }
    }
}