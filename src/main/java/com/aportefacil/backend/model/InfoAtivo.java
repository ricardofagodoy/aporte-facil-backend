package com.aportefacil.backend.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoAtivo infoAtivo = (InfoAtivo) o;
        return tipo == infoAtivo.tipo &&
                Objects.equals(cotacao, infoAtivo.cotacao) &&
                Objects.equals(pvp, infoAtivo.pvp) &&
                Objects.equals(dy, infoAtivo.dy) &&
                Objects.equals(pl, infoAtivo.pl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, cotacao, pvp, dy, pl);
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