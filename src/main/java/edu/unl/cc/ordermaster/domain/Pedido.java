package edu.unl.cc.ordermaster.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pedido {


    private Integer mesa;
    private BigDecimal precioTotal;
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    //relaciones
    private List<ItemPedido> itemPedido;
    private Cliente cliente;

    public Pedido() {
    }

    public Pedido(int mesa, Cliente cliente) {
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
        if(!itemPedido.contains(item)) {
            throw new IllegalArgumentException("El item no puede ser eliminado");
        }
        itemPedido.remove(item);
        calcularTotal();
    }

    private void calcularTotal(){
        BigDecimal total = BigDecimal.ZERO;
        for(ItemPedido item:itemPedido) {
            total.add(item.getSubtotal());
        }
        precioTotal = total;
    }

    public boolean cambiarEstado(EstadoPedido nuevoEstado) {
        if (this.estado == EstadoPedido.PENDIENTE) {

            if (nuevoEstado == EstadoPedido.LISTO) {
                this.estado = nuevoEstado;
                return true;
            } else {
                throw new IllegalArgumentException("El estado ya es pendiente");
            }
        }

        else if (this.estado == EstadoPedido.LISTO) {

            if (nuevoEstado == EstadoPedido.PENDIENTE) {
                throw  new IllegalArgumentException("El estado ya esta listo, no puede ser cambiado");
            }
            return true;
        }

        else {
            return false;
        }
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
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
