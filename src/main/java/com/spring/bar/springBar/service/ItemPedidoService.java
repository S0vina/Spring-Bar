package com.spring.bar.springBar.service;

import com.spring.bar.springBar.entity.Conta;
import com.spring.bar.springBar.entity.ItemPedido;
import com.spring.bar.springBar.entity.Produto;
import com.spring.bar.springBar.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemPedidoService {
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoService produtoService;
    private ContaService contaService;


    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository, ProdutoService produtoService, ContaService contaService) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoService = produtoService;
        this.contaService = contaService;
    }

    public ItemPedido adicionarItem(Long contaId, Long produtoId, int quantidade) {
        Conta conta = contaService.buscarContaPorId(contaId);
        Produto produto = produtoService.buscarPorId(produtoId);

        if (conta.getStatus() != Conta.StatusConta.ABERTA) {
            throw new RuntimeException("Nao eh possivel adicionar itens a uma conta que nao esta aberta!");
        }

        // Cria o itemPedido
        ItemPedido item = new ItemPedido();
        item.setConta(conta);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setItemCancelado(false);

        // Salva o item
        return this.itemPedidoRepository.save(item);

    }
}
