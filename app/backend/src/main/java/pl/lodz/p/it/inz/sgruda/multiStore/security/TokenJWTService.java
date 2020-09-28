package pl.lodz.p.it.inz.sgruda.multiStore.security;

import io.jsonwebtoken.*;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

@Log
@Component
public class TokenJWTService {


    @Value("${app.auth.jwt.secret}")
    private String jwtSecret;

    @Value("${app.auth.jwt.expiration.mili-sec}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.severe("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.severe("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.severe("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.severe("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.severe("JWT claims string is empty.");
        }
        return false;
    }
}
