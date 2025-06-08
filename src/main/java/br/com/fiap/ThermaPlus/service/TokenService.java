package br.com.fiap.ThermaPlus.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.fiap.ThermaPlus.model.Usuario;
import br.com.fiap.ThermaPlus.repository.UsuarioRepository;

@Service
public class TokenService {

    private Algorithm algorithm = Algorithm.HMAC256("secret");

    @Autowired
    private UsuarioRepository usuarioRepository;

    public record Token(String token, String email) {}

    public Token createToken(Usuario user) {
        Instant expiresAt = LocalDateTime.now().plusMinutes(120).toInstant(ZoneOffset.ofHours(-3));
        var jwt = JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("email", user.getEmail())
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return new Token(jwt, user.getEmail());
    }

    public Usuario getUserFromToken(String token) {
        var verifiedToken = JWT.require(algorithm).build().verify(token);
        String userIdStr = verifiedToken.getSubject();
        Long userId = Long.valueOf(userIdStr);

        return usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no token"));
    }
}
