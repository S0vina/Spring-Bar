package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.service.MesaService;
import com.spring.bar.springBar.entity.Mesa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciar o cadastro de Mesas (Funções do Administrador).
 */
@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    /**
     * [ADMIN] Listar todas as mesas.
     * Endpoint: GET /api/mesas
     */
    @GetMapping
    public ResponseEntity<List<Mesa>> listarMesas() {
        List<Mesa> mesas = mesaService.listarTodas();
        return ResponseEntity.ok(mesas); // Retorna 200 OK
    }

    /**
     * [ADMIN] Cadastrar nova mesa.
     * Endpoint: POST /api/mesas
     */
    @PostMapping
    public ResponseEntity<Mesa> cadastrarMesa(@RequestBody Mesa novaMesa) {
        Mesa mesa = mesaService.cadastrarMesa(novaMesa);
        // Retorna 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(mesa);
    }

    /**
     * [ADMIN] Editar mesa existente.
     * Endpoint: PUT /api/mesas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mesa> editarMesa(@PathVariable long id, @RequestBody Mesa mesaAtualizada) {
        Mesa mesaEditada = mesaService.editarMesa(id, mesaAtualizada);
        // Retorna 200 OK
        return ResponseEntity.ok(mesaEditada);
    }

    // Você pode adicionar um DELETE /api/mesas/{id} para remover mesas se necessário.
}