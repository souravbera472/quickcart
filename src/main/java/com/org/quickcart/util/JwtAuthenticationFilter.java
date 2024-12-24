package com.org.quickcart.util;

import com.org.quickcart.exception.CustomException;
import com.org.quickcart.exception.ErrorCode;
import com.org.quickcart.logger.QLogger;
import com.org.quickcart.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            QLogger.error("Provide token to accessing apis.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUserName(token);
            String role = jwtUtil.extractRole(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(email != null && authentication == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if(jwtUtil.isTokenValid(token, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (ExpiredJwtException e){
            QLogger.error("Token expired: " + e.getMessage());
            throw new CustomException(ErrorCode.SESSION_EXPIRED);
        } catch (JwtException e) {
            QLogger.error("Invalid token: " + e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        filterChain.doFilter(request, response);
    }
}
