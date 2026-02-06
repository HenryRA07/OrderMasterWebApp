package edu.unl.cc.ordermaster.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;


    @Positive @NotNull
    private Integer cantidad;


    private BigDecimal subtotal;

    @NotNull @NotEmpty
    private String observacion;
    //relaciones
    @NotNull
    @OneToOne
    @JoinColumn(name = "itemMenu_id")
    private ItemMenu item;

    public ItemPedido() {
    }

    public ItemPedido(Long id, @Positive @NotNull Integer cantidad, @NotNull @NotEmpty String observacion, @NotNull ItemMenu item) {
        this.id = id;
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

    public void setObservacion(@NotNull @NotEmpty String observacion) {
        this.observacion = observacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
