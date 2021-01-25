package com.aportefacil.backend.repository.impl;

import com.aportefacil.backend.model.Carteira;
import com.aportefacil.backend.repository.CarteiraRepository;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository("Persistent")
public class CarteiraFirestoreRepository implements CarteiraRepository {

    private final CollectionReference collection;

    public CarteiraFirestoreRepository(Firestore firestore) {
        this.collection = firestore.collection("carteiras");
    }

    @Override
    public Optional<Carteira> getCarteira(String id) {

        Optional<Carteira> carteira = Optional.empty();

        try {
            DocumentSnapshot document = this.fetchCarteira(id);

            if (document.exists())
                carteira = Optional.ofNullable(document.toObject(Carteira.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return carteira;
    }

    @Override
    public void updateCarteira(String id, Carteira carteira) {
        this.collection.document(id).set(carteira);
    }

    private DocumentSnapshot fetchCarteira(String id) throws Exception {
        return this.collection.document(id).get().get(5, TimeUnit.SECONDS);
    }
}