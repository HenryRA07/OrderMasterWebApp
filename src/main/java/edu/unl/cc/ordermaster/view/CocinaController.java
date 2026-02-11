package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.business.service.PedidoFacade;
import edu.unl.cc.ordermaster.domain.EstadoPedido;
import edu.unl.cc.ordermaster.domain.ItemMenu;
import edu.unl.cc.ordermaster.domain.ItemPedido;
import edu.unl.cc.ordermaster.domain.Pedido;
import edu.unl.cc.ordermaster.faces.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Named("cocina")
@RequestScoped
public class CocinaController implements Serializable {

    private static Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    private List<Pedido> pedidosPendientes;
    private LocalDate fechaConsulta;
    private List<ItemPedido> detallesPedido;
    private Pedido pedidoSeleccionado;

    @Inject
    FacesUtil faces;

    @Inject
    PedidoFacade dbpedido;

    @PostConstruct
    public void init(){
        caragarPedidosPendientes();
    }

    public void caragarPedidosPendientes(){
        pedidosPendientes = dbpedido.obtenerPedidosPorFechayEstado(
                fechaConsulta.now(),EstadoPedido.PENDIENTE
        );
    }

    public void marcarPedidoListo(Pedido pedido){
        pedidoSeleccionado = pedido;
        pedido.setEstado(EstadoPedido.LISTO);
        dbpedido.actualizarPedido(pedido);
        caragarPedidosPendientes();
    }

    public void verDetalles(Pedido pedido){
        pedidoSeleccionado=pedido;
        Pedido detalles = dbpedido.buscarPedido(pedido.getId());
        detallesPedido.addAll(detalles.getItemPedido());
    }

    public List<Pedido> getPedidosPendientes() {
        return pedidosPendientes;
    }

    public void setPedidosPendientes(List<Pedido> pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }

    public List<ItemPedido> getDetallesPedido() {
        return detallesPedido;
    }

    public void setDetallesPedido(List<ItemPedido> detallesPedido) {
        this.detallesPedido = detallesPedido;
    }

    public LocalDate getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDate fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public Pedido getPedidoSeleccionado() {
        return pedidoSeleccionado;
    }

    public void setPedidoSeleccionado(Pedido pedidoSeleccionado) {
        this.pedidoSeleccionado = pedidoSeleccionado;
    }
}
