package com.aportefacil.backend.repository;

import com.aportefacil.backend.model.Carteira;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Repository
public class CarteiraRepositoryImpl implements CarteiraRepository {

    private final CollectionReference collection;

    public CarteiraRepositoryImpl(Firestore firestore) {
        this.collection = firestore.collection("carteiras");
    }

    @Override
    public Optional<Carteira> getCarteira(String id) {

        Optional<Carteira> carteira = Optional.empty();

        try {
            DocumentSnapshot document = this.collection.document(id).get().get(5, TimeUnit.SECONDS);

            if (document.exists())
                carteira = Optional.ofNullable(document.toObject(Carteira.class));

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        return carteira;
    }

    @Override
    public void updateCarteira(String id, Carteira carteira) {

        try {
            WriteResult result = this.collection.document(id).set(carteira).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}