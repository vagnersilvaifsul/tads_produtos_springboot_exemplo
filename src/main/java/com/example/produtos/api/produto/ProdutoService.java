package com.example.produtos.api.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/*
    Pelo Princípio da Responsabilidade Única (SRP - Single-responsibility Principle) as services manipulam
    apenas Entidades (objetos da classe de modelo).
 */
@Service //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de serviço de dados
public class ProdutoService {

    private ProdutoRepository rep;

    //A classe ProdutoService depende de ProdutoRepository. Isso é o que está definido nesse construtor.
    //Ao analisar essa situação o Spring Boot irá INJETAR a ProdutoRepository, que ele escaneou a partir da anotação implícita @Repository na classe ProdutoRepository
    public ProdutoService(ProdutoRepository rep){
        this.rep = rep;
    }

    public Page<Produto> getProdutos(Pageable paginacao) {
        return rep.findAll(paginacao);
    }

    public Optional<Produto> getProdutoById(Long id) {
        return rep.findById(id);
    }

    public List<Produto> getProdutosByNome(String nome) {
        return rep.findByNome(nome+"%");
    }

    public Produto insert(Produto produto) {
        Assert.isNull(produto.getId(),"Não foi possível inserir o registro");
        return rep.save(produto);
    }

    public Produto update(Produto produto) {
        Assert.notNull(produto.getId(),"Não foi possível atualizar o registro");

        // Busca o produto no banco de dados
        var optional = rep.findById(produto.getId());
        if(optional.isPresent()) {
            var db = optional.get();
            // Copia as propriedades
            db.setNome(produto.getNome());
            db.setValorDeCompra(produto.getValorDeCompra());
            db.setValorDeVenda(produto.getValorDeVenda());
            db.setDescricao(produto.getDescricao());
            db.setSituacao(produto.getSituacao());
            db.setEstoque(produto.getEstoque());
            return rep.save(db);
        }
        return null;

    }

    public boolean delete(Long id) {
        var optional = rep.findById(id);
        if(optional.isPresent()) {
            rep.deleteById(id);
            return true;
        }
        return false;
    }
}
