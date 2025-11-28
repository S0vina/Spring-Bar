package com.spring.bar.springBar.service;

import com.spring.bar.springBar.entity.Produto;
import com.spring.bar.springBar.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Validação básica para Produto.
     * @param produto Produto a ser validado.
     */
    private void validarProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }
        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        if (produto.getCategoria() == null) {
            throw new IllegalArgumentException("A categoria do produto é obrigatória (COMIDA ou BEBIDA).");
        }
    }

    /**
     * [ADMIN] Cadastrar novo item no cardápio.
     */
    @Transactional
    public Produto cadastrarProduto(Produto produto) {
        validarProduto(produto);
        // Regra de Negócio: Não permite cadastrar produto com o mesmo nome (opcional, mas boa prática)
        // Você precisaria de um método findByNome() no ProdutoRepository para isso.

        return produtoRepository.save(produto);
    }

    /**
     * [ADMIN] Editar item do cardápio.
     */
    @Transactional
    public Produto editarProduto(long id, Produto produtoAtualizado) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto ID " + id + " não encontrado para edição."));

        validarProduto(produtoAtualizado);

        // Atualiza os campos do produto existente
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setCategoria(produtoAtualizado.getCategoria());

        return produtoRepository.save(produtoExistente);
    }

    /**
     * [ADMIN] Listar todos os itens do cardápio.
     */
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    /**
     * [ADMIN/GARÇOM] Buscar produto por ID.
     */
    public Produto buscarPorId(long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto ID " + id + " não encontrado."));
    }
}