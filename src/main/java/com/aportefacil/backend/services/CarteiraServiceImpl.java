package com.aportefacil.backend.services;

import com.aportefacil.backend.model.Carteira;
import com.aportefacil.backend.repository.CarteiraRepository;
import com.aportefacil.backend.repository.CotacaoRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CarteiraServiceImpl implements CarteiraService {

    private final CarteiraRepository carteiraRepository;
    private final CotacaoRepository cotacaoRepository;

    public CarteiraServiceImpl(CarteiraRepository carteiraRepository,
                               CotacaoRepository cotacaoRepository) {
        this.carteiraRepository = carteiraRepository;
        this.cotacaoRepository = cotacaoRepository;
    }

    @Override
    public Optional<Carteira> getCarteira(String id) {

        Optional<Carteira> carteira = this.carteiraRepository.getCarteira(id);

        // Atualizar cotações da carteira
        carteira.ifPresent(
                c -> c.getAtivos().forEach(
                        a -> a.setCotacao(this.cotacaoRepository.getCotacao(a.getTicker()))
                ));

        // TODO: Calcula os pesos e ações a serem tomadas
        // ...

        return carteira;
    }

    @Override
    public void updateCarteira(String id, Carteira carteira) {
        this.carteiraRepository.updateCarteira(id, carteira);
    }
}