package pl.java.scalatech.security.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@Primary
public class MemoryTokenService implements TokenService {

    @Value("${app.token.secret}")
    private String secret;

    @Override
    public String getUsernameFromToken(String token) {
        String username;
        try {
            username = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        return (getUsernameFromToken(token).equals(userDetails.getUsername()));
    }

    @Override
    public void refreshToken(String token) {
    }
}