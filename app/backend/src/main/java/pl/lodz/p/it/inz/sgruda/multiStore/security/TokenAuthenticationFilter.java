package pl.lodz.p.it.inz.sgruda.multiStore.security;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.auth.jwt.TokenJWTHasBeenExpiredException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Log
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenJWTService tokenService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
                String hashId = tokenService.getUserHashIdFromJWT(jwt);
                String email = tokenService.getEmailFromJWT(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserByEmailAndCheckId(email, hashId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info(LocalDateTime.now() + " User: " + userDetails.getUsername() + " IP: " +
                        request.getRemoteAddr() + " used JWT: " + jwt);

            }
        } catch(TokenJWTHasBeenExpiredException ex) {
            log.severe("Error: " + ex);
        } catch (Exception ex) {
            log.severe("Could not set user authentication in security context " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}