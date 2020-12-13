package com.aportefacil.backend.controllers;

import com.aportefacil.backend.controllers.dto.ErrorResponse;
import com.aportefacil.backend.model.Carteira;
import com.aportefacil.backend.services.CarteiraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
public class CarteiraController {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    @RequestMapping("carteira/{id}")
    public ResponseEntity<Object> getCarteira(@PathVariable("id") String id) {

        Optional<Carteira> carteira = this.carteiraService.getCarteira(id);

        if (carteira.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Carteira not found"));

        return ResponseEntity.ok(carteira.get());
    }

    @RequestMapping(value = "carteira/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> setCarteira(@PathVariable("id") String id, @RequestBody Carteira carteira) {

        try {
            this.carteiraService.updateCarteira(id, carteira);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}