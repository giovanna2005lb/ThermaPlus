package br.com.fiap.ThermaPlus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.ThermaPlus.model.Usuario;
import br.com.fiap.ThermaPlus.service.TokenService.Token;


@RestController
public class AuthController {

    public record Credentials (String email, String password){}

    @Autowired
    private br.com.fiap.ThermaPlus.service.TokenService tokenService;

    @Autowired
    AuthenticationManager authManager;

    @PostMapping("/login")
    public Token login(@RequestBody Credentials credentials){
        var authentication = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());
        var user = (Usuario) authManager.authenticate(authentication).getPrincipal();

        return tokenService.createToken(user);
    }
    
}
