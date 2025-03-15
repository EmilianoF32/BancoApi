package com.example.Api.Repository;

import com.example.Api.Model.SaldoCuenta; // Importa el modelo de SaldoCuenta
import com.example.Api.Model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaldoCuentaRepository extends JpaRepository<SaldoCuenta, Long> { // Cambiar el nombre de la interfaz a SaldoCuentaRepository

    // Método para encontrar el saldo de una cuenta específica
    Optional<SaldoCuenta> findByCuenta(Cuenta cuenta);
}