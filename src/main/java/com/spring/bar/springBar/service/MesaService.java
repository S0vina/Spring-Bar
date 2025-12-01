package com.spring.bar.springBar.service;

import com.spring.bar.springBar.dto.MesaRequestDTO;
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

        // 1. O número da mesa deve vir do DTO.
        mesa.setNumero(dto.getNumero());

        // 2. O número de pessoas DEVE ser zero no cadastro.
        mesa.setNumPessoas(0);

        // 3. Couver Habilitado: Usa o valor do DTO ou padroniza para true
        mesa.setCouverHabilitado(dto.getCouverHabilitado() != null ? dto.getCouverHabilitado() : true);

        // 4. Status inicial
        mesa.setStatus(StatusMesa.LIVRE);

        // 5. CORRIGIDO: Token inicial volta para null, confiando que a restrição UNIQUE foi removida do Mesa.java
        mesa.setTokenAcesso(null);

        return mesa;
    }

    /**
     * [ADMIN] Listar todas as mesas.
     */
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    /**
     * [ADMIN] Cadastrar nova mesa.
     */
    @Transactional
    public Mesa cadastrarMesa(MesaRequestDTO dto) {
        // 1. Validação
        //validarNumeroMesa(dto.getNumero());

        // 2. Regra de Negócio: Garante que o número da mesa é único
        //Optional<Mesa> mesaExistente = mesaRepository.findByNumero(dto.getNumero());
        //if (mesaExistente.isPresent()) {
         //   throw new IllegalStateException("O número da mesa " + dto.getNumero() + " já está em uso por outra mesa.");
        //}

        // 3. Conversão para Entidade e Salva
        Mesa novaMesa = converterDtoParaEntidade(dto);

        return mesaRepository.save(novaMesa);
    }

    /**
     * [ADMIN] Editar mesa existente.
     *
     * @param id ID da mesa a ser atualizada.
     * @param mesaAtualizadaDTO DTO com os novos dados (apenas numero e couverHabilitado são usados).
     * @return Mesa atualizada.
     */
    @Transactional
    public Mesa editarMesa(long id, MesaRequestDTO mesaAtualizadaDTO) {
        Mesa mesaExistente = mesaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mesa ID " + id + " não encontrada para edição."));

        validarNumeroMesa(mesaAtualizadaDTO.getNumero());

        // Se o número da mesa foi alterado, verifica a unicidade
        if (mesaExistente.getNumero() != mesaAtualizadaDTO.getNumero()) {
            Optional<Mesa> mesaConflito = mesaRepository.findByNumero(mesaAtualizadaDTO.getNumero());
            if (mesaConflito.isPresent() && mesaConflito.get().getId() != id) {
                throw new IllegalStateException("O número da mesa " + mesaAtualizadaDTO.getNumero() + " já está em uso por outra mesa.");
            }
            mesaExistente.setNumero(mesaAtualizadaDTO.getNumero());
        }

        // Atualiza a flag de couvert
        mesaExistente.setCouverHabilitado(mesaAtualizadaDTO.getCouverHabilitado() != null ? mesaAtualizadaDTO.getCouverHabilitado() : mesaExistente.getCouverHabilitado());

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
            // Em uma aplicação real, você deve buscar a Conta.
            // Para simplificar, assumimos que se a Mesa está OCUPADA/AGUARDANDO, há uma Conta em aberto.
            throw new IllegalStateException("Não é possível excluir a mesa. Status atual: " + mesa.getStatus() + ". A mesa deve estar LIVRE.");
        }

        mesaRepository.delete(mesa);
    }

    /**
     * Busca uma mesa pelo número e lança exceção se não for encontrada.
     * Usado internamente pelo ContaService.
     */
    public Mesa buscarMesaPorNumero(int numeroMesa) {
        return mesaRepository.findByNumero(numeroMesa)
                .orElseThrow(() -> new NoSuchElementException("Mesa de número " + numeroMesa + " não cadastrada."));
    }

    /**
     * Busca uma mesa pelo token de acesso.
     * Usado internamente pelo ContaService.
     */
    public Mesa buscarMesaPorToken(String tokenAcesso) {
        return mesaRepository.findByTokenAcesso(tokenAcesso)
                .orElseThrow(() -> new NoSuchElementException("Token de acesso inválido ou expirado."));
    }
}