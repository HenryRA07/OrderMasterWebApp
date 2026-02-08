package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.business.service.PedidoFacade;
import edu.unl.cc.ordermaster.domain.*;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
public class CajaController implements Serializable {

    @Inject
    PedidoFacade pedido;

    private List<Pedido> pedidoslistos;
    private Pedido pedidocobrar;
    private MetodoPago pago;
    private LocalDate pedidosdia;
    private BigDecimal entregado;
    private String banco;
    private String numerocomprobante;
    private ComprobanteVenta comprobanteVenta;

    public void init() {
        pedidoslistos = pedido.obtenerPedidosPorFechayEstado(pedidosdia.now(), EstadoPedido.LISTO);
    }

    public void asignar(Pedido pedido) {
        pedidocobrar = pedido;
    }

    public void cobrarefrectivo() {
        pago = new Efectivo(entregado, pedidocobrar);
        resetear();
    }

    public void cobrartransferencia() {
        pago = new Transferencia(entregado, banco, numerocomprobante, pedidocobrar);
        resetear();
    }

    public void resetear(){
        pago = null;
        entregado = BigDecimal.ZERO;
        banco = "";
        numerocomprobante = "";
        pedidocobrar = null;
    }

//    public void generalComprobante(){
//        comprobanteVenta = new ComprobanteVenta(pedidocobrar,pago);
//
//    }

    public List<Pedido> getPedidoslistos() {
        return pedidoslistos;
    }

    public void setPedidoslistos(List<Pedido> pedidoslistos) {
        this.pedidoslistos = pedidoslistos;
    }

    public Pedido getPedidocobrar() {
        return pedidocobrar;
    }

    public void setPedidocobrar(Pedido pedidocobrar) {
        this.pedidocobrar = pedidocobrar;
    }

    public MetodoPago getPago() {
        return pago;
    }

    public void setPago(MetodoPago pago) {
        this.pago = pago;
    }

    public LocalDate getPedidosdia() {
        return pedidosdia;
    }

    public void setPedidosdia(LocalDate pedidosdia) {
        this.pedidosdia = pedidosdia;
    }

    public BigDecimal getEntregado() {
        return entregado;
    }

    public void setEntregado(BigDecimal entregado) {
        this.entregado = entregado;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumerocomprobante() {
        return numerocomprobante;
    }

    public void setNumerocomprobante(String numerocomprobante) {
        this.numerocomprobante = numerocomprobante;
    }
}
