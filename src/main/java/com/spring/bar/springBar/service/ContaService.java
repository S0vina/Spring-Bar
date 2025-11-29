package com.spring.bar.springBar.service;


import com.spring.bar.springBar.entity.Conta;
import com.spring.bar.springBar.entity.Mesa;
import com.spring.bar.springBar.entity.ItemPedido;
import com.spring.bar.springBar.entity.Produto;
import com.spring.bar.springBar.entity.Pagamento;
import com.spring.bar.springBar.entity.Configuracao; // NOVO: Importa a Entidade Configuracao
import com.spring.bar.springBar.entity.Mesa.StatusMesa;
import com.spring.bar.springBar.entity.Produto.categoriaProduto;

import com.spring.bar.springBar.repository.ContaRepository;
import com.spring.bar.springBar.repository.MesaRepository;
import com.spring.bar.springBar.repository.ProdutoRepository;
import com.spring.bar.springBar.repository.ItemPedidoRepository;
import com.spring.bar.springBar.repository.PagamentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    // NOVO: Injeção do Service para obter configurações dinâmicas
    @Autowired
    private ConfiguracaoService configuracaoService;

    // REMOVIDO: PERC_GORJETA_COMIDA e PERC_GORJETA_BEBIDA (Agora dinamicos)
    private static final long PRODUTO_ID_COUVBERT= 1L;

    @Transactional
    public Conta abrirConta(int numeroMesa, int numPessoas, boolean habilitarCouvert) {

        Mesa mesa = mesaRepository.findByNumero(numeroMesa)
                .orElseThrow(() -> new NoSuchElementException("Mesa " + numeroMesa + " não encontrada para abertura."));

        if (mesa.getStatus() != StatusMesa.LIVRE) {
            throw new IllegalStateException("Mesa " + numeroMesa + " já está " + mesa.getStatus() + ". Impossível abrir nova conta.");
        }
        if (numPessoas <= 0) {
            throw new IllegalArgumentException("Número de pessoas inválido (" + numPessoas + "). Deve ser maior que zero.");
        }

        // 1. Atualiza a Mesa e gera token de acesso
        mesa.setStatus(StatusMesa.ABERTA);
        mesa.setTokenAcesso(UUID.randomUUID().toString());
        mesaRepository.save(mesa);

        // 2. Cria a nova Conta
        Conta novaConta = new Conta();
        novaConta.setMesa(mesa);
        novaConta.setMomentoAbertura(LocalDateTime.now());
        novaConta.setNumeroPessoas(numPessoas);
        novaConta.setCouverHabilitado(habilitarCouvert);
        novaConta.setPrecoCouvertPessoa(configuracaoService.buscarConfiguracaoAtual().getPrecoCouvert());
        novaConta.setPercGorjetaComida(configuracaoService.buscarConfiguracaoAtual().getPercGorjetaComida());
        novaConta.setPercGorjetaBebida(configuracaoService.buscarConfiguracaoAtual().getPercGorjetaBebida());
        Conta contaSalva = contaRepository.save(novaConta);

        // 3. Lançamento do Couvert (Usa o Produto ID 1 como marcador no ItemPedido)
        if (habilitarCouvert) {
            Produto produtoCouvert = produtoRepository.findById(PRODUTO_ID_COUVBERT)
                    .orElseThrow(() -> new NoSuchElementException("Produto Couvert (ID: " + PRODUTO_ID_COUVBERT + ") não encontrado no cardápio."));

            ItemPedido itemCouvert = new ItemPedido();
            itemCouvert.setConta(contaSalva);
            itemCouvert.setProduto(produtoCouvert);
            itemCouvert.setQuantidade(numPessoas); // Couvert é lançado por pessoa
            itemCouvert.setItemCancelado(false);

            itemPedidoRepository.save(itemCouvert);
        }
        return contaSalva;
    }

    @Transactional
    public ItemPedido cancelarItemPedido(int contaId, long itemPedidoId, String motivoCancelamento) {

        // Busca a conta para garantir que ela existe
        contaRepository.findById(Long.valueOf(contaId))
                .orElseThrow(() -> new NoSuchElementException("Conta " + contaId + " não encontrada."));

        // Busca o item pedido específico
        ItemPedido item = itemPedidoRepository.findById(itemPedidoId)
                .orElseThrow(() -> new NoSuchElementException("Item de pedido (ID: " + itemPedidoId + ") não encontrado."));

        // Validação
        if (item.getConta().getId() != contaId) {
            throw new IllegalArgumentException("O item de pedido não pertence à Conta " + contaId + ".");
        }
        if (item.getItemCancelado()) {
            throw new IllegalStateException("O item de pedido já estava cancelado.");
        }
        if (motivoCancelamento == null || motivoCancelamento.trim().isEmpty()) {
            throw new IllegalArgumentException("O motivo do cancelamento é obrigatório.");
        }

        // Ação: Cancelar o item
        item.setItemCancelado(true);
        item.setMotivoCancelamento(motivoCancelamento);

        return itemPedidoRepository.save(item);
    }

    @Transactional
    public ItemPedido adicionarPedido(int contaId, long produtoId, int quantidade) {
        Conta conta = contaRepository.findById(Long.valueOf(contaId))
                .orElseThrow(() -> new NoSuchElementException("Conta " + contaId + " não encontrada."));

        if (conta.getMesa().getStatus() == StatusMesa.FECHADA) {
            throw new IllegalStateException("Conta " + contaId + " já está FECHADA. Não é possível adicionar pedidos.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida (" + quantidade + "). Deve ser maior que zero.");
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new NoSuchElementException("Produto " + produtoId + " não encontrado no cardápio."));

        ItemPedido novoItem = new ItemPedido();
        novoItem.setConta(conta);
        novoItem.setProduto(produto);
        novoItem.setQuantidade(quantidade);
        novoItem.setItemCancelado(false);

        return itemPedidoRepository.save(novoItem);
    }

    public double calcularSaldoFinal(long contaId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new NoSuchElementException("Conta " + contaId + " não encontrada."));

        // Filtra os ItemPedidos que NÃO foram cancelados
        List<ItemPedido> itensValidos = conta.getItens().stream()
                .filter(item -> !item.getItemCancelado())
                .collect(Collectors.toList());

        // 1. CALCULA SUBTOTAIS
        double subtotalComida = 0.0;
        double subtotalBebida = 0.0;

        for (ItemPedido item : itensValidos) {
            double valorItem = item.getProduto().getPreco() * item.getQuantidade();
            if (item.getProduto().getCategoria() == Produto.categoriaProduto.COMIDA) {
                subtotalComida += valorItem;
            } else {
                subtotalBebida += valorItem;
            }
        }

        // 2. CALCULA GORJETAS (USANDO VALORES CONGELADOS DA CONTA)
        double gorjetaComida = subtotalComida * conta.getPercGorjetaComida(); // <--- AQUI!
        double gorjetaBebida = subtotalBebida * conta.getPercGorjetaBebida(); // <--- AQUI!
        double totalGorjetas = gorjetaComida + gorjetaBebida;

        // 3. CALCULA COUVERT (USANDO VALORES CONGELADOS DA CONTA)
        double valorCouvert = 0.0;
        if (conta.getCouverHabilitado()) {
            valorCouvert = conta.getPrecoCouvertPessoa() * conta.getNumeroPessoas(); // <--- AQUI!
        }

        // 4. CALCULA TOTAL DE ITENS
        double totalItens = subtotalComida + subtotalBebida;
        double valorTotalBruto = totalItens + totalGorjetas + valorCouvert;

        // 5. CALCULA PAGAMENTOS JÁ EFETUADOS
        double totalPago = conta.getPagamentos().stream()
                .mapToDouble(Pagamento::getValor)
                .sum();

        // 6. SALDO FINAL
        return valorTotalBruto - totalPago;
    }

    /**
     * [CLIENTE] Busca o saldo final da conta através do token de acesso da Mesa.
     */
    public double calcularSaldoFinalPorToken(String tokenAcesso) {

        // 1. Busca a Mesa pelo Token de Acesso
        Mesa mesa = mesaRepository.findByTokenAcesso(tokenAcesso)
                .orElseThrow(() -> new NoSuchElementException("Mesa não encontrada ou token de acesso inválido."));

        // 2. Obtém a Conta associada à Mesa
        Conta conta = mesa.getComanda();

        if (conta == null) {
            throw new NoSuchElementException("Conta não encontrada para o token fornecido. Contate o suporte.");
        }

        // 3. Usa o método de cálculo de saldo existente
        return calcularSaldoFinal(conta.getId());
    }

    /**
     * Registra pagamento na conta.
     */
    @Transactional
    public Pagamento registrarPagamento(int contaId, double valor, String tipoPagamento) {
        Conta conta = contaRepository.findById(Long.valueOf(contaId))
                .orElseThrow(() -> new NoSuchElementException("Conta " + contaId + " não encontrada para registrar pagamento."));

        double saldoAtual = calcularSaldoFinal(contaId);

        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de pagamento deve ser positivo.");
        }

        // Regra de Negócio: Não deve ser possível incluir pagamentos maiores que o valor da conta
        if (valor > saldoAtual) {
            throw new IllegalArgumentException("O pagamento de R$" + String.format("%.2f", valor) + " excede o saldo de R$" + String.format("%.2f", saldoAtual) + ".");
        }

        // Cria e salva o objeto Pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setConta(conta);
        pagamento.setValor(valor);
        pagamento.setTipo(tipoPagamento); // Usa o tipo de pagamento recebido
        pagamento.setDataPagamento(LocalDateTime.now());

        return pagamentoRepository.save(pagamento);
    }

    /**
     * [GARÇOM] Habilita ou dispensa o couvert para uma conta aberta.
     * Requisito: Habilitar/dispensar couvert.
     */
    @Transactional
    public Conta atualizarCouvert(Long contaId, Boolean habilitado) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new NoSuchElementException("Conta ID " + contaId + " não encontrada."));

        if (conta.getStatus() != Conta.StatusConta.ABERTA) {
            throw new IllegalStateException("Não é possível alterar o status do couvert de uma conta que não está ABERTA.");
        }

        conta.setCouverHabilitado(habilitado);

        return contaRepository.save(conta);
    }

    @Transactional
    public Conta fecharConta(Long id){
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta com id: " + id + " nao encontrada."));

        if(conta.getStatus() == Conta.StatusConta.FECHADA) {
            throw new IllegalStateException("Essa acao nao pode ser feita a uma conta fecha ja fechada.");
        }

        double saldoFinal = calcularSaldoFinal(id);

        // Verifica se ha um saldo devedor
        if(saldoFinal != 0.0) {
            throw new IllegalStateException("A conta nao pode ser fechada com saldo devedor");

        }

        conta.setStatus(Conta.StatusConta.FECHADA);
        conta.getMesa().setStatus(StatusMesa.FECHADA); // Define a mesa como livre novamente

        contaRepository.save(conta);

        return conta;

    }

    public Conta buscarContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta nao encontrada com o ID: " + id));
    }
}