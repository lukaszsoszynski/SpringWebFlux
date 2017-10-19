package com.impaq.training.spring.webfluxexamples.ex09security;

import static org.springframework.security.config.web.server.HttpSecurity.http;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.server.WebFilter;

import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableWebFluxSecurity
public class SecureApplication {

    static final String BILLING_RESOURCE = "/ex09/billing";
    private static final String ROLE_ADMIN = "ADMIN";

    public static void main(String[] args) {
        SpringApplication.run(SecureApplication.class, args);
    }

    @Bean
    Flux<WebFilter> springSecurityFilterChain(ReactiveAuthenticationManager manager) throws Exception {
        // FIXME use BeanPostProcessor to set the manager
        return http().authenticationManager(manager)
            .httpBasic()
        .and()
            .authorizeExchange()
                .pathMatchers(BILLING_RESOURCE).hasRole(ROLE_ADMIN)
                .anyExchange().authenticated()
        .and()
                .build()
                .getWebFilters();
    }

    @Bean
    UserDetailsRepository userDetailsRepository(){
        User user = new User("lukasz", "s3cret", Collections.singleton(new SimpleGrantedAuthority(ROLE_ADMIN)));
        return new MapUserDetailsRepository(user);
    }

    @Bean
    ReactiveAuthenticationManager userDetailsRepositoryAuthenticationManager(UserDetailsRepository repository){
        return new UserDetailsRepositoryAuthenticationManager(repository);
    }

//    @Bean
//    AuthorizationWebFilter authorizationWebFilter(ReactiveAuthorizationManager<? super ServerWebExchange> accessDecisionManager){
//        return new AuthorizationWebFilter(accessDecisionManager);
//    }
}
