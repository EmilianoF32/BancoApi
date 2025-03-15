package com.example.Api.Dto;

import java.math.BigDecimal;

public class RetiroRequest {
    private String numeroTarjeta;
    private BigDecimal monto;

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}
