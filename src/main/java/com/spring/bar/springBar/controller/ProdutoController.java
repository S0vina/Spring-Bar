package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.dto.ProdutoRequestDTO;
// CRIE UM NOVO DTO: ProdutoResponseDTO
// import com.spring.bar.springBar.dto.ProdutoResponseDTO;
import com.spring.bar.springBar.service.ProdutoService;
import com.spring.bar.springBar.entity.Produto;
import jakarta.validation.Valid; // NOVO: Import para validação
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cardapio")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService produtoService; // Usando injeção via construtor (preferencial)

    // A injeção de dependência é feita automaticamente pelo Spring
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Endpoint para listar todos os produtos
    @GetMapping
    // IDEAL: Retornar ResponseEntity<List<ProdutoResponseDTO>>
    public ResponseEntity<List<Produto>> listarCardapio() {
        // CORREÇÃO: Chama o Service via variável de instância, não estaticamente
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    // Endpoint: POST /api/cardapio
    @PostMapping // REMOVIDA URL redundante
    public ResponseEntity<Produto> cadastrarProduto(@Valid @RequestBody ProdutoRequestDTO dto) {

        Produto novoProduto = produtoService.converterDtoParaEntidade(dto);
        Produto cadastrado = produtoService.salvar(novoProduto);

        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> editarProduto(@PathVariable long id, @Valid @RequestBody ProdutoRequestDTO dto) {
        // Chame o serviço com o DTO, não a entidade
        // return ResponseEntity.ok(produtoService.atualizar(id, dto));
        return null; // Apenas para compilar no exemplo
    }
}