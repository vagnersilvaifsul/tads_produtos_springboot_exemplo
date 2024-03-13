package com.example.produtos.api.produto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/produtos")
@Api(value = "Produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    @ApiOperation(value = "Retorna todos os produtos cadastrados.")
    public ResponseEntity<List<ProdutoDTOResponse>> selectAll() {
        return ResponseEntity.ok(service.getProdutos());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Retorna um produto pelo campo identificador.")
    public ResponseEntity<ProdutoDTOResponse> selectById(@PathVariable("id") Long id) {
        var p = service.getProdutoById(id);
        if(p.isPresent()){
            return ResponseEntity.ok(ProdutoDTOResponse.create(p.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nome/{nome}")
    @ApiOperation(value = "Retorna uma lista de produtos pela chave nome.")
    public ResponseEntity<List<ProdutoDTOResponse>> selectByNome(@PathVariable("nome") String nome) {
        var produtos = service.getProdutosByNome(nome);
        return produtos.isEmpty() ?
            ResponseEntity.noContent().build() :
            ResponseEntity.ok(produtos);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    @ApiOperation(value = "Insere um novo produto.")
    public ResponseEntity<URI> insert(@RequestBody Produto produto){
        var p = service.insert(produto);
        var location = getUri(p.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Altera um produto existente.")
    public ResponseEntity<ProdutoDTOResponse> update(@PathVariable("id") Long id, @Valid @RequestBody Produto produto){
        produto.setId(id);
        var p = service.update(produto, id);
        return p != null ?
            ResponseEntity.ok(p) :
            ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta um produto.")
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
