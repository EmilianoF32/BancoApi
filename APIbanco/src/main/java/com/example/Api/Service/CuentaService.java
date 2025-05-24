package com.example.Api.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import com.example.Api.Repository.*;
import com.example.Api.Model.Cuenta;
import java.util.List;
import com.example.Api.Model.Roles;
import com.example.Api.Model.SaldoCuenta;
import java.math.BigDecimal;


@Service
public class CuentaService {
    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private SaldoCuentaRepository saldoCuentaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Inyectar el PasswordEncoder

    public Cuenta registrarCuenta(Cuenta cuenta) {
    	Optional<Cuenta> cuentaExistente = cuentaRepository.findByCurpAndTipoCuenta(cuenta.getCurp(), cuenta.getTipoCuenta());
        if (cuentaExistente.isPresent()) {
            throw new RuntimeException("Ya existe una cuenta con este CURP y tipo de cuenta.");
        }
        cuenta.setContrasena(passwordEncoder.encode(cuenta.getContrasena()));  // Se usa el encoder inyectado
        cuenta.setNumeroTarjeta(generarNumeroTarjeta(cuenta.getTipoCuenta()));
        if (cuenta.getRole() == null) {
            Roles userRole = roleRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
            cuenta.setRole(userRole);    
        }
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        // Crear el saldo inicial en 0 para la cuenta
        SaldoCuenta saldoInicial = new SaldoCuenta(cuentaGuardada, BigDecimal.ZERO);
        saldoCuentaRepository.save(saldoInicial);
        return cuentaGuardada;
    }
    public Cuenta registrarAdministrador(Cuenta cuenta) {
        // Verificar si ya existe una cuenta con el mismo CURP y tipo de cuenta
        Optional<Cuenta> cuentaExistente = cuentaRepository.findByCurpAndTipoCuenta(cuenta.getCurp(), cuenta.getTipoCuenta());
        if (cuentaExistente.isPresent()) {
            throw new RuntimeException("Ya existe una cuenta con este CURP y tipo de cuenta.");
        }
        // Encriptar la contraseña
        cuenta.setContrasena(passwordEncoder.encode(cuenta.getContrasena()));
        // Generar número de tarjeta con tipo ADMIN
        cuenta.setNumeroTarjeta(generarNumeroTarjeta(TipoCuenta.ADMIN));
        // Asignar el rol ADMIN por defecto
        if (cuenta.getRole() == null) {
            Roles adminRole = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
            cuenta.setRole(adminRole);
        }
        // Asignar tipo de cuenta como ADMIN
        if (cuenta.getTipoCuenta() == null) {
            cuenta.setTipoCuenta(TipoCuenta.ADMIN);  // Asegurarse que es ADMIN
        }
        // Guardar la cuenta en la base de datos
        return cuentaRepository.save(cuenta);
    }

    public Optional<Cuenta> buscarPorNumeroTarjeta(String numeroTarjeta) {
        return Optional.ofNullable(cuentaRepository.findByNumeroTarjeta(numeroTarjeta));
    }

    private String generarNumeroTarjeta(TipoCuenta tipoCuenta) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append((int) (Math.random() * 10)); // Genera un número entre 0-9
        }
        sb.append(tipoCuenta == TipoCuenta.CREDITO ? 'C' : 'D');
        return sb.toString();
    }

    // ✅ Método para autenticar usuario
    public Cuenta autenticarCuenta(String numeroTarjeta, String password) {
        Optional<Cuenta> cuentaOpt = Optional.ofNullable(cuentaRepository.findByNumeroTarjeta(numeroTarjeta));

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();

            // No es necesario encriptar la contraseña nuevamente, solo comparamos con el hash almacenado
            System.out.println("Contraseña ingresada: " + password);
            System.out.println("Contraseña almacenada: " + cuenta.getContrasena());

            // Verificación de la contraseña encriptada
            if (passwordEncoder.matches(password, cuenta.getContrasena())) {
                System.out.println("Autenticación exitosa");
                return cuenta;
            } else {
                System.out.println("Error: La contraseña no coincide");
            }
        } else {
            System.out.println("Error: Cuenta no encontrada");
        }
        return null;
    }
    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll();
    }
    public boolean deshabilitarCuenta(String numeroTarjeta) {
        Optional<Cuenta> cuentaOpt = Optional.ofNullable(cuentaRepository.findByNumeroTarjeta(numeroTarjeta));
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setActivo(false);
            cuentaRepository.save(cuenta);
            return true;
        }
        return false;
    }
}
