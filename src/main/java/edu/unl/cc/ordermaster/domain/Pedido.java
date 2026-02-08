package edu.unl.cc.ordermaster.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private Integer mesa;

    private BigDecimal precioTotal;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    //relaciones
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "pedido")
    private List<ItemPedido> itemPedido;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Cliente cliente;

    private LocalDate fechaPedidoCreacion;

    public Pedido() {
        this.fechaPedidoCreacion = LocalDate.now();
        this.itemPedido = new ArrayList<>();
    }

    public Pedido(Long id,int mesa, Cliente cliente) {
        this();
        this.id = id;
        this.mesa = mesa;
        this.cliente = cliente;
        this.precioTotal = BigDecimal.ZERO;
    }

    public void agregarItem(ItemPedido item){
        if(itemPedido==null){
            itemPedido = new ArrayList<>();
        }
        if(!itemPedido.contains(item)){
            itemPedido.add(item);
            item.setPedido(this); // Establecer relación bidireccional
        }
        calcularTotal();
    }

    public void agregarItems(ItemPedido... itemPedido){
        for(ItemPedido items: itemPedido){
            agregarItem(items);
        }
    }

    public void eliminarItem(ItemPedido item){
        if(item == null){
            throw new IllegalArgumentException("El item no puede ser nulo");
        }

        boolean eliminado = false;
        
        // Buscar y eliminar por ID si existe
        if(item.getId() != null) {
            Long targetId = item.getId();
            eliminado = itemPedido.removeIf(existing -> 
                existing.getId() != null && existing.getId().equals(targetId));
        } else {
            // Si no tiene ID, buscar por igualdad (usa equals())
            eliminado = itemPedido.remove(item);
            
            // Si no funciona por equals, buscar manualmente por item y cantidad
            if (!eliminado) {
                eliminado = itemPedido.removeIf(existing -> 
                    existing.getItem() != null && existing.getItem().equals(item.getItem()) &&
                    existing.getCantidad() != null && existing.getCantidad().equals(item.getCantidad()));
            }
        }
        
        if(!eliminado) {
            throw new IllegalArgumentException("El item no fue encontrado en el pedido");
        }
        calcularTotal();
    }

    public void calcularTotal(){
        BigDecimal total = BigDecimal.ZERO;
        if(itemPedido != null) {
            for(ItemPedido item:itemPedido) {
                if(item.getSubtotal() != null) {
                    total = total.add(item.getSubtotal());
                }
            }
        }
        precioTotal = total;
    }

    public Integer getMesa() {
        return mesa;
    }

    public void setMesa(Integer mesa) {
        this.mesa = mesa;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public List<ItemPedido> getItemPedido() {
        return itemPedido;
    }

    public void setItemPedido(List<ItemPedido> itemPedido) {
        this.itemPedido = itemPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "Pedido{" +
                "nombreCliente='" + cliente.getNombreCompleto() + '\'' +
                ", mesa=" + mesa +
                ", precioTotal=" + precioTotal +
                ", estado=" + estado +
                ", itemPedido=" + itemPedido +
                '}';
    }
}
