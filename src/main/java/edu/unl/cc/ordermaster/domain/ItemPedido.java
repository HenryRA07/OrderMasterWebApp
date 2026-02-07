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

    public void setObservacion(@NotNull @NotEmpty String observacion) {
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ItemPedido that = (ItemPedido) obj;
        
        // Si ambos tienen ID, comparar por ID
        if (id != null && that.id != null) {
            return id.equals(that.id);
        }
        
        // Si no tienen ID, comparar por item y cantidad (productos iguales)
        if (item != null && that.item != null) {
            return item.equals(that.item) && 
                   cantidad != null && cantidad.equals(that.cantidad);
        }
        
        return false;
    }
    
    @Override
    public int hashCode() {
        // Si tiene ID, usar ID
        if (id != null) {
            return id.hashCode();
        }
        
        // Si no tiene ID, usar combinación de item y cantidad
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + (cantidad != null ? cantidad.hashCode() : 0);
        return result;
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
