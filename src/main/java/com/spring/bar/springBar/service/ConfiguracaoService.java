package com.spring.bar.springBar.service;

import com.spring.bar.springBar.dto.ConfiguracaoRequestDTO;
import com.spring.bar.springBar.entity.Configuracao; // Assumimos que esta Entidade existe
import com.spring.bar.springBar.repository.ConfiguracaoRepository; // Assumimos que este Repositório existe
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ConfiguracaoService {

    private final ConfiguracaoRepository configuracaoRepository;

    // Assumimos que haverá APENAS UM registro de configuração (ID fixo: 1)
    private final long CONFIG_ID = 1L;

    public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository) {
        this.configuracaoRepository = configuracaoRepository;
    }

    public Configuracao buscarConfiguracaoAtual() {
        return configuracaoRepository.findById(1L)
                .orElseGet(() -> {
                    Configuracao novaConfig = new Configuracao();
                    return configuracaoRepository.save(novaConfig);
                });
    }

    /**
     * [ADMIN] Atualiza as configurações do sistema.
     * [cite_start]Requisito: Definir preço de entrada (couvert) e percentual de gorjeta. [cite: 37, 38]
     */
    @Transactional
    public Configuracao atualizarConfiguracao(ConfiguracaoRequestDTO configAtualizada) {

        // Validação básica
        if (configAtualizada.getPrecocCouvert() < 0 || configAtualizada.getPercentualGorjetacomidas() < 0 || configAtualizada.getPercentualGorjetaBebidas() < 0) {
            throw new IllegalArgumentException("Nenhum preço ou percentual pode ser negativo.");
        }

        Configuracao config = buscarConfiguracaoAtual();

        // Atualiza os campos
        config.setPrecoCouvert(configAtualizada.getPrecocCouvert());
        config.setPercGorjetaComida(configAtualizada.getPercentualGorjetacomidas());
        config.setPercGorjetaBebida(configAtualizada.getPercentualGorjetaBebidas());

        return configuracaoRepository.save(config);
    }
}