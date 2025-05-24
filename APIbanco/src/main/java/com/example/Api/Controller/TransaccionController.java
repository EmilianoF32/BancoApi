package com.example.Api.Controller;


import com.example.Api.Model.Cuenta;
import com.example.Api.Service.CuentaService;
import com.example.Api.Service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Api.Dto.*;
import com.example.Api.Model.Transacciones;
import com.example.Api.Repository.TransaccionesRepository;
import java.math.BigDecimal;
import com.example.Api.Segurity.JwtUtil;

@RestController
@RequestMapping("/transacciones")
public class TransaccionController {
    @Autowired
    private TransaccionService transaccionService;
    @Autowired
    private CuentaService cuentaService;  // servicio CuentaService
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TransaccionesRepository transaccionesRepository;

    @PostMapping("/ingreso")
    public ResponseEntity<String> realizarIngreso(@RequestBody IngresoRequest request, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extraemos el token del encabezado Authorization
            String token = authorizationHeader.replace("Bearer ", "");
            // Extraemos el número de tarjeta desde el token
            String numeroTarjeta = jwtUtil.extraerNumeroTarjeta(token);
            // Llamamos al servicio pasando el número de tarjeta y el monto
            transaccionService.realizarIngreso(numeroTarjeta, request.getMonto());

            return ResponseEntity.ok("Ingreso realizado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al realizar el ingreso: " + e.getMessage());
        }
    }

    // ✅ Endpoint para realizar un retiro
    @PostMapping("/retiro")
    public ResponseEntity<String> realizarRetiro(@RequestBody RetiroRequest request, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String numeroTarjeta = jwtUtil.extraerNumeroTarjeta(token);
            transaccionService.realizarRetiro(numeroTarjeta, request.getMonto());
            
            return ResponseEntity.ok("Retiro realizado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al realizar el retiro: " + e.getMessage());
        }
    }

    // ✅ Endpoint para realizar una transferencia
    @PostMapping("/transferencia")
    public ResponseEntity<String> realizarTransferencia(@RequestBody TransferenciaRequest request, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String numeroTarjetaOrigen = jwtUtil.extraerNumeroTarjeta(token);

            // Validar que la cuenta destino exista
            Cuenta cuentaDestino = cuentaService.buscarPorNumeroTarjeta(request.getNumeroTarjetaDestino())
                    .orElseThrow(() -> new RuntimeException("Cuenta de destino no encontrada"));

            // Lógica para realizar la transferencia
            transaccionService.realizarTransferencia(numeroTarjetaOrigen, request.getNumeroTarjetaDestino(), request.getMonto());

            return ResponseEntity.ok("Transferencia realizada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al realizar la transferencia: " + e.getMessage());
        }
    }
 // Endpoint para consultar transacciones de la cuenta autenticada
    @GetMapping("/historial")
    public ResponseEntity<?> obtenerHistorialTransacciones(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String numeroTarjeta = jwtUtil.extraerNumeroTarjeta(token);

            // Llamamos al servicio y transformamos la lista a DTO
            var historial = transaccionService.obtenerHistorialTransacciones(numeroTarjeta);

            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el historial de transacciones: " + e.getMessage());
        }
    }
}
