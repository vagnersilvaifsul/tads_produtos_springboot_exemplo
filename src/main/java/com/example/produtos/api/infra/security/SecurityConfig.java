package com.example.produtos.api.infra.security;


import com.example.produtos.api.infra.security.jwt.JwtAuthenticationFilter;
import com.example.produtos.api.infra.security.jwt.JwtAuthorizationFilter;
import com.example.produtos.api.infra.security.jwt.handler.AccessDeniedHandler;
import com.example.produtos.api.infra.security.jwt.handler.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private UnauthorizedHandler unauthorizedHandler;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;


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
        final AuthenticationManager authManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        http
            .authorizeRequests()//Qualquer request requer autenticação
                .antMatchers(HttpMethod.POST, "/api/v1/login").permitAll() //porém, esse path é exceção
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
                .permitAll()
                .anyRequest().authenticated() //qualquer requeste deve ser autenticada
            .and()
                .csrf().disable() //desabilita o controle de ataques CSRF
                .addFilter(new JwtAuthenticationFilter(authManager)) //filtro de autenticação do JWT
                .addFilter(new JwtAuthorizationFilter(authManager, userDetailsService)) //filtro de autorização do JWT
            .exceptionHandling() //adicionar os handlers de exceção
                .accessDeniedHandler(accessDeniedHandler) //handler de acesso negado
                .authenticationEntryPoint(unauthorizedHandler) //handler de autorização negada
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //desliga os cookies da sessão


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
