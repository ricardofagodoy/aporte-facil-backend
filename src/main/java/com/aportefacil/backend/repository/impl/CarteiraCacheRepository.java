package com.aportefacil.backend.repository.impl;

import com.aportefacil.backend.model.Carteira;
import com.aportefacil.backend.repository.CarteiraRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Primary
@Repository
public class CarteiraCacheRepository implements CarteiraRepository {

    // Local cache being persistent periodically
    private final Map<String, Carteira> read;
    private final Map<String, Carteira> write;
    private final CarteiraRepository persistentRepository;

    public CarteiraCacheRepository(@Qualifier("Persistent") CarteiraRepository persistentRepository) {
        this.persistentRepository = persistentRepository;

        // Caches
        this.read = Collections.synchronizedMap(new HashMap<>());
        this.write = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public Optional<Carteira> getCarteira(String id) {

        if (this.read.containsKey(id))
            return Optional.ofNullable(this.read.get(id));

        // Read from repository
        Optional<Carteira> carteira = this.persistentRepository.getCarteira(id);

        // Store to cache
        this.read.put(id, carteira.orElse(null));

        return carteira;
    }

    @Override
    public void updateCarteira(String id, Carteira carteira) {
        this.write.put(id, carteira);
        this.read.put(id, carteira);
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("Persisting before destroying application...");
        this.updateCarteiraRemote();
    }

    @Scheduled(fixedDelayString = "${updateCarteiraRemoteDelay}")
    private void updateCarteiraRemote() {

        System.out.println("Persisting carteira for " + this.write.size() + " records...");
        this.write.forEach(this.persistentRepository::updateCarteira);

        System.out.println("Clearing write buffer...");
        this.write.clear();
    }
}