package com.example.produtos.api.usuario;

import com.example.produtos.api.servicos.mail.EmailService;
import com.example.produtos.api.usuario.validacoes.ValidacaoCadastroDeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de serviço de dados
public class UsuarioService {

    private UsuarioRepository rep;

    //indica ao Spring Boot que ele deve injetar estas dependências para a classe funcionar
    public UsuarioService(UsuarioRepository rep){
        this.rep = rep;
    }

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private TokenConfirmacaoEmailRepository tokenConfirmacaoEmailRepository;

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private EmailService emailService;

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private List<ValidacaoCadastroDeUsuario> validacoes;

    public Usuario insert(Usuario usuario) {
        Assert.isNull(usuario.getId(),"Não foi possível inserir o registro");

        //validações
        validacoes.forEach(v -> v.validar(usuario));

        var usuarioSalvo = rep.save(usuario);
        var tokenConfirmacaoEmail = new TokenConfirmacaoEmail(usuarioSalvo);
        tokenConfirmacaoEmailRepository.save(tokenConfirmacaoEmail);

        //envia email
        emailService.enviarEmail(
            usuario.getEmail(),
            "Solicitação de cadastro no app Aulas TADS",
            "Olá, " + usuario.getNome() + " " + usuario.getSobrenome()
                +"\n\nAgora que você se cadastrou no app Aulas TADS, com o email " + usuario.getEmail()
                + " é necessário confirmá-lo, clicando no link a a seguir:"
                + "\nhttp://localhost:8080/confirmar-email?token=" + tokenConfirmacaoEmail.getToken());

        return usuarioSalvo;
    }

    public boolean confirmarEmail(String TokenDeConfirmacao) {
        var token = tokenConfirmacaoEmailRepository.findByToken(TokenDeConfirmacao);
        if(token != null)
        {
            var usuario = rep.findByEmail(token.getUsuario().getEmail());
            usuario.setConfirmado(true);
            rep.save(usuario);
            return true;
        }
        return false;
    }
}
