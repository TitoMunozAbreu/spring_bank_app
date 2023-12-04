package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
public class SecurityConfig {
    @Autowired
    private PasswordConfig encoder;
    final String[] WHITELIST = {
            "/",
            "/home",
            "/images/**",
            "/register"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(WHITELIST).permitAll()
                            .requestMatchers("/admin").hasRole("ADMIN")
                            .requestMatchers("/customer").hasRole("USER")
                            .anyRequest().authenticated())
                .formLogin(form ->
                        form.loginPage("/login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/home")
                                .permitAll())
                .logout(logout ->
                        logout.logoutUrl("/logout")
                                .permitAll());

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login"); // Redirigir a /login en caso de fallo
    }
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
        UserDetails admin = User.builder()
                .username("admin@mail.com")
                .password(encoder.passwordEncoder().encode("123"))
                .roles("ADMIN")
                .build();
        UserDetails user = User.builder()
                .username("papaya@mail.com")
                .password(encoder.passwordEncoder().encode("123"))
                .roles("USER")
                .build();

        detailsManager.createUser(admin);
        detailsManager.createUser(user);

        return detailsManager;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new AuthenticationProviderImpl();
    }

}
