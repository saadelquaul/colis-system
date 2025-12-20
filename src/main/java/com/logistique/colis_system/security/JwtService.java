package com.logistique.colis_system.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(Authentication authentication) {
        try {
            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

            String role = userPrincipal.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.create()
                    .withIssuer("ColisApp")
                    .withSubject(userPrincipal.getUsername())
                    .withClaim("role", role)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erreur lors de la création du token : " + e.getMessage());
        }
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("ColisApp")
                    .build();

            return verifier.verify(token);

        } catch (TokenExpiredException e) {
            throw new BadCredentialsException("Le jeton a expiré. Veuillez vous reconnecter.");
        } catch (SignatureVerificationException e) {
            throw new BadCredentialsException("La signature du jeton est invalide (tampering détecté).");
        } catch (AlgorithmMismatchException e) {
            throw new BadCredentialsException("L'algorithme du jeton ne correspond pas.");
        } catch (Exception e) {
            throw new BadCredentialsException("Jeton JWT invalide : " + e.getMessage());
        }
    }

    public String getUsernameFromToken(String token) {
        return validateToken(token).getSubject();
    }
}