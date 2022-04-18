package com.example.produtos.api.produtos;

import com.example.produtos.api.infra.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository rep;

    public List<ProdutoDTO> getProdutos() {
        return rep.findAll().stream().map(ProdutoDTO::create).collect(Collectors.toList());
    }

    public ProdutoDTO getProdutoById(Long id) {
        Optional<Produto> produto = rep.findById(id);
        return produto.map(ProdutoDTO::create).orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado"));
    }

    public List<ProdutoDTO> getProdutosByNome(String nome) {
        return rep.findByNome(nome).stream().map(ProdutoDTO::create).collect(Collectors.toList());
    }

    public ProdutoDTO insert(Produto produto) {
        Assert.isNull(produto.getId(),"Não foi possível inserir o registro");

        return ProdutoDTO.create(rep.save(produto));
    }

    public ProdutoDTO update(Produto produto, Long id) {
        Assert.notNull(id,"Não foi possível atualizar o registro");

        // Busca o produto no banco de dados
        Optional<Produto> optional = rep.findById(id);
        if(optional.isPresent()) {
            Produto db = optional.get();
            // Copiar as propriedades
            db.setNome(produto.getNome());
            db.setDescricao(produto.getDescricao());
            db.setValor(produto.getValor());
            db.setEstoque(produto.getEstoque());
            db.setSituacao(produto.getSituacao());
            System.out.println("Produto id " + db.getId());

            // Atualiza o produto
            rep.save(db);

            return ProdutoDTO.create(db);
        } else {
            return null;
            //throw new RuntimeException("Não foi possível atualizar o registro");
        }
    }

    public void delete(Long id) {
        rep.deleteById(id);
    }
}
