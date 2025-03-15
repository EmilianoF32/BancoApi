package com.example.Api.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Api.Model.Cuenta;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
	Cuenta findByNumeroTarjeta(String numeroTarjeta);
	Optional<Cuenta> findByCurpAndTipoCuenta(String curp, TipoCuenta tipoCuenta);
}
