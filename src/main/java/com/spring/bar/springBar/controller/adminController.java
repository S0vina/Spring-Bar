package com.spring.bar.springBar.controller;

import com.spring.bar.springBar.entity.Produto;
import com.spring.bar.springBar.service.produtoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Arrays;

@Controller
@RequestMapping("/admin")
public class adminController {

    // A injeção de dependência do Service (Spring vai instanciar para você)
    private final produtoService produtoService;

    // Construtor para Injeção de Dependência
    public adminController(produtoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Lida com a requisição GET /admin/cardapio (Exibir a página)
     */
    @GetMapping("/cardapio")
    public String listarCardapio(Model model) {
        // 1. Busca a lista de produtos existentes (para a tabela)
        List<Produto> produtos = produtoService.buscarTodos();
        model.addAttribute("produtos", produtos);

        // 2. Cria um objeto vazio 'produtoForm' (para o formulário de cadastro)
        model.addAttribute("produtoForm", new Produto());

        // 3. Adiciona as opções do enum para o <select> de categoria
        model.addAttribute("categorias", Arrays.asList(Produto.categoriaProduto.values()));

        // Retorna o nome do template (AdminCardapio.html)
        return "admin/AdminCardapio";
    }

    @GetMapping("/cardapio/editar/{id}")
    public String editarProduto(@PathVariable("id") Long id, Model model) {
        // Busca produto existente pelo id
        Produto produtoEditado = produtoService.buscarPorId(id);

        model.addAttribute("produtoForm", produtoEditado);

        model.addAttribute("categorias", Arrays.asList(Produto.categoriaProduto.values()));

        model.addAttribute("produtos", produtoService.buscarTodos());

        return "admin/adminCardapio.html";
    }

    @GetMapping("/cardapio/excluir//{id}")
    public String excluirProduto(Long id) {
        produtoService.deleteById(id);

        return "redirect:/admin/cardapio";
    }

    /**
     * Lida com a requisição POST /admin/cardapio (Salvar um novo produto)
     */
    @PostMapping("/cardapio")
    public String salvarProduto(@ModelAttribute("produtoForm") Produto produto) {
        // Salva o objeto preenchido pelo formulário no banco de dados
        produtoService.salvar(produto);

        // Redireciona para o GET, recarregando a página e mostrando a lista atualizada
        return "redirect:/admin/cardapio";
    }
}