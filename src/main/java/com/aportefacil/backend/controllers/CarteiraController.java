package com.aportefacil.backend.controllers;

import com.aportefacil.backend.controllers.dto.ErrorResponse;
import com.aportefacil.backend.model.Carteira;
import com.aportefacil.backend.services.CarteiraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
public class CarteiraController {

    private final Logger logger = LoggerFactory.getLogger(CarteiraController.class);

    private final CarteiraService carteiraService;
    private final String loggedField;

    public CarteiraController(CarteiraService carteiraService,
                              @Value("${logged.field}") String loggedField) {
        this.carteiraService = carteiraService;
        this.loggedField = loggedField;
    }

    @RequestMapping("/carteira")
    public ResponseEntity<Object> getCarteira(HttpSession session) {

        String id = (String) session.getAttribute(loggedField);

        logger.info("Retrieving carteira with ID " + id);

        return ResponseEntity.ok(this.carteiraService.getCarteira(id));
    }

    @RequestMapping(value = "/carteira", method = RequestMethod.PATCH)
    public ResponseEntity<Object> setCarteira(HttpSession session, @RequestBody Carteira carteira) {

        String id = (String) session.getAttribute(loggedField);

        logger.info("Updating carteira with ID " + id);

        Carteira carteiraBalanceada;

        try {
            carteiraBalanceada = this.carteiraService.updateCarteira(id, carteira);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

        return ResponseEntity.ok(carteiraBalanceada);
    }
}