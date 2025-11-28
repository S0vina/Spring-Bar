package com.spring.bar.springBar.service;

import com.spring.bar.springBar.entity.Mesa;
import com.spring.bar.springBar.entity.Mesa.StatusMesa;
import com.spring.bar.springBar.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    /**
     * Validação básica para o número da Mesa.
     */
    private void validarNumeroMesa(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("O número da mesa deve ser positivo.");
        }
    }

    /**
     * [ADMIN] Cadastrar nova mesa.
     */
    @Transactional
    public Mesa cadastrarMesa(Mesa novaMesa) {
        validarNumeroMesa(novaMesa.getNumero());

        // Regra de Negócio: Garante que o número da mesa é único
        Optional<Mesa> mesaExistente = mesaRepository.findByNumero(novaMesa.getNumero());
        if (mesaExistente.isPresent()) {
            throw new IllegalStateException("O número da mesa " + novaMesa.getNumero() + " já está cadastrado.");
        }

        // Garante que o status inicial é LIVRE
        novaMesa.setStatus(StatusMesa.LIVRE);
        novaMesa.setNumPessoas(0);
        novaMesa.setCouverHabilitado(true); // Default

        return mesaRepository.save(novaMesa);
    }

    /**
     * [ADMIN] Editar mesa existente (apenas número e status - se fechada).
     */
    @Transactional
    public Mesa editarMesa(long id, Mesa mesaAtualizada) {
        Mesa mesaExistente = mesaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mesa ID " + id + " não encontrada para edição."));

        validarNumeroMesa(mesaAtualizada.getNumero());

        // Se o número da mesa foi alterado, verifica a unicidade
        if (mesaExistente.getNumero() != mesaAtualizada.getNumero()) {
            Optional<Mesa> mesaConflito = mesaRepository.findByNumero(mesaAtualizada.getNumero());
            if (mesaConflito.isPresent() && mesaConflito.get().getId() != id) {
                throw new IllegalStateException("O número da mesa " + mesaAtualizada.getNumero() + " já está em uso por outra mesa.");
            }
            mesaExistente.setNumero(mesaAtualizada.getNumero());
        }

        // Nota: Outras alterações de status (ABERTA/FECHADA) devem ser feitas
        // pelos métodos da ContaService (abrirConta/fecharConta) para garantir as regras de negócio.

        return mesaRepository.save(mesaExistente);
    }

    /**
     * [ADMIN] Listar todas as mesas.
     */
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }
}