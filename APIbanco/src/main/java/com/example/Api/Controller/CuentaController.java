package com.example.Api.Controller;

import com.example.Api.Model.Cuenta;
import java.math.BigDecimal;
import com.example.Api.Service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.example.Api.Dto.CuentaDto;
import org.springframework.security.core.Authentication;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
	
	@GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("La API está funcionando correctamente");
    }
    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/mi-cuenta")
    public ResponseEntity<?> obtenerCuentaPropia(Authentication authentication) {
        String usuarioActual = authentication.getName();
        Optional<Cuenta> cuentaOpt = cuentaService.buscarPorNumeroTarjeta(usuarioActual);

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            BigDecimal saldo = cuenta.getSaldoCuenta() != null ? cuenta.getSaldoCuenta().getSaldo() : BigDecimal.ZERO;

            CuentaDto cuentaDto = new CuentaDto(
                cuenta.getNombre(),
                cuenta.getApellidoPaterno(),
                cuenta.getApellidoMaterno(),
                cuenta.getCorreoElectronico(),
                cuenta.getNumeroTarjeta(),
                cuenta.getTipoCuenta(),
                cuenta.getRole(),
                saldo
            );
            return ResponseEntity.ok(cuentaDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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