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
import com.example.Api.Dto.CuentaDto;
import com.example.Api.Dto.TransaccionDto;
import com.example.Api.Dto.CuentaTransaccionesResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import com.example.Api.Model.Transacciones;
import java.math.BigDecimal;
import com.example.Api.Service.TransaccionService;


@RestController
@RequestMapping("/Admin")
public class AdministradorController {

    @Autowired
    private CuentaService cuentaService;
    @Autowired
    private TransaccionService transaccionService;
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
            String token = request.getHeader("Authorization").substring(7);
            String role = jwtUtil.extraerRole(token);

            if (!"ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden ver todas las cuentas.");
            }

            List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
            List<CuentaDto> cuentasDto = cuentas.stream()
                    .map(cuenta -> {
                        BigDecimal saldo = (cuenta.getSaldoCuenta() != null) 
                                ? cuenta.getSaldoCuenta().getSaldo() 
                                : BigDecimal.ZERO;

                        // Usamos el role y el tipoCuenta correctamente
                        return new CuentaDto(
                                cuenta.getNombre(),
                                cuenta.getApellidoPaterno(),
                                cuenta.getApellidoMaterno(),
                                cuenta.getCorreoElectronico(),
                                cuenta.getNumeroTarjeta(),
                                cuenta.getTipoCuenta(),  // TipoCuenta aquí
                                cuenta.getRole(),        // Role aquí
                                saldo
                        );
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(cuentasDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/AdminTransacciones/{numeroTarjeta}")
    public ResponseEntity<?> obtenerTransaccionesPorCuenta(@PathVariable String numeroTarjeta, HttpServletRequest request) {
    	try {
            String token = request.getHeader("Authorization").substring(7);
            String role = jwtUtil.extraerRole(token);

            if (!"ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden ver el historial de transacciones.");
            }

            Optional<Cuenta> cuentaOpt = cuentaService.buscarPorNumeroTarjeta(numeroTarjeta);
            if (cuentaOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada.");
            }

            Cuenta cuenta = cuentaOpt.get();
            List<Transacciones> transacciones = transaccionService.obtenerTransaccionesPorCuenta(numeroTarjeta);

            // Mapear entidades a DTO
            List<TransaccionDto> transaccionesDto = transacciones.stream()
            	    .map(tx -> new TransaccionDto(
            	            tx.getMonto(),
            	            tx.getTipoTransaccion(),
            	            tx.getFecha(),
            	            tx.getCuentaDestinoNumeroTarjeta()
            	    ))
            	    .toList();
            // Crear respuesta con DTO
            CuentaTransaccionesResponseDTO respuestaDTO = new CuentaTransaccionesResponseDTO(
                    cuenta.getNumeroTarjeta(),
                    cuenta.getNombre() + " " + cuenta.getApellidoPaterno(),
                    transaccionesDto
            );
            return ResponseEntity.ok(respuestaDTO);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @PutMapping("/deshabilitar/{numeroTarjeta}")
    public ResponseEntity<?> deshabilitarCuenta(@PathVariable String numeroTarjeta, @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String role = jwtUtil.extraerRole(jwt);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden deshabilitar cuentas.");
        }

        boolean deshabilitado = cuentaService.deshabilitarCuenta(numeroTarjeta);
        if (deshabilitado) {
            return ResponseEntity.ok("La cuenta ha sido deshabilitada exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada.");
        }
    }
    
    @GetMapping("/Atransacciones/filtrar")
    public ResponseEntity<?> obtenerTransaccionesConFiltros(
            @RequestParam String numeroTarjeta,
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String tipoTransaccion,
            @RequestHeader("Authorization") String token) {

        String role = jwtUtil.extraerRole(token.replace("Bearer ", ""));

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden ver el historial de transacciones.");
        }

        List<Transacciones> transacciones = transaccionService.obtenerTransaccionesConFiltros(
                numeroTarjeta, fecha, tipoTransaccion);

        List<TransaccionDto> transaccionesDto = transacciones.stream()
                .map(tx -> new TransaccionDto(
                        tx.getMonto(),
                        tx.getTipoTransaccion(),
                        tx.getFecha(),
                        tx.getCuentaDestinoNumeroTarjeta()))
                .toList();

        return ResponseEntity.ok(transaccionesDto);
    }
}