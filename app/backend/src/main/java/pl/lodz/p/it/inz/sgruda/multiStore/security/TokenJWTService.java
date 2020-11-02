package pl.lodz.p.it.inz.sgruda.multiStore.security;

import io.jsonwebtoken.*;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth.jwt.TokenJWTHasBeenExpiredException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashGenerator;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Log
@Component
public class TokenJWTService {

    @Value("${app.auth.jwt.secret}")
    private String jwtSecret;

    @Value("${app.auth.jwt.expiration.mili-sec}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        HashGenerator hashGenerator = new HashGenerator();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", hashGenerator.hash(userPrincipal.getId()));
        claims.put("email", userPrincipal.getEmail());
        claims.put("roles", userPrincipal.getAuthorities().stream()
                                         .map(GrantedAuthority::getAuthority)
                                         .collect(Collectors.toSet()));
        claims.put("iss", now);
        claims.put("exp", expiryDate);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserHashIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email").toString();
    }
    private long getExpiryDateMiliSec(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.get("exp").toString());
    }

    public boolean validateToken(String authToken) throws TokenJWTHasBeenExpiredException {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            Date now = new Date();
            if(getExpiryDateMiliSec(authToken) <= now.getTime()) {
                throw new TokenJWTHasBeenExpiredException();
            }
            return true;
    }
}
