package com.example.Api.Segurity;

import com.example.Api.Model.Cuenta;
import com.example.Api.Repository.CuentaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CuentaRepository cuentaRepository;

    public UserDetailsServiceImpl(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String numeroTarjeta) throws UsernameNotFoundException {
        Cuenta cuenta = cuentaRepository.findByNumeroTarjeta(numeroTarjeta);
        if (cuenta == null) {
            throw new UsernameNotFoundException("Cuenta no encontrada con el número de tarjeta: " + numeroTarjeta);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(cuenta.getNumeroTarjeta())
                .password(cuenta.getContrasena()) // Aunque no lo usemos aquí, Spring lo necesita
                .roles(cuenta.getRole().getName().name()) // Esto depende de cómo guardes los roles
                .build();
    }
}