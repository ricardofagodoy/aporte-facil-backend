package com.aportefacil.backend.model;

public class InfoAtivo {

    private Double cotacao;
    private Double dy;
    private Double pvp;

    public InfoAtivo() {
    }

    public InfoAtivo(Double cotacao, Double dy, Double pvp) {
        this.cotacao = cotacao;
        this.dy = dy;
        this.pvp = pvp;
    }

    public Double getCotacao() {
        return cotacao;
    }

    public Double getDy() {
        return dy;
    }

    public Double getPvp() {
        return pvp;
    }
}