package com.example.produtos.api.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final ProdutoRepository produtoRepository;

    //A classe ProdutoController depende de ProdutoRepository. Isso é o que está definido nesse construtor.
    //Ao analisar essa situação o Spring Boot irá INJETAR a ProdutoRepository, que ele escaneou a partir do extends JpaRepository<T,K>
    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    //O PageableDefault é sobrescrito pelos parâmetros da requisição (ou seja, a requisição é mandatória)
    //Experimente fazer a requisição assim: /api/v1/produtos?size=2&sort=nome,desc (verá que sobrescreve o PageableDefault)
    public ResponseEntity<Page<ProdutoDTOResponse>> findAll(@PageableDefault(size = 50, sort = "nome") Pageable paginacao) {
        return ResponseEntity.ok(produtoRepository.findAll(paginacao).map(ProdutoDTOResponse::new));
    }

    @GetMapping("{id}") //URL_BASE:8080/api/v1/produtos/1
    public ResponseEntity<ProdutoDTOResponse> findById(@PathVariable("id") Long id) {
        var optional = produtoRepository.findById(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(new ProdutoDTOResponse(optional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ProdutoDTOResponse>> finByNome(@PathVariable("nome") String nome) {
        var produtos = produtoRepository.findByNome(nome + "%");
        return produtos.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(produtos.stream().map(ProdutoDTOResponse::new).toList());
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<URI> insert(@RequestBody ProdutoDTOPost produtoDTO, UriComponentsBuilder uriBuilder) {
        var p = produtoRepository.save(new Produto(
                null,
                produtoDTO.nome(),
                produtoDTO.valorDeCompra(),
                produtoDTO.valorDeVenda(),
                produtoDTO.descricao(),
                true,
                produtoDTO.estoque(),
                null
        ));
        var location = uriBuilder.path("api/v1/produtos/{id}").buildAndExpand(p.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<ProdutoDTOResponse> update(@PathVariable("id") Long id, @RequestBody ProdutoDTOPut produtoDTO) {
        var optional = produtoRepository.findById(id);
        if (optional.isPresent()) {
            var p = produtoRepository.save(new Produto(
                    id,
                    produtoDTO.nome(),
                    produtoDTO.valorDeCompra(),
                    produtoDTO.valorDeVenda(),
                    produtoDTO.descricao(),
                    produtoDTO.situacao(),
                    produtoDTO.estoque(),
                    null
            ));
            return ResponseEntity.ok(new ProdutoDTOResponse(p));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProdutoDTOResponse> delete(@PathVariable("id") Long id) {
        var optional = produtoRepository.findById(id);
        if (optional.isPresent()) {
            produtoRepository.deleteById(id);
            return ResponseEntity.ok(new ProdutoDTOResponse(optional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
