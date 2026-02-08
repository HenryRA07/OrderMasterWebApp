package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.business.service.common.PedidoRepository;
import edu.unl.cc.ordermaster.business.service.PedidoFacade;
import edu.unl.cc.ordermaster.domain.EstadoPedido;
import edu.unl.cc.ordermaster.domain.Pedido;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalDate;
import java.util.List;

@Named("cocina")
@RequestScoped
public class ConcinaController {

    private List<Pedido> pedidosPendientes;
    private LocalDate fechaConsulta;
    private Pedido pedidoSeleccionado;

    @Inject
    PedidoRepository bdpedido;
    
    @Inject
    PedidoFacade pedidoFacade;

    @PostConstruct
    public void init(){
        caragarPedidosPendientes();
    }

    public void caragarPedidosPendientes(){
        pedidosPendientes = bdpedido.findPedidosForDateAndEstado(
                fechaConsulta.now(),fechaConsulta.now(), EstadoPedido.PENDIENTE
        );
    }

    public void marcarPedidoListo(Pedido pedido){
        pedido.setEstado(EstadoPedido.LISTO);
        pedidoFacade.actualizarPedido(pedido);
        caragarPedidosPendientes();
    }


    public List<Pedido> getPedidosPendientes() {
        return pedidosPendientes;
    }

    public void setPedidosPendientes(List<Pedido> pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }

    public void verDetallesPedido(Pedido pedido) {
        this.pedidoSeleccionado = pedido;
    }

    public Pedido getPedidoSeleccionado() {
        return pedidoSeleccionado;
    }

    public void setPedidoSeleccionado(Pedido pedidoSeleccionado) {
        this.pedidoSeleccionado = pedidoSeleccionado;
    }
}
