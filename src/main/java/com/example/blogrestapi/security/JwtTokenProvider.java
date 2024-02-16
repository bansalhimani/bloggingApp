package com.example.blogrestapi.security;

import com.example.blogrestapi.exception.BlogApiException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-in-ms}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication){
        String username=authentication.getName();
        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+jwtExpirationDate);
        String token= Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
        return token;
    }

    private Key getSecretKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith((SecretKey) getSecretKey()).build()
                    .parse(token);
            return true;
        }
        catch (MalformedJwtException malformedJwtException){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Invalid JWT token");
        }
        catch (ExpiredJwtException expiredJwtException){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Expired JWT token");
        }
        catch (UnsupportedJwtException unsupportedJwtException){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Unsupported JWT token");
        }
        catch (IllegalArgumentException illegalArgumentException){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"JWT claims string is empty");
        }

    }

}
