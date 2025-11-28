package com.spring.bar.springBar.service;

import com.spring.bar.springBar.entity.Configuracao; // Assumimos que esta Entidade existe
import com.spring.bar.springBar.repository.ConfiguracaoRepository; // Assumimos que este Repositório existe
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    // Assumimos que haverá APENAS UM registro de configuração (ID fixo: 1)
    private static final long CONFIG_ID = 1L;

    /**
     * [ADMIN] Obtém as configurações atuais (percentuais de gorjeta, preço de entrada).
     */
    public Configuracao getConfiguracaoAtual() {
        // Busca a única linha de configuração no banco. Se não existir, cria uma padrão.
        return configuracaoRepository.findById(CONFIG_ID)
                .orElseGet(() -> {
                    // Cria e salva uma configuração padrão se não existir.
                    Configuracao defaultConfig = new Configuracao();
                    defaultConfig.setId(CONFIG_ID);
                    defaultConfig.setPrecoCouvert(10.00);
                    defaultConfig.setPercGorjetaComida(0.15);
                    defaultConfig.setPercGorjetaBebida(0.10);
                    return configuracaoRepository.save(defaultConfig);
                });
    }

    /**
     * [ADMIN] Atualiza as configurações do sistema.
     * [cite_start]Requisito: Definir preço de entrada (couvert) e percentual de gorjeta. [cite: 37, 38]
     */
    @Transactional
    public Configuracao atualizarConfiguracao(Configuracao configAtualizada) {

        // Validação básica
        if (configAtualizada.getPrecoCouvert() < 0 || configAtualizada.getPercGorjetaComida() < 0 || configAtualizada.getPercGorjetaBebida() < 0) {
            throw new IllegalArgumentException("Nenhum preço ou percentual pode ser negativo.");
        }

        Configuracao config = getConfiguracaoAtual();

        // Atualiza os campos
        config.setPrecoCouvert(configAtualizada.getPrecoCouvert());
        config.setPercGorjetaComida(configAtualizada.getPercGorjetaComida());
        config.setPercGorjetaBebida(configAtualizada.getPercGorjetaBebida());

        return configuracaoRepository.save(config);
    }
}