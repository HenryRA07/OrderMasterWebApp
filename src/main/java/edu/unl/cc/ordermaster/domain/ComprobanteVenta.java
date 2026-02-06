package edu.unl.cc.ordermaster.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ComprobanteVenta {
    private String nombreRestaurante;
    private String direccionRestaurante;
    private LocalDate fechaComprobante;
    //Relaciones
    private Pedido pedido;
    private List<ItemComprobante> itemComprobante;
    private MetodoPago metodoPago;

    public ComprobanteVenta() {
        this.fechaComprobante = LocalDate.now();
    }

    public ComprobanteVenta(String nombreRestaurante, String direccionRestaurante, Pedido pedido, MetodoPago metodoPago) {
        this();
        this.nombreRestaurante = nombreRestaurante;
        this.direccionRestaurante = direccionRestaurante;
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        duplicar();
    }

    public void duplicar(){
        for(ItemPedido ped: pedido.getItemPedido()){
            agregarItem(new ItemComprobante(ped));
        }
    }

    public void agregarItem(ItemComprobante item){
        if(itemComprobante==null){
            itemComprobante = new ArrayList<>();
        }
        if(!itemComprobante.contains(item)){
            itemComprobante.add(item);
        }
    }

    public void agregarItems(ItemComprobante... itemComprobantes){
        for(ItemComprobante items: itemComprobantes){
            agregarItem(items);
        }
    }

    public void eliminarItem(ItemComprobante item){
        if(item == null){
            throw new IllegalArgumentException("El item no puede ser nulo");
        }
        if(!itemComprobante.contains(item)) {
            throw new IllegalArgumentException("El item no puede ser eliminado");
        }
        itemComprobante.remove(item);
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public String getDireccionRestaurante() {
        return direccionRestaurante;
    }

    public void setDireccionRestaurante(String direccionRestaurante) {
        this.direccionRestaurante = direccionRestaurante;
    }

    public LocalDate getFechaComprobante() {
        return fechaComprobante;
    }

    public void setFechaComprobante(LocalDate fechaComprobante) {
        this.fechaComprobante = fechaComprobante;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
