package com.project.springbootlibrary.config;

import com.okta.spring.boot.oauth.Okta;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.io.IOException;

@Configuration
public class SecurityConfiguration {
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    public SecurityConfiguration(CustomAuthenticationEntryPoint customAuthenticationEntryPoint){
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception{
        //Disable Cross Site forgery
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()).
        //Protect endpoints at /api/<type>/secure
        authorizeHttpRequests(configurer ->
                configurer.requestMatchers("/api/**").permitAll().anyRequest().authenticated())


                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

//        http.cors(Customizer.withDefaults());

        //Add content negotiation strategy
//        http.setSharedObject(ContentNegotiationStrategy.class , new HeaderContentNegotiationStrategy());

        //Force a non-empty response Body for 401's to make the response friendly
//        Okta.configureResourceServer401ResponseBody(http);
        return http.build();
    }


}
