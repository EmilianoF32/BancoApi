package com.example.Api.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "saldo_cuenta")
public class SaldoCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   

    @OneToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta; // Relación con la tabla Cuenta
    
    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;  // Saldo inicial es 0

    // 🔹 Constructor vacío (necesario para Hibernate)
    public SaldoCuenta() {
    }

    // 🔹 Constructor con parámetros
    public SaldoCuenta(Cuenta cuenta, BigDecimal saldo) {
        this.cuenta = cuenta;
        this.saldo = saldo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldoActual) { this.saldo = saldoActual; }
    

}