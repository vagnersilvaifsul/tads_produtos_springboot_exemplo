package com.example.produtos.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean de Configuração
@EnableWebSecurity //indica para o spring que esta classe irá personalizar as configurações de segurança
@EnableMethodSecurity(securedEnabled = true) //controle de acesso por anotação em métodos
public class SecurityConfig {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private SecurityFilter securityFilter;


    @Bean //indica ao spring boot que essa configuração deve ser adicionada ao contexto do aplicativo
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean //indica ao spring boot que essa configuração deve ser adicionada ao contexto do aplicativo
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //indica ao spring boot que essa configuração deve ser adicionada ao contexto do aplicativo
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Configuração JWT Authentication
        http
            .csrf(csrf -> csrf.disable()) //desabilita a proteção contra ataques Cross-site Request Forger, comum em conexões Stateless
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //sem sessão (desabilita o stateful)
            .authorizeHttpRequests(req -> {  //configurar a autorização
                req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll(); //exceto, a rota de documentação (para doc em html no navegador; e para ferramentas automatizadas de geração de código)
                req.requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll(); //exceto, a rota de login
                req.anyRequest().authenticated(); //demais rotas devem ser autenticadas
            })
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); //manda o filter do projeto vir antes do filter do Spring

        //Esta é a configuração para Basic Authentication (vimos no início do conteúdo sobre Spring Security)
        //Basic Authentication
//        http
//            .csrf(csrf -> csrf.disable()) //desabilita a proteção contra ataques Cross-site Request Forger
//            .authorizeHttpRequests(req -> {
//                req.requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll(); //exceto, a rota de login
//                req.anyRequest().authenticated(); //demais rotas devem ser autenticadas
//            })
//            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    //Este método é utilizado para autenticar em memória (vimos isso em Basic Authentication, antes de criar as tabelas da feat usuarios, para autheticação por bando de dados)
//    @Bean //indica ao spring boot que essa configuração deve ser adicionada ao contexto do aplicativo
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user = User.withUsername("user")
//            .password(passwordEncoder.encode("user"))
//            .roles("USER")
//            .build();
//
//        UserDetails admin = User.withUsername("admin")
//            .password(passwordEncoder.encode("admin"))
//            .roles("USER", "ADMIN")
//            .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

}
