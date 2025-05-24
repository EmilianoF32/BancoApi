package com.example.Api.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.Api.Repository.TipoTransaccion;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "transacciones")
public class Transacciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    @JsonIgnore
    private Cuenta cuenta; // Relación con la cuenta

    @Column(nullable = false)
    private BigDecimal monto; // Monto de la transacción

    @Column(nullable = false)
    private String tipoTransaccion; // Tipo de transacción (INGRESO, RETIRO, TRANSFERENCIA)

    @Column(nullable = false)
    private String fecha; // Fecha de la transacción (puedes usar `LocalDate` si prefieres manejar fechas)
    
    @ManyToOne
    @JoinColumn(name = "cuenta_destino_id")
    @JsonIgnore
    private Cuenta cuentaDestino;
    
    public Transacciones() { 
    }

    // Constructor con los parámetros Cuenta, BigDecimal, String
    public Transacciones(Cuenta cuenta, BigDecimal monto, String tipoTransaccion, Cuenta cuentaDestino) {
        this.cuenta = cuenta;
        this.monto = monto;
        this.tipoTransaccion = tipoTransaccion;
        this.cuentaDestino = cuentaDestino;
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
    
    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }
    public String getCuentaDestinoNumeroTarjeta() {
        return cuentaDestino != null ? cuentaDestino.getNumeroTarjeta() : null;  // método auxiliar para obtener el número de tarjeta
    }
}
