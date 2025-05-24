package com.example.Api.Repository;

import com.example.Api.Model.Transacciones; // Importa el modelo de Transacciones
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

@Repository
public interface TransaccionesRepository extends JpaRepository<Transacciones, Long>, JpaSpecificationExecutor<Transacciones>  {
	List<Transacciones> findByCuenta_NumeroTarjeta(String numeroTarjeta);
}