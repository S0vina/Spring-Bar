package com.spring.bar.springBar.service;

import com.spring.bar.springBar.entity.Produto;
import com.spring.bar.springBar.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Marca esta classe como um bean de service
public class produtoService {

    private final ProdutoRepository produtoRepository;

    // construtor da dependencia
    public produtoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * Retorna todos os produtos do cardápio.
     * Usado para a tela de listagem do Administrador.
     */
    public List<Produto> buscarTodos() {
        return produtoRepository.findAll();
    }

    /**
     * Salva ou atualiza um produto no banco de dados.
     * Usado para o formulário de Cadastro/Edição.
     */
    public Produto salvar(Produto produto) {
        // Aqui você poderia adicionar lógica de negócio, como validar preço > 0
        return produtoRepository.save(produto);
    }

    // Método para a futura funcionalidade de Edição
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    }

    // Método para a futura funcionalidade de Exclusão
    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
    }


}