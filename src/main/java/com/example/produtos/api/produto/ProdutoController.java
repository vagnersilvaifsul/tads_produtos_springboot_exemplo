package com.example.produtos.api.produto;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/*
    Pelo Princípio da Responsabilidade Única (SRP - Single-responsibility Principle) os controller manipulam
    apenas DTOs.
    Porém, se a meta for entregar o produto no Curto Prazo, é aceitável não utilizar DTO no controller.
    Mas, se a meta for de Longo Prazo, a entrega de um produto em produção, é importante aplicar o SRP, e fazer
    com que a controller trabalhe apenas com DTOs, isso facilitará a manutenção no futuro e ajuda na segurança do
    aplicativo (não expõe todos os dados aos clientes do aplicativo).
 */
@RestController //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de controle API REST
@RequestMapping("api/v1/produtos") //Endpoint padrão da classe
public class ProdutoController {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private ProdutoService service;

    @GetMapping
    //O PageableDefault é sobrescrito pelos parâmetros da requisição (ou seja, a requisição é mandatória)
    //Experimente fazer a requisição assim: /api/v1/produtos?size=2&sort=nome,desc (verá que sobrescreve o PageableDefault)
    public ResponseEntity<Page<ProdutoDTOResponse>> selectAll(@PageableDefault(size = 50, sort = "nome") Pageable paginacao) {
        return ResponseEntity.ok(service.getProdutos(paginacao).map(ProdutoDTOResponse::new));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProdutoDTOResponse> selectById(@PathVariable("id") Long id) {
        var p = service.getProdutoById(id);
        if(p.isPresent()){
            return ResponseEntity.ok(new ProdutoDTOResponse(p.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ProdutoDTOResponse>> selectByNome(@PathVariable("nome") String nome) {
        var produtos = service.getProdutosByNome(nome);
        return produtos.isEmpty() ?
            ResponseEntity.noContent().build() :
            ResponseEntity.ok(produtos.stream().map(ProdutoDTOResponse::new).collect(Collectors.toList()));
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<URI> insert(@RequestBody Produto produto, UriComponentsBuilder uriBuilder){
        var p = service.insert(produto);
        var location = uriBuilder.path("api/v1/produtos/{id}").buildAndExpand(p.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<ProdutoDTOResponse> update(@PathVariable("id") Long id, @Valid @RequestBody Produto produto){
        produto.setId(id);
        var p = service.update(produto, id);
        return p != null ?
            ResponseEntity.ok(new ProdutoDTOResponse(p)):
            ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        return service.delete(id) ?
            ResponseEntity.ok().build() :
            ResponseEntity.notFound().build();
    }
}
