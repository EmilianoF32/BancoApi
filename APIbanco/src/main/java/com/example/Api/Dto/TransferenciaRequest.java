package com.example.Api.Dto;

import java.math.BigDecimal;

public class TransferenciaRequest {
    private String numeroTarjetaDestino;
    private String usuarioActual;
    private BigDecimal monto;

    public String getNumeroTarjetaDestino() { return numeroTarjetaDestino; }
    public void setNumeroTarjetaDestino(String numeroTarjetaDestino) { this.numeroTarjetaDestino = numeroTarjetaDestino; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    
    public String getUsuarioActual() {return usuarioActual;}
    public void setUsuarioActual(String usuarioActual) {this.usuarioActual = usuarioActual;
    }
}
