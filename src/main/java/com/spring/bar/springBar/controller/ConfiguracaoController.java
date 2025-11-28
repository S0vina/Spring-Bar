package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.service.ConfiguracaoService;
import com.spring.bar.springBar.entity.Configuracao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gerenciar as Configurações Globais (Funções do Administrador).
 */
@RestController
@RequestMapping("/api/config")
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoService configuracaoService;

    /**
     * [ADMIN] Visualiza as configurações atuais.
     * Endpoint: GET /api/config
     */
    @GetMapping
    public ResponseEntity<Configuracao> getConfiguracao() {
        Configuracao config = configuracaoService.getConfiguracaoAtual();
        return ResponseEntity.ok(config); // Retorna 200 OK
    }

    /**
     * [ADMIN] Atualiza as configurações.
     * Endpoint: PUT /api/config
     */
    @PutMapping
    public ResponseEntity<Configuracao> atualizarConfiguracao(@RequestBody Configuracao configAtualizada) {
        Configuracao config = configuracaoService.atualizarConfiguracao(configAtualizada);
        return ResponseEntity.ok(config); // Retorna 200 OK com a configuração atualizada
    }
}