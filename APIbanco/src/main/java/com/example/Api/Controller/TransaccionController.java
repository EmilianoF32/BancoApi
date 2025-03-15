package com.example.Api.Controller;


import com.example.Api.Model.Cuenta;
import com.example.Api.Service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Api.Dto.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transacciones")
public class TransaccionController {
    @Autowired
    private TransaccionService transaccionService;

    // ✅ Endpoint para realizar un ingreso (depósito)
    @PostMapping("/ingreso")
    public ResponseEntity<String> realizarIngreso(@RequestBody IngresoRequest request) {
        try {
            transaccionService.realizarIngreso(request.getNumeroTarjeta(), request.getMonto());
            return ResponseEntity.ok("Ingreso realizado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al realizar el ingreso: " + e.getMessage());
        }
    }

    // ✅ Endpoint para realizar un retiro
    @PostMapping("/retiro")
    public ResponseEntity<String> realizarRetiro(@RequestBody RetiroRequest request) {
        try {
            transaccionService.realizarRetiro(request.getNumeroTarjeta(), request.getMonto());
            return ResponseEntity.ok("Retiro realizado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al realizar el retiro: " + e.getMessage());
        }
    }

    // ✅ Endpoint para realizar una transferencia
    @PostMapping("/transferencia")
    public ResponseEntity<String> realizarTransferencia(@RequestBody TransferenciaRequest request) {
        try {
            transaccionService.realizarTransferencia(request.getNumeroTarjetaOrigen(), request.getNumeroTarjetaDestino(), request.getMonto());
            return ResponseEntity.ok("Transferencia realizada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al realizar la transferencia: " + e.getMessage());
        }
    }
}
