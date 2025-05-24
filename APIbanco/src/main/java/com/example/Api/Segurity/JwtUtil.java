package com.example.Api.Segurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "U29tZVNlY3JldEtleVRoYXQxU01vcmVTaWN1cmUhIQ=="; // ¡Cámbialo en producción!

    // ✅ Generar token JWT
    public String generarToken(String numeroTarjeta, String role) {
        return Jwts.builder()
                .setSubject(numeroTarjeta)
                .claim("role", role)  // ✅ Pasamos el rol como parámetro
                .setIssuedAt(new Date())  
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) 
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) 
                .compact();
        /*return Jwts.builder()
                .setSubject(numeroTarjeta)
                .setIssuedAt(new Date())  // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // 🔹 Convertir a bytes
                .compact();*/
    }

    // ✅ Obtener usuario desde token
    public String extraerNumeroTarjeta(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) // 🔹 Convertir a bytes	
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    // ✅ Extraer el rol desde el token
    public String extraerRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) // 🔹 Convertir a bytes
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class); // Aquí extraemos el valor del "role" desde los claims
    }
    
}