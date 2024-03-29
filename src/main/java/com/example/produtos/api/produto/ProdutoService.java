package com.example.produtos.api.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de serviço de dados
public class ProdutoService {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private ProdutoRepository rep;

    public List<ProdutoDTOResponse> getProdutos() {
        return rep.findAll().stream().map(ProdutoDTOResponse::create).collect(Collectors.toList());
    }

    public Optional<Produto> getProdutoById(Long id) {
        return rep.findById(id);
    }

    public List<ProdutoDTOResponse> getProdutosByNome(String nome) {
        return rep.findByNome(nome+"%").stream().map(ProdutoDTOResponse::create).collect(Collectors.toList());
    }

    public Produto insert(Produto produto) {
        Assert.isNull(produto.getId(),"Não foi possível inserir o registro");
        return rep.save(produto);
    }

    public ProdutoDTOResponse update(Produto produto, Long id) {
        Assert.notNull(id,"Não foi possível atualizar o registro");

        // Busca o produto no banco de dados
        var optional = rep.findById(id);
        if(optional.isPresent()) {
            var db = optional.get();
            // Copia as propriedades
            db.setNome(produto.getNome());
            db.setValorDeCompra(produto.getValorDeCompra());
            db.setValorDeVenda(produto.getValorDeVenda());
            db.setDescricao(produto.getDescricao());
            db.setSituacao(produto.getSituacao());
            db.setEstoque(produto.getEstoque());
            return ProdutoDTOResponse.create(rep.save(db));
        }
        return null;

    }

    public boolean delete(Long id) {
        var optional = rep.findById(id);
        if(optional.isPresent()) {
            rep.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
