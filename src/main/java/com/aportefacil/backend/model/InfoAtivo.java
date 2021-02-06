package com.aportefacil.backend.model;

public class InfoAtivo {

    private TipoAtivo tipo;
    private Double cotacao;
    private Double pvp;
    private Double dy;
    private Double pl;

    public InfoAtivo() {
    }

    public InfoAtivo(Double cotacao, TipoAtivo tipo, Double pvp, Double dy, Double pl) {
        this.cotacao = cotacao;
        this.tipo = tipo;
        this.pvp = pvp;
        this.dy = dy;
        this.pl = pl;
    }

    public Double getCotacao() {
        return cotacao;
    }

    public TipoAtivo getTipo() {
        return tipo;
    }

    public Double getPvp() {
        return pvp;
    }

    public Double getDy() {
        return dy;
    }

    public Double getPl() {
        return pl;
    }
}