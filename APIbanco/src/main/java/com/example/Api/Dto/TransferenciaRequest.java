package com.example.Api.Dto;

import java.math.BigDecimal;

public class TransferenciaRequest {
    private String numeroTarjetaOrigen;
    private String numeroTarjetaDestino;
    private BigDecimal monto;

    public String getNumeroTarjetaOrigen() { return numeroTarjetaOrigen; }
    public void setNumeroTarjetaOrigen(String numeroTarjetaOrigen) { this.numeroTarjetaOrigen = numeroTarjetaOrigen; }

    public String getNumeroTarjetaDestino() { return numeroTarjetaDestino; }
    public void setNumeroTarjetaDestino(String numeroTarjetaDestino) { this.numeroTarjetaDestino = numeroTarjetaDestino; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}
