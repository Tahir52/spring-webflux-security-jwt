package com.tahir.webfluxjwt.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

import java.util.List;

@Configuration
@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Value("${jwt.jwks.url:http://localhost:8081/.well-known/jwks.json}")
    private final String jwksUrl;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .oauth2ResourceServer()
                .jwt()
                .jwtDecoder(NimbusReactiveJwtDecoder.withJwkSetUri(jwksUrl)
                        .jwsAlgorithms(algos -> algos.addAll(List.of(SignatureAlgorithm.values())))
                        .build())
                .and()
                .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .authorizeExchange()
                .pathMatchers("/public/**").permitAll()
                .pathMatchers("/secure/**").authenticated()
                .anyExchange().authenticated()
                .and()
                .build();
    }
}
