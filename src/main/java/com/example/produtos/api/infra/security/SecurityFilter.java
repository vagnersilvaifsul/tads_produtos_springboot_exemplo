package com.example.produtos.api.infra.security;

import com.example.produtos.api.usuarios.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //Uma classe genérica que faz algum serviço de infraestrutura
//OncePerRequestFilter (do Spring, executada uma única vez por request) implementa a classe Filter
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //recupera o token do request
        var tokenJWT = recuperarToken(request);
        //valida o token
        if(tokenJWT != null){ //se a rota tem o token (no caso, qualquer rota, exceto /login)
            var subject = tokenService.getSubject(tokenJWT);
            //autentica o subject (usuário que fez login, pois o Spring está configurado como Stateless)
            var usuario = repository.findByUsuario(subject); //recupera o objeto usuário
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); //DTO do Spring para o usuário que acessa o sistema
            SecurityContextHolder.getContext().setAuthentication(authentication); //força a autenticação no Spring
        }

        //Não esquecer de chamar o próximo filtro (que pode significar simplesmente entregar a request ou response)
        filterChain.doFilter(request, response); //chama o próximo filter na cadeia de filters do Spring (ou do Servlet)
    }

    private String recuperarToken(HttpServletRequest request) {
        var autorizationHeader = request.getHeader("Authorization");
        if (autorizationHeader != null) { //só recupera o token de rotas que o tem
            return autorizationHeader.replace("Bearer ", "");
        }
        return null; //senão retorna null
    }
}
