package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.service.MesaService;
import com.spring.bar.springBar.entity.Mesa;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spring.bar.springBar.dto.MesaRequestDTO;

import java.util.List;

/**
 * Controlador REST para gerenciar o cadastro de Mesas (Funções do Administrador).
 */
@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

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
    public ResponseEntity<Mesa> cadastrarMesa(@Valid @RequestBody MesaRequestDTO novaMesaDTO) { // Mude o tipo para DTO
        Mesa mesa = mesaService.cadastrarMesa(novaMesaDTO); // Mudar a assinatura do Service para receber DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(mesa);
    }

    /**
     * [ADMIN] Editar mesa existente.
     * Endpoint: PUT /api/mesas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mesa> editarMesa(@PathVariable long id, @Valid @RequestBody MesaRequestDTO mesaAtualizadaDTO) { // Mude o tipo para DTO
        Mesa mesa = mesaService.editarMesa(id, mesaAtualizadaDTO); // Mudar a assinatura do Service
        return ResponseEntity.ok(mesa);
    }

    // Você pode adicionar um DELETE /api/mesas/{id} para remover mesas se necessário.
}