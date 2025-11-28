package com.spring.bar.springBar.controller;

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
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos); // Retorna 200 OK
    }

    /**
     * [ADMIN] Cadastrar novo item no cardápio.
     * Endpoint: POST /api/cardapio
     */
    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.cadastrarProduto(produto);
        // Retorna 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    /**
     * [ADMIN] Editar item do cardápio.
     * Endpoint: PUT /api/cardapio/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produto> editarProduto(@PathVariable long id, @RequestBody Produto produtoAtualizado) {
        Produto produtoEditado = produtoService.editarProduto(id, produtoAtualizado);
        // Retorna 200 OK
        return ResponseEntity.ok(produtoEditado);
    }

    // Você também pode adicionar um endpoint DELETE, se necessário, para remover o item do cardápio.
}