package edu.unl.cc.ordermaster.domain;

import java.math.BigDecimal;

public abstract class MetodoPago {
    private BigDecimal cantidadEntrgada;
    private Pedido pedido;

    public MetodoPago() {
    }

    public MetodoPago(BigDecimal cantidad, Pedido pedido) {
        this.pedido = pedido;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public String toString() {
        return "cantidad = " + cantidadEntrgada + ", ";
    }

}
