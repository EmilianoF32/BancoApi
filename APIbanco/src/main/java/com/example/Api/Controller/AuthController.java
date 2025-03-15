package com.example.Api.Controller;

import com.example.Api.Model.Cuenta;
import com.example.Api.Segurity.JwtUtil;
import com.example.Api.Service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
    private CuentaService cuentaService;

    @Autowired
    private JwtUtil jwtUtil; // Para generar tokens

    // ✅ Registro de usuario
    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody Cuenta cuenta) {
        Cuenta nuevaCuenta = cuentaService.registrarCuenta(cuenta);
        String token = jwtUtil.generarToken(nuevaCuenta.getNumeroTarjeta(), nuevaCuenta.getRole().getName().name());
        //String token = jwtUtil.generarToken(cuenta.getNumeroTarjeta(), cuenta.getRole().getName().name());
        return ResponseEntity.ok(Map.of("token", token, "cuenta", nuevaCuenta));
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String numeroTarjeta = credenciales.get("numeroTarjeta");
        String password = credenciales.get("password");

        System.out.println("Login solicitado para: " + numeroTarjeta); // Agrega un log aquí

        Cuenta cuenta = cuentaService.autenticarCuenta(numeroTarjeta, password);

        if (cuenta != null) {
            //String token = jwtUtil.generarToken(numeroTarjeta);
        	String token = jwtUtil.generarToken(cuenta.getNumeroTarjeta(), cuenta.getRole().getName().name());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
    }
    
}
