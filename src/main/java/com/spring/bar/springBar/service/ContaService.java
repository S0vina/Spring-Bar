package com.spring.bar.springBar.service;

import com.spring.bar.springBar.dto.AberturaContaDTO;
import com.spring.bar.springBar.entity.Conta;
import com.spring.bar.springBar.entity.Mesa;
import com.spring.bar.springBar.entity.ItemPedido;
import com.spring.bar.springBar.entity.Pagamento;
import com.spring.bar.springBar.entity.Configuracao;
import com.spring.bar.springBar.entity.Mesa.StatusMesa;

import com.spring.bar.springBar.repository.ContaRepository;
import com.spring.bar.springBar.repository.MesaRepository;
import com.spring.bar.springBar.repository.ItemPedidoRepository;
import com.spring.bar.springBar.repository.PagamentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ConfiguracaoService configuracaoService;

    // Métodos Auxiliares
    public Conta buscarContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Conta não encontrada com ID: " + id));
    }

    // Calcula o valor total dos itens (comida + bebida)
    private double calcularTotalItens(Conta conta) {
        double total = 0.0;
        if (conta.getItens() != null) {
            for (ItemPedido item : conta.getItens()) {
                if (!item.getItemCancelado()) {
                    total += item.getProduto().getPreco() * item.getQuantidade();
                }
            }
        }
        return total;
    }

    /**
     * Calcula o total do couvert.
     * Usa o preço copiado na conta e o número de pessoas/status habilitado da mesa.
     */
    private double calcularTotalCouvert(Conta conta) {
        Mesa mesa = conta.getMesa();
        // Verifica se couvert está habilitado E se o preço foi definido (copiado)
        if (mesa.getCouverHabilitado() != null && mesa.getCouverHabilitado() && conta.getPrecoCouvertPessoa() != null) {
            // O couvert é calculado pelo Preço Copiado na Conta * Número de Pessoas na Mesa
            return conta.getPrecoCouvertPessoa() * mesa.getNumPessoas();
        }
        return 0.0;
    }

    /**
     * [GARÇOM] Abrir conta (comanda) para uma mesa.
     * Endpoint: POST /api/contas/abrir
     */
    @Transactional
    public Conta abrirConta(AberturaContaDTO dto) {
        // 1. Busca e valida a Mesa
        Mesa mesa = mesaRepository.findByNumero(Math.toIntExact(dto.getNumeroMesa()))
                .orElseThrow(() -> new NoSuchElementException("Mesa " + dto.getNumeroMesa() + " não encontrada."));

        if (mesa.getStatus() != StatusMesa.LIVRE) {
            throw new IllegalStateException("A mesa " + dto.getNumeroMesa() + " não está LIVRE.");
        }

        // 2. Busca as Configurações Globais
        Configuracao config = configuracaoService.buscarConfiguracaoAtual();

        // 3. Atualiza os dados da Mesa com base no DTO
        mesa.setNumPessoas(dto.getNumPessoas());
        // A informação do couvert vem do DTO e é salva na MESA (que é referenciada pela Conta)
        mesa.setCouverHabilitado(dto.isHabilitarCouvert());
        mesa.setStatus(StatusMesa.ABERTA);
        // Gera e salva o Token de Acesso
        mesa.setTokenAcesso(UUID.randomUUID().toString());

        Mesa mesaAtualizada = mesaRepository.save(mesa);

        // 4. Cria e configura a nova Conta
        Conta novaConta = new Conta();
        novaConta.setMesa(mesaAtualizada); // Linka a Conta à Mesa
        novaConta.setStatus(Conta.StatusConta.ABERTA);

        // 5. Copia as configurações de preço/percentuais para a Conta para histórico
        novaConta.setPercGorjetaComida(config.getPercGorjetaComida());
        novaConta.setPercGorjetaBebida(config.getPercGorjetaBebida());
        novaConta.setPrecoCouvertPessoa(config.getPrecoCouvertPessoa()); // Copia o Preço do Couvert
        novaConta.setDataAbertura(LocalDateTime.now());

        // 6. Salva e retorna a Conta. O mapeamento OneToOne em Mesa garante que a referência (comanda) será atualizada
        return contaRepository.save(novaConta);
    }

    /**
     * Calcula o saldo final de uma conta (Total de itens + Gorjeta + Couvert - Pagamentos).
     */
    public double calcularSaldoFinal(Long id) { // ID da Conta é Long
        Conta conta = buscarContaPorId(id);

        double totalItens = calcularTotalItens(conta);

        // Adiciona o Couvert ao total, se habilitado
        double totalCouvert = calcularTotalCouvert(conta);

        // Simplificado: Total = Itens + Couvert (sem gorjeta ainda)
        double totalConta = totalItens + totalCouvert;

        // Calcula o total de pagamentos
        double totalPagamentos = 0.0;
        if (conta.getPagamentos() != null) {
            totalPagamentos = conta.getPagamentos().stream()
                    .mapToDouble(Pagamento::getValor)
                    .sum();
        }

        // Saldo = Total Geral - Pagamentos
        return totalConta - totalPagamentos;
    }


    /**
     * Calcula o saldo final de uma conta, buscando-a pelo token da Mesa.
     */
    public double calcularSaldoFinalPorToken(String tokenAcesso) {
        Mesa mesa = mesaRepository.findByTokenAcesso(tokenAcesso)
                .orElseThrow(() -> new NoSuchElementException("Mesa não encontrada para o token: " + tokenAcesso));

        // A comanda é a conta OneToOne, usamos seu ID para calcular.
        if (mesa.getComanda() == null) {
            throw new NoSuchElementException("Nenhuma conta aberta para o token: " + tokenAcesso);
        }

        // É crucial usar o Long para o ID
        return calcularSaldoFinal((long) mesa.getComanda().getId());
    }


    /**
     * [GARÇOM] Fechar conta.
     *
     * @return
     */
    @Transactional
    public Conta fecharConta(Long contaId) {
        // Busca a conta usando o ID como Long
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new NoSuchElementException("Conta " + contaId + " não encontrada."));

        if (conta.getStatus() == Conta.StatusConta.FECHADA) {
            throw new IllegalStateException("A conta " + contaId + " já está fechada.");
        }

        // Calcula o saldo final usando o ID da conta como Long
        double saldo = calcularSaldoFinal(contaId);

        if (saldo > 0.01) { // Permite pequena margem de erro
            throw new IllegalStateException("Não é possível fechar a conta. Saldo devedor restante: R$" + String.format("%.2f", saldo));
        }

        // 1. Atualiza Status da Conta
        conta.setStatus(Conta.StatusConta.FECHADA);
        conta.setDataFechamento(LocalDateTime.now());
        contaRepository.save(conta);

        // 2. Atualiza Status da Mesa
        Mesa mesa = conta.getMesa();
        mesa.setStatus(StatusMesa.LIVRE); // Mesa volta a ficar livre
        mesa.setNumPessoas(0);
        mesa.setCouverHabilitado(null); // Limpa o status do couvert
        mesa.setTokenAcesso(null); // Invalida o token
        // Não removemos a comanda da Mesa.java, pois a conta fechada deve ser mantida para histórico.
        // O mapeamento @OneToOne em Mesa deve ser ajustado para não ser 'orphanRemoval = true'
        // se quisermos manter Contas fechadas.
        mesa.setComanda(null);

        mesaRepository.save(mesa);
        return conta;
    }


    /**
     * Registra pagamento na conta.
     */
    @Transactional
    public Pagamento registrarPagamento(Long contaId, double valor, String tipoPagamento) {
        // Busca a conta usando o ID como Long
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new NoSuchElementException("Conta " + contaId + " não encontrada para registrar pagamento."));

        // Calcula o saldo atual usando o ID da conta como Long
        double saldoAtual = calcularSaldoFinal(contaId); // CORRIGIDO: Agora recebe Long

        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de pagamento deve ser positivo.");
        }

        // Regra de Negócio: Não deve ser possível incluir pagamentos maiores que o saldo restante
        if (valor > saldoAtual) {
            throw new IllegalArgumentException("O pagamento de R$" + String.format("%.2f", valor) + " excede o saldo restante de R$" + String.format("%.2f", saldoAtual) + ".");
        }

        // Se o saldo for muito pequeno, mas positivo, ajusta o valor para evitar sobrepagamento
        if (saldoAtual > 0 && saldoAtual < valor) {
            valor = saldoAtual;
        }

        // Cria e salva o objeto Pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setConta(conta);
        pagamento.setValor(valor);
        pagamento.setTipo(tipoPagamento); // Usa o tipo de pagamento recebido
        pagamento.setDataPagamento(LocalDateTime.now());

        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public ItemPedido cancelarItemPedido(Long contaId, Long itemPedidoId, String motivo) {
        // 1. Busca e valida a Conta
        // Note: Assumindo que o ID da Conta é 'int' baseado na sua entidade Conta.java
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new NoSuchElementException("Conta ID " + contaId + " não encontrada para cancelamento de pedido."));

        // Regra de Negócio: Cancelamento só é permitido se a conta estiver ABERTA
        if (conta.getStatus() != Conta.StatusConta.ABERTA) {
            throw new IllegalStateException("Não é possível cancelar itens em uma conta que não está ABERTA. Status atual: " + conta.getStatus());
        }

        // 2. Busca e valida o ItemPedido
        ItemPedido item = itemPedidoRepository.findById(itemPedidoId)
                .orElseThrow(() -> new NoSuchElementException("Item de Pedido ID " + itemPedidoId + " não encontrado."));

        // 3. Valida se o ItemPedido pertence à Conta
        // Note: Assumindo que o ID da Conta na ItemPedido entity é um objeto Conta (getConta().getId())
        if (item.getConta().getId() != contaId) {
            throw new IllegalArgumentException("O Item de Pedido ID " + itemPedidoId + " não pertence à Conta ID " + contaId + ".");
        }

        // 4. Valida se o item já está cancelado
        if (item.getItemCancelado()) {
            throw new IllegalStateException("O Item de Pedido ID " + itemPedidoId + " já se encontra cancelado. Motivo anterior: " + item.getMotivoCancelamento());
        }

        // 5. Aplica o cancelamento
        item.setItemCancelado(true);
        // Garante que o motivo não é nulo/vazio
        item.setMotivoCancelamento(motivo != null && !motivo.trim().isEmpty() ? motivo : "Motivo não especificado.");

        // 6. Salva e retorna o item atualizado
        return itemPedidoRepository.save(item);
    }

    @Transactional
    public Conta atualizarCouvert(Long contaId, boolean habilitarCouvert) {
        // 1. Busca e valida a Conta
        // Note: Converte o ID int para Long, que é o tipo comum para IDs de repositório.
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new NoSuchElementException("Conta ID " + contaId + " não encontrada para atualizar Couvert."));

        // 2. Regra de Negócio: Apenas contas ABERTAS podem ter o couvert alterado.
        if (conta.getStatus() != Conta.StatusConta.ABERTA) {
            throw new IllegalStateException("Não é possível alterar o Couvert em uma conta que não está ABERTA. Status atual: " + conta.getStatus());
        }

        Mesa mesaConsultada = conta.getMesa();
        // 3. Verifica se o valor é o mesmo para evitar operações desnecessárias
        if (mesaConsultada.getCouverHabilitado() == habilitarCouvert) {
            String status = habilitarCouvert ? "habilitado" : "desabilitado";
            // throw new IllegalStateException("O Couvert já se encontra " + status + " nesta conta.");
            // Opcional: Se for apenas um PUT, podemos simplesmente retornar 200/Conta.
            return conta;
        }

        // 4. Aplica a alteração na entidade Conta
        mesaConsultada.setCouverHabilitado(habilitarCouvert);

        // 5. Salva e retorna a Conta atualizada
        return contaRepository.save(conta);
    }
}