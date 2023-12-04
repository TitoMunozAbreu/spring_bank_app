package com.example.config;

import com.example.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationProviderImpl implements AuthenticationProvider {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PasswordEncoder encoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //datos del request
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        UserDetails user = customerService.loadUserByUsername(username);
        String password = user.getPassword();

        //comprobar si el password es valido
        if(!isValidPassword(rawPassword,password)){
            throw new BadCredentialsException("Invalid username and password");
        }

        return UsernamePasswordAuthenticationToken.authenticated(
                user,
                user.getPassword(),
                user.getAuthorities()
        );
    }

    private boolean isValidPassword(String rawPassword, String password) {
        return encoder.matches(rawPassword,password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
