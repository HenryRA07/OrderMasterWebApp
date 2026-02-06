package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.business.service.common.PedidoRepository;
import edu.unl.cc.ordermaster.domain.EstadoPedido;
import edu.unl.cc.ordermaster.domain.Pedido;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalDate;
import java.util.List;

@Named
public class ConcinaController {

    private List<Pedido> pedidos;

    public EstadoPedido estadoPedido;

    @Inject
    PedidoRepository bdpedido;

    @PostConstruct
    public void init(){
        pedidos = bdpedido.findPedidosForDateAndEstado(LocalDate.now(), LocalDate.now(), estadoPedido = EstadoPedido.PENDIENTE);
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
