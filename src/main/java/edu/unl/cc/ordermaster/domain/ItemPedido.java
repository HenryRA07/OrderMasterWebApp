package edu.unl.cc.ordermaster.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ItemPedido {

    @Positive @NotNull
    private Integer cantidad;
    private BigDecimal subtotal;

    @NotBlank
    private String observacion;
    //relaciones
    @NotNull
    private ItemMenu item;

    public ItemPedido() {
    }

    public ItemPedido(@Positive @NotNull Integer cantidad, @NotBlank String observacion,@NotNull ItemMenu item) {
        setItem(item);
        this.observacion = observacion;
        setCantidad(cantidad);
    }

    private void calcularSubtotal(){
        this.subtotal = BigDecimal.valueOf(this.cantidad).multiply(this.getItem().getPrecio());
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(@Positive @NotNull Integer cantidad) {
//        if(cantidad <= 0){
//            throw new IllegalArgumentException("La cantidad por lo menos debe ser uno");
//        }
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public ItemMenu getItem() {
        return item;
    }

    public void setItem(@NotNull ItemMenu item) {
//        if (item == null) {
//            throw new IllegalArgumentException("Item no puede estar vacio");
//        }
        this.item = item;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(@NotBlank String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ItemPedido{");
        sb.append("cantidad=").append(cantidad);
        sb.append(", subtotal=").append(subtotal);
        sb.append(", observacion='").append(observacion).append('\'');
        sb.append(", item=").append(item);
        sb.append('}');
        return sb.toString();
    }
}
