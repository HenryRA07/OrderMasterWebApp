package edu.unl.cc.ordermaster.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ItemMenu {

    @NotNull
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
    private boolean disponibilidad;
    //extensiones

    @NotNull
    private Producto producto;

    public ItemMenu() {
    }

    public ItemMenu(@NotNull @Positive(message = "El precio debe ser mayor a 0") BigDecimal precio,
                    boolean disponibilidad,
                    @NotNull Producto producto) {
        setPrecio(precio);
        setProducto(producto);
        this.disponibilidad = disponibilidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(@NotNull @Positive(message = "El precio debe ser mayor a 0") BigDecimal precio) {
//        if (precio.floatValue() <= 0) {
//            throw new IllegalArgumentException("El precio debe ser mayor a 0");
//        }
        this.precio = precio;
    }

    public boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(@NotNull Producto producto) {
//        if (producto == null) {
//            throw new IllegalArgumentException("El producto no puede ser nulo");
//        }
        this.producto = producto;
    }



    @Override
    public String toString() {
        return "ItemMenu{" +
                "precio=" + precio +
                ", disponibilidad=" + disponibilidad +
                ", alimento=" + producto;
    }
}
