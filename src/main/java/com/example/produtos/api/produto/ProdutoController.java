package com.example.produtos.api.produto;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/*
    Pelo Princípio da Responsabilidade Única (SRP - Single-responsibility Principle) os controller manipulam
    apenas DTOs.
    Porém, se a meta for entregar o produto no Curto Prazo, é aceitável não utilizar DTO no controller.
    Mas, se a meta for de Longo Prazo, a entrega de um produto em produção, é importante aplicar o SRP, e fazer
    com que a controller trabalhe apenas com DTOs, isso facilitará a manutenção no futuro e ajuda na segurança do
    aplicativo (não expõe todos os dados aos clientes do aplicativo).
 */
@RestController
//indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de controle API REST
@RequestMapping("api/v1/produtos") //Endpoint padrão da classe
public class ProdutoController {

    private final ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTOResponse>> findAll() {
        return ResponseEntity.ok(repository.findAll().stream().map(ProdutoDTOResponse::new).toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<Produto> findById(@PathVariable Long id) {
        var optionalProduto = repository.findById(id);
        if (optionalProduto.isPresent()) {
            return ResponseEntity.ok(optionalProduto.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("nome/{nome}")
    public ResponseEntity<List<Produto>> findByNome(@PathVariable String nome) {
        var produtos = repository.findByNome(nome + "%");
        if (produtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    //@Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<URI> insert(@Valid @RequestBody ProdutoDTOPost produtoDTOPost, UriComponentsBuilder uriBuilder) {
        var p = repository.save(new Produto(
                produtoDTOPost.nome(),
                produtoDTOPost.valorDeCompra(),
                produtoDTOPost.valorDeVenda(),
                produtoDTOPost.descricao(),
                produtoDTOPost.estoque()
        ));
        var location = uriBuilder.path("api/v1/produtos/{id}").buildAndExpand(p.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<ProdutoDTOResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ProdutoDTOPut produtoDTOPut) {
        var p = repository.save(new Produto(
                id,
                produtoDTOPut.nome(),
                produtoDTOPut.valorDeCompra(),
                produtoDTOPut.valorDeVenda(),
                produtoDTOPut.descricao(),
                produtoDTOPut.situacao(),
                produtoDTOPut.estoque()
        ));
        return p != null ?
                ResponseEntity.ok(new ProdutoDTOResponse(p)) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
