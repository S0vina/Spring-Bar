package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.dto.ConfiguracaoRequestDTO;
import com.spring.bar.springBar.service.ConfiguracaoService;
import com.spring.bar.springBar.entity.Configuracao;
import jakarta.validation.Valid; // Import para validação
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*")
public class ConfiguracaoController {

    private final ConfiguracaoService configuracaoService;

    public ConfiguracaoController(ConfiguracaoService configuracaoService) {
        this.configuracaoService = configuracaoService;
    }

    // [ADMIN] Visualiza as configurações atuais.
    @GetMapping
    // IDEAL: Retornar ResponseEntity<ConfiguracaoResponseDTO>
    public ResponseEntity<Configuracao> buscarConfiguracao() {
        Configuracao config = configuracaoService.buscarConfiguracaoAtual();
        return ResponseEntity.ok(config);
    }

    // [ADMIN] Atualiza as configurações.
    @PutMapping // CORREÇÃO: Usar PUT para atualização
    public ResponseEntity<Configuracao> atualizarConfiguracao(@Valid @RequestBody ConfiguracaoRequestDTO dto) {
        Configuracao configAtualizada = configuracaoService.atualizarConfiguracao(dto);
        // O método com @PostMapping e o que recebia a Entidade foram REMOVIDOS
        return ResponseEntity.ok(configAtualizada);
    }
}