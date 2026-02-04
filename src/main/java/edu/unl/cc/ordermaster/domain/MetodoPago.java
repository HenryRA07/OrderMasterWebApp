package edu.unl.cc.ordermaster.domain;

import java.math.BigDecimal;

public abstract class MetodoPago {
    private BigDecimal cantidadEntrgada;

    public MetodoPago(BigDecimal cantidad) {
        setCantidad(cantidad);
    }

    public BigDecimal getCantidad() {
        return cantidadEntrgada;
    }

    public void setCantidad(BigDecimal cantidad) {
        if (cantidad.floatValue() <= 0){
            throw new IllegalArgumentException("El cantidad debe ser mayor a 0");
        }
        this.cantidadEntrgada = cantidad;
    }

    @Override
    public String toString() {
        return "cantidad = " + cantidadEntrgada + ", ";
    }

}
