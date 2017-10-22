package com.impaq.training.spring.webfluxexamples.ex09security;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.web.server.HttpSecurity.http;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
@EnableWebFluxSecurity
public class SecureApplication {

    static final String BILLING_RESOURCE = "/ex09/billing";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";

    public static void main(String[] args) {
        SpringApplication.run(SecureApplication.class, args);
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ReactiveAuthenticationManager manager){
        return http().authenticationManager(manager)
            .httpBasic()
        .and()
            .authorizeExchange()
                .pathMatchers(POST, BILLING_RESOURCE).hasRole(ROLE_ADMIN)
                .pathMatchers(HttpMethod.GET, BILLING_RESOURCE + "/*").hasRole(ROLE_USER)
                .anyExchange().denyAll()
        .and()
                .build();
    }

    @Bean
    UserDetailsRepository userDetailsRepository(){
        User admin = new User("lukasz", "s3cret", Collections.singleton(new SimpleGrantedAuthority(String.format("ROLE_%s", ROLE_ADMIN))));
        User user = new User("michal", "password",Collections.singleton(new SimpleGrantedAuthority(String.format("ROLE_%s", ROLE_USER)) ));
        return new MapUserDetailsRepository(admin, user);
    }

    @Bean
    ReactiveAuthenticationManager userDetailsRepositoryAuthenticationManager(UserDetailsRepository repository){
        return new UserDetailsRepositoryAuthenticationManager(repository);
    }

}
