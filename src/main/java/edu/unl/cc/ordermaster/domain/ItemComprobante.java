package edu.unl.cc.ordermaster.domain;

import java.math.BigDecimal;

public class ItemComprobante {


    private Integer cantidad;
    private BigDecimal subtotal;
    //relaciones
    private ItemPedido itempedido;

    public ItemComprobante() {
    }

    public ItemComprobante(ItemPedido itempedido) {
        this.itempedido = itempedido;
        this.cantidad = itempedido.getCantidad();
        this.subtotal = itempedido.getSubtotal();
    }

    private void calcularTotal() {
        this.subtotal = BigDecimal.valueOf(this.cantidad).multiply(this.itempedido.getItem().getPrecio());
    }

    public ItemPedido getItem() {
        return itempedido;
    }

    public void setItem(ItemPedido item) {
        this.itempedido = itempedido;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        calcularTotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
    //Eliminar
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "ItemComprobante{" +
                "cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                ", itempedido=" + itempedido +
                '}';
    }
}