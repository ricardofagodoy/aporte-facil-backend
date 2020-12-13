package com.aportefacil.backend.controllers;

import com.aportefacil.backend.services.AtivosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AtivosController {

    private final AtivosService ativosService;

    public AtivosController(AtivosService ativosService) {
        this.ativosService = ativosService;
    }

    @RequestMapping("ativos")
    public ResponseEntity<Object> getAtivos() {
        return ResponseEntity.ok(this.ativosService.ativosDisponiveisOrdenados());
    }
}