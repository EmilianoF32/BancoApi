package com.example.Api.Dto;


import com.example.Api.Model.Roles;
import com.example.Api.Repository.TipoCuenta;

import java.math.BigDecimal;

public class CuentaDto {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoElectronico;
    private String numeroTarjeta;
    private TipoCuenta tipoCuenta;
    private Roles role;
    private BigDecimal saldo;

    // Constructor
    public CuentaDto(String nombre, String apellidoPaterno, String apellidoMaterno, 
                 	 String correoElectronico, String numeroTarjeta, 
                     TipoCuenta tipoCuenta, Roles role, BigDecimal saldo) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correoElectronico = correoElectronico;
        this.numeroTarjeta = numeroTarjeta;
        this.tipoCuenta = tipoCuenta;
        this.role = role;
        this.saldo = saldo;
    }

    // Getters and Setters

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public TipoCuenta getTipoCuenta() {return tipoCuenta;}
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public Roles getRole() { return role; }
    public void setRole(Roles role) { this.role = role; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}
