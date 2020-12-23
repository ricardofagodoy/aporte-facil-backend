package com.aportefacil.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.floor;

@IgnoreExtraProperties
public class Carteira {

    private Double saldo;
    private List<Ativo> ativos;

    public Carteira() {
        this.saldo = 0.0;
        this.ativos = new ArrayList<>();
    }

    public Carteira(Double saldo, List<Ativo> ativos) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carteira carteira = (Carteira) o;
        return saldo.equals(carteira.saldo) && ativos.equals(carteira.ativos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saldo, ativos);
    }

    public void balance() {

        // Merge duplicates
        ativos = this.mergeDuplicates(ativos);

        // Tenta balancear até não ter mais negativos
        int quantosPositivos;
        List<Ativo> positiveActions = ativos;

        // Zera desbalanco
        ativos.forEach(a -> a.setDesbalanco(null));

        do {
            quantosPositivos = positiveActions.size();

            // Need to rebalance those with buy orders
            this.balance(positiveActions);

            positiveActions = positiveActions.stream()
                    .filter(a -> a.getAcao() > 0)
                    .collect(Collectors.toList());

        } while (quantosPositivos != positiveActions.size());

        // Saldo restante após compras
        Double saldoRestante = positiveActions.stream()
                .map(a -> a.getAcao() * a.getCotacao())
                .reduce(saldo, (restante, valor) -> restante - valor);

        // Sort all buy actions by how close it is to be purchased
        List<Ativo> positiveActionsSorted = positiveActions.stream()
                .sorted((a, b) -> Double.compare(b.getDesbalanco(), a.getDesbalanco()))
                .collect(Collectors.toList());

        // Usa saldo restante para comprar ativos possíveis
        for (Ativo a : positiveActionsSorted) {
            if (a.getCotacao() <= saldoRestante) {
                int quantosComprar = (int) (saldoRestante / a.getCotacao());
                a.setAcao(a.getAcao() + quantosComprar);
                //a.setDesbalanco(a.getDesbalanco() + a.getCotacao() * quantosComprar);

                saldoRestante -= a.getCotacao() * quantosComprar;
            }
        }

        // Return sorted by biggest value to buy
        this.ativos.sort((a, b) -> Double.compare(b.getAcao() * b.getCotacao(), a.getAcao() * a.getCotacao()));
    }

    private List<Ativo> mergeDuplicates(List<Ativo> ativos) {

        Stack<Ativo> ativosDedupicated = new Stack<>();

        ativos.stream()
                .sorted(Comparator.comparing(Ativo::getTicker))
                .forEach(a -> {

                    if (!ativosDedupicated.isEmpty()) {
                        Ativo peek = ativosDedupicated.peek();

                        if (peek.getTicker().equals(a.getTicker())) {
                            peek.merge(a);
                            return;
                        }
                    }

                    ativosDedupicated.push(a);
                });

        return ativosDedupicated;
    }

    private void balance(List<Ativo> ativos) {

        // Sum all pesos and values
        double somaPesos = 0;
        double somaValores = 0;

        for (Ativo a : ativos) {
            somaPesos += a.getPeso();
            somaValores += a.getQuantidade() * a.getCotacao();
        }

        // Balance all ativos
        for (Ativo a : ativos) {

            double valorIdeal = (somaValores + saldo) * (a.getPeso() / somaPesos);
            double valorAtual = a.getQuantidade() * a.getCotacao();
            double valorDesbalanco = valorIdeal - valorAtual;

            // Set desired values
            if (a.getDesbalanco() == null)
                a.setDesbalanco(valorDesbalanco);

            a.setAcao((int) Math.max((valorDesbalanco / a.getCotacao()), 0));
        }
    }
}