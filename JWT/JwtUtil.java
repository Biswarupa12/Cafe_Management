package com.cafe.cafe_management.JWT;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


public class JwtUtil {

    private String secret = "Signature";


    //expiration of token
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration); 
    }


    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

     public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignKey() {
		byte[]keyBytes=Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

    public Claims extractAllClaims(String token){
        // return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
}

    private Boolean isTokenExpiration(String token){
           return extractExpiration(token).before(new Date()); 
}

    public String generateToken(String Username,String role){
        Map<String,Object> claims= new HashMap<>();
        claims.put("role", role);
        //  createToken(claims, username);
       return  createToken(claims, Username);
    }
 
    private String createToken(Map<String,Object> claims , String subject){
        return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000*60*5))
                    .signWith(SignatureAlgorithm.HS256, secret).compact();
    }


    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpiration(token));
    }

}