package com.example.Api.Service;


import com.example.Api.Model.Cuenta;
import com.example.Api.Model.SaldoCuenta;
import com.example.Api.Model.Transacciones;
import com.example.Api.Repository.CuentaRepository;
import com.example.Api.Repository.SaldoCuentaRepository;
import com.example.Api.Repository.TransaccionesRepository;
import com.example.Api.Segurity.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Api.Dto.TransaccionDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.stream.Collectors;


@Service
public class TransaccionService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private SaldoCuentaRepository saldoCuentaRepository;

    @Autowired
    private TransaccionesRepository transaccionesRepository;
   
    // Método para procesar un ingreso (depósito) en una cuenta

	    public void realizarIngreso(String numeroTarjeta, BigDecimal monto) {
	        System.out.println("Número de tarjeta extraído del token: " + numeroTarjeta);
	        // Buscar la cuenta asociada al número de tarjeta
	        Cuenta cuenta = cuentaRepository.findByNumeroTarjeta(numeroTarjeta);
	        if (cuenta != null && monto.compareTo(BigDecimal.ZERO) > 0) {
	            // Obtener saldo actual de la cuenta
	            Optional<SaldoCuenta> saldoCuentaOpt = saldoCuentaRepository.findByCuenta(cuenta);
	            SaldoCuenta saldoCuenta = saldoCuentaOpt.orElseGet(() -> new SaldoCuenta(cuenta, BigDecimal.ZERO));

	            // Actualizar el saldo
	            saldoCuenta.setSaldo(saldoCuenta.getSaldo().add(monto));
	            saldoCuentaRepository.save(saldoCuenta);

	            // Registrar la transacción
	            Transacciones transaccion = new Transacciones(cuenta, monto, "Depósito efectivo", null);
	            transaccionesRepository.save(transaccion);
	        } else {
	            throw new RuntimeException("Cuenta no encontrada o monto inválido.");
	        }
	        emailService.enviarCorreo(
	        	    cuenta.getCorreoElectronico(),
	        	    "Ingreso realizado correctamente",
	        	    "Se ha realizado un ingreso a tu cuenta.\n\n" +
	        	    "Monto: $" + monto + "\n" +
	        	    "Fecha: " + LocalDate.now() + "\n\n" +
	        	    "Gracias por usar nuestro banco."
	        	);
    }

    // Método para procesar un retiro de una cuenta
    public void realizarRetiro(String numeroTarjeta, BigDecimal monto) {
        Cuenta cuenta = cuentaRepository.findByNumeroTarjeta(numeroTarjeta); // Verificar si el número de tarjeta existe
        if (cuenta != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            // Obtener saldo actual de la cuenta
            Optional<SaldoCuenta> saldoCuentaOpt = saldoCuentaRepository.findByCuenta(cuenta);
            if (saldoCuentaOpt.isPresent()) {
                SaldoCuenta saldoCuenta = saldoCuentaOpt.get();

                // Verificar si el saldo es suficiente
                if (saldoCuenta.getSaldo().compareTo(monto) >= 0) {
                    // Actualizar el saldo
                    saldoCuenta.setSaldo(saldoCuenta.getSaldo().subtract(monto));
                    saldoCuentaRepository.save(saldoCuenta);

                    // Registrar la transacción (sin cuenta destino porque es un retiro)
                    Transacciones transaccion = new Transacciones(cuenta, monto, "Retiro Cajero Automático", null); // Retiro no tiene cuenta destino
                    transaccionesRepository.save(transaccion);
                } else {
                    throw new RuntimeException("Saldo insuficiente para realizar el retiro.");
                }
            } else {
                throw new RuntimeException("Saldo de cuenta no encontrado.");
            }
        } else {
            throw new RuntimeException("Cuenta no encontrada o monto inválido.");
        }
    }

    // Método para procesar una transferencia entre dos cuentas
    public void realizarTransferencia(String numeroTarjetaOrigen, String numeroTarjetaDestino, BigDecimal monto) {
        Cuenta cuentaOrigen = cuentaRepository.findByNumeroTarjeta(numeroTarjetaOrigen);
        Cuenta cuentaDestino = cuentaRepository.findByNumeroTarjeta(numeroTarjetaDestino);

        if (cuentaOrigen != null && cuentaDestino != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            // Obtener saldo de la cuenta origen
            Optional<SaldoCuenta> saldoCuentaOrigenOpt = saldoCuentaRepository.findByCuenta(cuentaOrigen);
            Optional<SaldoCuenta> saldoCuentaDestinoOpt = saldoCuentaRepository.findByCuenta(cuentaDestino);

            if (saldoCuentaOrigenOpt.isPresent() && saldoCuentaDestinoOpt.isPresent()) {
                SaldoCuenta saldoCuentaOrigen = saldoCuentaOrigenOpt.get();
                SaldoCuenta saldoCuentaDestino = saldoCuentaDestinoOpt.get();

                // Verificar si el saldo es suficiente
                if (saldoCuentaOrigen.getSaldo().compareTo(monto) >= 0) {
                    // Actualizar saldo de la cuenta origen y destino
                    saldoCuentaOrigen.setSaldo(saldoCuentaOrigen.getSaldo().subtract(monto));
                    saldoCuentaDestino.setSaldo(saldoCuentaDestino.getSaldo().add(monto));

                    saldoCuentaRepository.save(saldoCuentaOrigen);
                    saldoCuentaRepository.save(saldoCuentaDestino);

                    // Registrar las transacciones
                    Transacciones transaccionOrigen = new Transacciones(cuentaOrigen, monto, "TRANSFERENCIA", cuentaDestino);
                    transaccionesRepository.save(transaccionOrigen);
                    Transacciones transaccionDestino = new Transacciones(cuentaDestino, monto, "TRANSFERENCIA", cuentaDestino);
                    transaccionesRepository.save(transaccionDestino);
                } else {
                    throw new RuntimeException("Saldo insuficiente para realizar la transferencia.");
                }
            }
        }
    }
    public List<Transacciones> obtenerTransaccionesPorCuenta(String numeroTarjeta) {
        return transaccionesRepository.findByCuenta_NumeroTarjeta(numeroTarjeta);
    }
    
    public List<TransaccionDto> obtenerHistorialTransacciones(String numeroTarjeta) {
        List<Transacciones> transacciones = transaccionesRepository.findByCuenta_NumeroTarjeta(numeroTarjeta);
        return transacciones.stream()
                .map(t -> new TransaccionDto(
                        t.getMonto(),
                        t.getTipoTransaccion(),
                        t.getFecha(),
                        t.getCuentaDestinoNumeroTarjeta()
                ))
                .collect(Collectors.toList());
    }
    public List<Transacciones> obtenerTransaccionesConFiltros(
            String numeroTarjeta,
            String fecha,
            String tipoTransaccion) {

        return transaccionesRepository.findAll(
            TransaccionEspecificaciones.filtrarTransacciones(numeroTarjeta, fecha, tipoTransaccion)
        );
    }
}