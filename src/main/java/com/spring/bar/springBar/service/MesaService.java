package com.spring.bar.springBar.service;

import com.spring.bar.springBar.dto.MesaRequestDTO; // Importando o DTO
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
     * Converte MesaRequestDTO para a Entidade Mesa.
     * @param dto DTO com dados da nova mesa.
     * @return Entidade Mesa preenchida.
     */
    private Mesa converterDtoParaEntidade(MesaRequestDTO dto) {
        Mesa mesa = new Mesa();

        // Mapeamento dos campos que o DTO fornece
        mesa.setNumero(dto.getNumero()); // Corrigido erro de digitação (antes era 'ge()')
        mesa.setNumero(dto.getNumPessoas() != null ? dto.getNumPessoas() : 0);

        // Define o status inicial da ENTIDADE (não do DTO)
        mesa.setStatus(StatusMesa.LIVRE);

        return mesa;
    }


    /**
     * [ADMIN] Cadastrar nova mesa.
     * A entrada agora é o DTO, não a Entidade Mesa diretamente.
     */
    @Transactional
    public Mesa cadastrarMesa(MesaRequestDTO novaMesaDto) {

        // Converte o DTO para a Entidade
        Mesa novaMesa = converterDtoParaEntidade(novaMesaDto);

        // Usa a Entidade para as validações e persistência
        validarNumeroMesa(novaMesa.getNumero());

        // Regra de Negócio: Garante que o número da mesa é único
        Optional<Mesa> mesaExistente = mesaRepository.findByNumero(novaMesa.getNumero());
        if (mesaExistente.isPresent()) {
            throw new IllegalStateException("O número da mesa " + novaMesa.getNumero() + " já está em uso.");
        }

        // Configurações de inicialização da Entidade
        // novaMesa.setNumPessoas(0); // Já configurado no DTO/converter
        // novaMesa.setCouverHabilitado(true); // Já configurado no DTO/converter
        // novaMesa.setStatus(StatusMesa.LIVRE); // Já configurado no converter

        // Salva a ENTIDADE no repositório (CORRIGIDO o erro de save(DTO))
        return mesaRepository.save(novaMesa);
    }

    /**
     * [ADMIN] Listar todas as mesas.
     */
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }


    /**
     * [ADMIN] Editar mesa existente (apenas número e status - se fechada).
     */
    @Transactional
    public Mesa editarMesa(long id, MesaRequestDTO mesaAtualizada) {
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
     * [ADMIN] Excluir mesa.
     */
    @Transactional
    public void excluirMesa(long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mesa ID " + id + " não encontrada para exclusão."));

        // Regra de Negócio: Não pode excluir mesa com conta aberta
        if (mesa.getStatus() != StatusMesa.LIVRE) {
            throw new IllegalStateException("Não é possível excluir a mesa " + mesa.getNumero() + " pois ela não está LIVRE.");
        }

        mesaRepository.delete(mesa);
    }
}