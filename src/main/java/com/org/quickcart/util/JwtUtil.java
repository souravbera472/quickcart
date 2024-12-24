package com.org.quickcart.util;

import com.org.quickcart.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRETKEY = "e5d01517280aabf56b0b4849e3bed7b1ea4730617c920ab6579cdee3a842fc43462940e2f43ee79a5b7e02a81ac9c0563deee916eb56f431784512c5bf460af0f686ef2f82d10790ee20cf3f8d92d9168f023cab2a8f94b593aa67a1f08d0eea63227e8305b12333e328076ca4f51c1a308bdef1d45a9d4a52d180a9cfb510ec23c543829ed815913eeb6580b7cbf427cba7b5c56b3546627e099046915da8d705b0339a522b02982b0b5ce50ef988e3f664afdb82283cd2b37f7419318647824d1e19bfdff84e4d82b5f006cc797c5f1dcb2081cb3e0e7a2d77cc4549343d73e2bfb0ffeefa770e106ce2239846ed81240963fafd0afe7e0cfa4f7124eb1c12";

    public String generateToken(User user, String role){
        Map<String, Object> claims =  new HashMap<>();
        claims.put("role", role);

        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(user.getEmail())
                .issuer("QuickCart")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10*60*1000))
                .and()
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRETKEY));
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return (String) extractClaims(token, claims -> claims.get("role"));
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token){
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
