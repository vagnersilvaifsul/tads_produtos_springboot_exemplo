package com.example.produtos.api.produto;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de controle API REST
@RequestMapping("api/v1/produtos") //Endpoint padrão da classe
public class ProdutoController {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<ProdutoDTOResponse>> selectAll() {
        return ResponseEntity.ok(service.getProdutos());
    }

    @GetMapping("{id}")
    public ResponseEntity<ProdutoDTOResponse> selectById(@PathVariable("id") Long id) {
        var p = service.getProdutoById(id);
        if(p.isPresent()){
            return ResponseEntity.ok(ProdutoDTOResponse.create(p.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ProdutoDTOResponse>> selectByNome(@PathVariable("nome") String nome) {
        var produtos = service.getProdutosByNome(nome);
        return produtos.isEmpty() ?
            ResponseEntity.noContent().build() :
            ResponseEntity.ok(produtos);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<URI> insert(@RequestBody Produto produto){
        var p = service.insert(produto);
        var location = getUri(p.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<ProdutoDTOResponse> update(@PathVariable("id") Long id, @Valid @RequestBody Produto produto){
        produto.setId(id);
        var p = service.update(produto, id);
        return p != null ?
            ResponseEntity.ok(p) :
            ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        return service.delete(id) ?
            ResponseEntity.ok().build() :
            ResponseEntity.notFound().build();
    }

    //utilitário
    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
