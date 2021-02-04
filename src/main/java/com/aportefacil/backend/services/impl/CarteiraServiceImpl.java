package com.aportefacil.backend.services.impl;

import com.aportefacil.backend.model.Carteira;
import com.aportefacil.backend.repository.CarteiraRepository;
import com.aportefacil.backend.repository.CotacaoRepository;
import com.aportefacil.backend.repository.TaxaTesouroRepository;
import com.aportefacil.backend.services.CarteiraService;
import org.springframework.stereotype.Service;

@Service
public class CarteiraServiceImpl implements CarteiraService {

    private final CarteiraRepository carteiraRepository;
    private final CotacaoRepository cotacaoRepository;
    private final TaxaTesouroRepository taxaTesouroRepository;

    public CarteiraServiceImpl(CarteiraRepository carteiraRepository,
                               CotacaoRepository cotacaoRepository,
                               TaxaTesouroRepository taxaTesouroRepository) {
        this.carteiraRepository = carteiraRepository;
        this.cotacaoRepository = cotacaoRepository;
        this.taxaTesouroRepository = taxaTesouroRepository;
    }

    @Override
    public Carteira getCarteira(String id) {

        Carteira carteira = this.carteiraRepository.getCarteira(id).orElse(new Carteira());

        // Balanceia pesos e cotações
        this.balance(carteira);

        return carteira;
    }

    @Override
    public Carteira updateCarteira(String id, Carteira carteira) {

        if (!carteira.isValid())
            throw new RuntimeException("Carteira has invalid information");

        // Balanceia pesos e cotações
        this.balance(carteira);

        this.carteiraRepository.updateCarteira(id, carteira);

        return carteira;
    }

    private void balance(Carteira carteira) {

        // Atualizar cotações da carteira
        carteira.getAtivos().forEach(a -> a.setInfoAtivo(this.cotacaoRepository.getInfoAtivo(a.getTicker())));

        // Carrega taxa tesouro IPCA no retorno da carteira
        carteira.setTaxaIpca(this.taxaTesouroRepository.getTaxaIpca());

        // Balanceia os ativos com base no saldo disponivel
        carteira.balance();
    }
}