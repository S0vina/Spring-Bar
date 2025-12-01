package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.service.MesaService;
import com.spring.bar.springBar.entity.Mesa;
import com.spring.bar.springBar.dto.MesaRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciar o cadastro de Mesas (Funções do Administrador).
 * Garante o mapeamento correto de GET, POST, PUT e DELETE.
 */
@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    private final MesaService mesaService;

    // Injeção de dependência via construtor (preferível ao @Autowired no campo)
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
    public ResponseEntity<Mesa> cadastrarMesa(@RequestBody MesaRequestDTO novaMesaDTO) {
        // O @Valid garante que as regras do DTO (numero > 0, not null) sejam checadas antes de entrar no service
        Mesa mesa = mesaService.cadastrarMesa(novaMesaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mesa); // Retorna 201 Created
    }

    /**
     * [ADMIN] Editar mesa existente.
     * Endpoint: PUT /api/mesas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mesa> editarMesa(@PathVariable long id, @Valid @RequestBody MesaRequestDTO mesaAtualizadaDTO) {
        Mesa mesa = mesaService.editarMesa(id, mesaAtualizadaDTO);
        return ResponseEntity.ok(mesa); // Retorna 200 OK
    }

    /**
     * [ADMIN] Excluir mesa.
     * Endpoint: DELETE /api/mesas/{id}
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> excluirMesa(@PathVariable long id) {
        mesaService.excluirMesa(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}