package com.example.Api.Service;

import com.example.Api.Model.Cuenta;
import com.example.Api.Model.SaldoCuenta;
import com.example.Api.Model.Transacciones;
import com.example.Api.Repository.CuentaRepository;
import com.example.Api.Repository.SaldoCuentaRepository;
import com.example.Api.Repository.TransaccionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

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
        Cuenta cuenta = cuentaRepository.findByNumeroTarjeta(numeroTarjeta);
        if (cuenta != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            // Obtener saldo actual de la cuenta
            Optional<SaldoCuenta> saldoCuentaOpt = saldoCuentaRepository.findByCuenta(cuenta);
            SaldoCuenta saldoCuenta = saldoCuentaOpt.orElseGet(() -> new SaldoCuenta(cuenta, BigDecimal.ZERO));

            // Actualizar el saldo
            saldoCuenta.setSaldo(saldoCuenta.getSaldo().add(monto));
            saldoCuentaRepository.save(saldoCuenta);

            // Registrar la transacción
            Transacciones transaccion = new Transacciones(cuenta, monto, "INGRESO");
            transaccionesRepository.save(transaccion);
        }
    }

    // Método para procesar un retiro de una cuenta
    public void realizarRetiro(String numeroTarjeta, BigDecimal monto) {
        Cuenta cuenta = cuentaRepository.findByNumeroTarjeta(numeroTarjeta);
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

                    // Registrar la transacción
                    Transacciones transaccion = new Transacciones(cuenta, monto, "RETIRO");
                    transaccionesRepository.save(transaccion);
                } else {
                    throw new RuntimeException("Saldo insuficiente para realizar el retiro.");
                }
            }
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
                    Transacciones transaccionOrigen = new Transacciones(cuentaOrigen, monto, "TRANSFERENCIA");
                    transaccionesRepository.save(transaccionOrigen);
                    Transacciones transaccionDestino = new Transacciones(cuentaDestino, monto, "TRANSFERENCIA");
                    transaccionesRepository.save(transaccionDestino);
                } else {
                    throw new RuntimeException("Saldo insuficiente para realizar la transferencia.");
                }
            }
        }
    }
}