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
    private InfoAtivo infoAtivo;
    private Double desbalanco;
    private Integer acao;
    private boolean quarentena;

    public Ativo() {}

    public Ativo(String ticker, Integer quantidade, Double peso, InfoAtivo infoAtivo,
                 Double desbalanco, Integer acao, boolean quarentena) {
        this.ticker = ticker;
        this.quantidade = quantidade;
        this.peso = peso;
        this.infoAtivo = infoAtivo;
        this.desbalanco = desbalanco;
        this.acao = acao;
        this.quarentena = quarentena;
    }

    public Ativo(String ticker, Integer quantidade, Double peso, InfoAtivo infoAtivo) {
        this(ticker, quantidade, peso, infoAtivo, null, null, false);
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

    public void setInfoAtivo(InfoAtivo infoAtivo) {
        this.infoAtivo = infoAtivo;
    }

    public void setAcao(Integer acao) {
        this.acao = acao;
    }

    public void setDesbalanco(Double desbalanco) {
        this.desbalanco = desbalanco;
    }

    @Exclude
    public InfoAtivo getInfoAtivo() {
        return infoAtivo;
    }

    @Exclude
    public Integer getAcao() {
        return acao;
    }

    @Exclude
    public Double getDesbalanco() {
        return desbalanco;
    }

    public boolean isQuarentena() {
        return quarentena;
    }

    public void setQuarentena(boolean quarentena) {
        this.quarentena = quarentena;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ativo ativo = (Ativo) o;
        return Objects.equals(ticker, ativo.ticker) && Objects.equals(quantidade, ativo.quantidade)
                && Objects.equals(peso, ativo.peso) && Objects.equals(infoAtivo, ativo.infoAtivo) &&
                Objects.equals(desbalanco, ativo.desbalanco) && Objects.equals(acao, ativo.acao)
                && Objects.equals(quarentena, ativo.quantidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, quantidade, peso, infoAtivo, desbalanco, acao, quarentena);
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