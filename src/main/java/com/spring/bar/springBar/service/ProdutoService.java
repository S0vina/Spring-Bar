package com.spring.bar.springBar.service;

import com.spring.bar.springBar.dto.ProdutoRequestDTO;
import com.spring.bar.springBar.entity.Produto;
import com.spring.bar.springBar.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Marca esta classe como um bean de service
public class ProdutoService {

    // Instancia do ProdutoRepository
    private ProdutoRepository produtoRepository = null;

    // construtor da dependencia
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * Retorna todos os produtos do cardápio.
     * Usado para a tela de listagem do Administrador.
     */
    public List<Produto> listarTodos() {
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

    // deleta o produto por ID
    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
    }

    public Produto converterDtoParaEntidade(ProdutoRequestDTO dto) {
        Produto produto = new Produto();

        // Mapeamento manual dos campos do DTO para a entity
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());

        return produto;
    }

}