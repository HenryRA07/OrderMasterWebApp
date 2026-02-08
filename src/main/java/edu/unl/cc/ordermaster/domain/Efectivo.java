package edu.unl.cc.ordermaster.domain;

import java.math.BigDecimal;

public class Efectivo extends MetodoPago {
    private BigDecimal cambioEntregado;
    private Pedido pedido;

    public Efectivo(BigDecimal cantidad, Pedido pedido) {
        super(cantidad, pedido);
        this.pedido = pedido;
        cambio();
    }

    private void cambio() {
        this.cambioEntregado = super.getCantidad().subtract(this.pedido.getPrecioTotal());
    }

    public BigDecimal getCambioEntregado() {
        return cambioEntregado;
    }

    //Quitar para mejorar seguridad
    public void setCambioEntregado(BigDecimal cambioEntregado) {
        this.cambioEntregado = cambioEntregado;
    }

    @Override
    public String toString() {
        return  "Efectivo dado: $" + super.getCantidad() +  "\n" + "Cambio a entregar: $" + getCambioEntregado();
    }
}
