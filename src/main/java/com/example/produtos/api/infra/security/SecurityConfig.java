package com.example.produtos.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //indica para o spring que esta é uma classe de configuração
@EnableWebSecurity //indica para o spring que esta classe irá personalizar as configurações de segurança
@EnableMethodSecurity(securedEnabled = true) //controle de acesso por anotação em métodos
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Configuração JWT Authentication
        http
            .csrf(csrf -> csrf.disable()) //desabilita a proteção contra ataques Cross-site Request Forger
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //sem sessão (desabilita o stateful)
            .authorizeHttpRequests(req -> {  //configurar a autorização
                req.requestMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll(); //exceto, a rota de documentação (para doc em html no navegador; e para ferramentas automatizadas de geração de código)
                req.requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll(); //exceto, a rota de login
                req.anyRequest().authenticated(); //demais rotas devem ser autenticadas
            })
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); //manda o filter do projeto vir antes do filter do Spring


//        //Basic Authentication
//        http
//            .csrf().disable()
//            .authorizeHttpRequests()
//                .anyRequest().authenticated()
//            .and()
//                .httpBasic(Customizer.withDefaults())
//            .userDetailsService(userDetailsService);

        return http.build();
    }

}
