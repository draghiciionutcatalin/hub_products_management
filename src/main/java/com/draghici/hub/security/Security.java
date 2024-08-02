package com.draghici.hub.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Component
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        description = """
                    Basic authentication. \n
                    Role: Admin (username: admin, password: admin) \n
                    Role: User (username: user, password: user). \n
                    """
)
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Server API")})
public class Security {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var passwordEncoder = passwordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        var admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .authorities("ADMIN")
                .build();
        var user = User.withUsername("user")
                .password(passwordEncoder.encode("user"))
                .authorities("USER")
                .build();
        manager.createUser(admin);
        manager.createUser(user);

        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(ahr ->
                ahr.requestMatchers(
                        "/h2-console/**",
                        "/v3/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/v2/api-docs/**",
                        "/swagger-resources/**"
                ).permitAll()

                .requestMatchers(HttpMethod.GET, "/api/product").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/product/{id}").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/product").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/product/{id}").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/product/{id}").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
        ).httpBasic(withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}