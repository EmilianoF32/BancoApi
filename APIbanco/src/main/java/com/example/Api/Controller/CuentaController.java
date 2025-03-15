package com.example.Api.Controller;

import com.example.Api.Model.Cuenta;
import java.math.BigDecimal;
import com.example.Api.Service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.example.Api.Model.SaldoCuenta;
import com.example.Api.Dto.CuentaDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.example.Api.Segurity.JwtUtil;
import com.example.Api.Model.Roles;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
	
	@GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("La API está funcionando correctamente");
    }
    @Autowired
    private CuentaService cuentaService;

    // Endpoint para obtener información de una cuenta por número de tarjeta
    @GetMapping("/{numeroTarjeta}")
    public ResponseEntity<?> obtenerCuentaPorNumeroTarjeta(@PathVariable String numeroTarjeta, @RequestHeader("Authorization") String token) {
    	JwtUtil jwtUtil = new JwtUtil();
    	String usuarioActual = jwtUtil.extraerNumeroTarjeta(token.replace("Bearer ", ""));
    	Optional<Cuenta> cuentaOpt = cuentaService.buscarPorNumeroTarjeta(numeroTarjeta, usuarioActual);
        // Verificamos si la cuenta existe
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();

            // Obtener el saldo asociado a la cuenta
            BigDecimal saldo = cuenta.getSaldoCuenta() != null ? cuenta.getSaldoCuenta().getSaldo() : BigDecimal.ZERO;

            // Crear el DTO con los datos de la cuenta y el saldo
            CuentaDto cuentaDto = new CuentaDto(
                cuenta.getNombre(),
                cuenta.getApellidoPaterno(),
                cuenta.getApellidoMaterno(),
                cuenta.getCorreoElectronico(),
                cuenta.getNumeroTarjeta(),
                cuenta.getTipoCuenta(),
                cuenta.getRole(),
                saldo  // Añadir el saldo de la cuenta
            );

            return ResponseEntity.ok(cuentaDto);  // Retornar ResponseEntity con el DTO
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Si no se encuentra la cuenta, retornamos un 404
        }
    }

    // Manejo de errores de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}