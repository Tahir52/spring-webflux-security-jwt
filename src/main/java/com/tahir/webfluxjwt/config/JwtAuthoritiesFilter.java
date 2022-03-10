package com.tahir.webfluxjwt.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.ReactiveSecurityContextHolder.withSecurityContext;
import static reactor.core.publisher.Mono.just;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthoritiesFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return exchange.getPrincipal()
                .map(principal -> {
                    var authentication = (JwtAuthenticationToken) principal;
                    Jwt jwt = (Jwt) authentication.getCredentials();
                    Collection<GrantedAuthority> authorities = Optional.ofNullable(jwt.getClaimAsStringList("authorities"))
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    authorities.addAll(authentication.getAuthorities());
                    SecurityContextImpl securityContext = new SecurityContextImpl();
                    securityContext.setAuthentication(new JwtAuthenticationToken(jwt, authorities));
                    return securityContext;
                })
                .defaultIfEmpty(new SecurityContextImpl())
                .flatMap(context -> chain.filter(exchange)
                        .contextWrite(withSecurityContext(just(context))));
    }
}
