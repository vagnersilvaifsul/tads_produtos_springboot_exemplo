package com.example.produtos.api.infra.security;

import com.example.produtos.api.infra.security.jwt.JwtAuthenticationFilter;
import com.example.produtos.api.infra.security.jwt.JwtAuthorizationFilter;
import com.example.produtos.api.infra.security.jwt.handler.AccessDeniedHandler;
import com.example.produtos.api.infra.security.jwt.handler.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) //habilita a Security por método no(s) controller(s)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private UnauthorizedHandler unauthorizedHandler;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //Configuração JWT Authentication
        AuthenticationManager authManager = authenticationManager();

        http
            .authorizeRequests()//Qualquer request requer autenticação
            .antMatchers(HttpMethod.POST, "/api/v1/login").permitAll() //porém, esse path é exceção
            .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
            .permitAll()
            .anyRequest().authenticated() //qualquer requeste deve ser autenticada
            .and().csrf().disable() //desabilita o controle de ataques CSRF
            .addFilter(new JwtAuthenticationFilter(authManager)) //filtro de autenticação do JWT
            .addFilter(new JwtAuthorizationFilter(authManager, userDetailsService)) //filtro de autorização do JWT
            .exceptionHandling() //adicionar os handlers de exceção
            .accessDeniedHandler(accessDeniedHandler) //handler de acesso negado
            .authenticationEntryPoint(unauthorizedHandler) //handler de autorização negada
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //desliga os cookies da sessão


        //Configuração para Basic Authentication
//        http.authorizeRequests()
//            .anyRequest().authenticated()
//            .and().httpBasic()
//            .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        //autenticação utilizando UserDetailService
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);

//        //autenticação em memória
//        auth
//            // enable in memory based authentication with a user named "user" and "admin"
//            .inMemoryAuthentication().passwordEncoder(encoder)
//                .withUser("user").password(encoder.encode("user")).roles("USER")
//                .and()
//                .withUser("admin").password(encoder.encode("admin")).roles("USER", "ADMIN");
    }
}
