package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.dto.ProdutoRequestDTO;
import com.spring.bar.springBar.service.ProdutoService;
import com.spring.bar.springBar.entity.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciar o Cardápio (Funções do Administrador).
 */
@RestController
@RequestMapping("/api/cardapio")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Endpoint para listar todos os produtos (pode ser usado pelo Garçom/Admin)
    @GetMapping
    public ResponseEntity<List<Produto>> listarCardapio() {
        List<Produto> produtos = ProdutoService.listarTodos();
        return ResponseEntity.ok(produtos); // Retorna 200 OK
    }

    /**
     * [ADMIN] Cadastrar novo item no cardápio.
     * Endpoint: POST /api/cardapio
     */
    @PostMapping("/api/cardapio")
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody ProdutoRequestDTO dto) {

        // Converte DTO para entity
        Produto novoProduto = produtoService.converterDtoParaEntidade(dto);

        // Usando o service
        Produto cadastrado = produtoService.salvar(novoProduto);

        // Retorna 201 Created e o objeto salvo (com o ID gerado)
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrado);
    }

    /**
     * [ADMIN] Editar item do cardápio.
     * Endpoint: PUT /api/cardapio/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produto> editarProduto(@PathVariable long id, @RequestBody Produto produtoAtualizado) {

        Produto produtoEditado = produtoService.salvar(id, produtoAtualizado);
        return ResponseEntity.ok(produtoEditado);
    }


}