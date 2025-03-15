package com.example.Api.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.Api.Repository.TipoTransaccion;

@Entity
@Table(name = "transacciones")
public class Transacciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta; // Relación con la cuenta

    @Column(nullable = false)
    private BigDecimal monto; // Monto de la transacción

    @Column(nullable = false)
    private String tipoTransaccion; // Tipo de transacción (INGRESO, RETIRO, TRANSFERENCIA)

    @Column(nullable = false)
    private String fecha; // Fecha de la transacción (puedes usar `LocalDate` si prefieres manejar fechas)

    // Constructor con los parámetros Cuenta, BigDecimal, String
    public Transacciones(Cuenta cuenta, BigDecimal monto, String tipoTransaccion) {
        this.cuenta = cuenta;
        this.monto = monto;
        this.tipoTransaccion = tipoTransaccion;
        this.fecha = java.time.LocalDate.now().toString(); // Asignar la fecha actual
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
