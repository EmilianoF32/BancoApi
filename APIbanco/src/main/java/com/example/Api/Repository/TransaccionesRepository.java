package com.example.Api.Repository;

import com.example.Api.Model.Transacciones; // Importa el modelo de Transacciones
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionesRepository extends JpaRepository<Transacciones, Long> { // Cambiar el nombre de la interfaz a TransaccionesRepository

}