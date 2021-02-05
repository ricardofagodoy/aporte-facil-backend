package com.aportefacil.backend.model;

public class InfoAtivo {

    private TipoAtivo tipo;
    private Double cotacao;
    private Double dy;
    private Double pl;
    private Double pvp;

    public InfoAtivo() {
    }

    public InfoAtivo(Double cotacao, TipoAtivo tipo, Double dy, Double pl, Double pvp) {
        this.cotacao = cotacao;
        this.tipo = tipo;
        this.dy = dy;
        this.pl = pl;
        this.pvp = pvp;
    }

    public Double getCotacao() {
        return cotacao;
    }

    public TipoAtivo getTipo() {
        return tipo;
    }

    public Double getDy() {
        return dy;
    }

    public Double getPl() {
        return pl;
    }

    public Double getPvp() {
        return pvp;
    }
}