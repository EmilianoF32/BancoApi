package com.example.Api.Controller;

import com.example.Api.Model.Cuenta;
import com.example.Api.Segurity.JwtUtil;
import com.example.Api.Service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.http.HttpStatus;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Admin")
public class AdministradorController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private JwtUtil jwtUtil; // Inyectamos JwtUtil en lugar de acceder a métodos estáticos

    // Endpoint para registrar un administrador
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarAdministrador(@RequestBody Cuenta cuenta, HttpServletRequest request) {
        try {
            // Extraer el token del encabezado Authorization
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado o inválido.");
            }

            String token = authHeader.substring(7); // Removemos "Bearer "
            String role = jwtUtil.extraerRole(token); // Obtener el rol desde el token

            // Validar que solo los administradores puedan crear otros administradores
            if (!"ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden crear otros administradores.");
            }

            // Registrar administrador
            Cuenta cuentaRegistrada = cuentaService.registrarAdministrador(cuenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(cuentaRegistrada);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    // Endpoint para listar todas las cuentas
    @GetMapping("/cuentas")
    public ResponseEntity<?> obtenerTodasLasCuentas(HttpServletRequest request) {
        try {
            // Extraer el token del header
            String token = request.getHeader("Authorization").substring(7);
            String role = jwtUtil.extraerRole(token);

            if (!"ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden ver todas las cuentas.");
            }
            // Obtener lista de cuentas
            List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
            return ResponseEntity.ok(cuentas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}