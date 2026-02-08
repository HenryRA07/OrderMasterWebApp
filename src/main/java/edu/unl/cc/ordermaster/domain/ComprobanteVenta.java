package edu.unl.cc.ordermaster.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ComprobanteVenta {
    private String NOMBRECOMPROBANTE = "Shaman Black";
    private String DIRECCIONRESTAURANTE = "Via Antigua Zamora";
    private LocalDate fechaComprobante;
    private BigDecimal precioTotal;
    //Relaciones
    private Pedido pedido;
    private List<ItemComprobante> itemComprobante;
    private MetodoPago metodoPago;

    public ComprobanteVenta() {
        this.fechaComprobante = LocalDate.now();
    }

    public ComprobanteVenta(Pedido pedido, MetodoPago metodoPago) {
        this();
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        duplicar();
        calcularTotal();
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

    public void calcularTotal(){
        BigDecimal total = BigDecimal.ZERO;
        if(itemComprobante != null) {
            for(ItemComprobante item:itemComprobante) {
                if(item.getSubtotal() != null) {
                    total = total.add(item.getSubtotal());
                }
            }
        }
        precioTotal = total;
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

    public String getNOMBRECOMPROBANTE() {
        return NOMBRECOMPROBANTE;
    }

    public void setNOMBRECOMPROBANTE(String NOMBRECOMPROBANTE) {
        this.NOMBRECOMPROBANTE = NOMBRECOMPROBANTE;
    }

    public String getDIRECCIONRESTAURANTE() {
        return DIRECCIONRESTAURANTE;
    }

    public void setDIRECCIONRESTAURANTE(String DIRECCIONRESTAURANTE) {
        this.DIRECCIONRESTAURANTE = DIRECCIONRESTAURANTE;
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

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<ItemComprobante> getItemComprobante() {
        return itemComprobante;
    }

    public void setItemComprobante(List<ItemComprobante> itemComprobante) {
        this.itemComprobante = itemComprobante;
        calcularTotal();
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }
}
