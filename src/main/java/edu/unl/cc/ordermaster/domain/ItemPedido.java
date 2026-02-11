package edu.unl.cc.ordermaster.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;


    private Integer cantidad;


    private BigDecimal subtotal;

    private String observacion;
    //relaciones
    @OneToOne()
    @JoinColumn(name = "itemMenu_id")
    private ItemMenu item;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public ItemPedido() {
    }

    public ItemPedido(Long id, @Positive @NotNull Integer cantidad, @NotNull @NotEmpty String observacion, @NotNull ItemMenu item) {
        this.id = id;
        setItem(item);
        this.observacion = observacion;
        setCantidad(cantidad);
    }

    private void calcularSubtotal(){
        if (this.cantidad != null && this.getItem() != null && this.getItem().getPrecio() != null) {
            this.subtotal = BigDecimal.valueOf(this.cantidad).multiply(this.getItem().getPrecio());
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
//        if(cantidad <= 0){
//            throw new IllegalArgumentException("La cantidad por lo menos debe ser uno");
//        }
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public ItemMenu getItem() {
        return item;
    }

    public void setItem(ItemMenu item) {
//        if (item == null) {
//            throw new IllegalArgumentException("Item no puede estar vacio");
//        }
        this.item = item;
        if (this.cantidad != null) {
            calcularSubtotal();
        }
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(id, that.id) && Objects.equals(cantidad, that.cantidad) && Objects.equals(subtotal, that.subtotal) && Objects.equals(observacion, that.observacion) && Objects.equals(item, that.item) && Objects.equals(pedido, that.pedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, subtotal, observacion, item, pedido);
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
