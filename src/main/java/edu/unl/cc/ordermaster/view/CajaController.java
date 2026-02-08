package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.business.service.PedidoFacade;
import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.Pedido;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Named("cajaController")
@ViewScoped
public class CajaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CajaController.class.getName());

    private List<Pedido> pedidosListos;
    private Pedido pedidoSeleccionado;
    private Double efectivoRecibido;
    private Double vuelto;

    @Inject
    private PedidoFacade pedidoFacade;

    @Inject
    private CrudGenericService crudService;

    @PostConstruct
    public void init() {
        cargarPedidosListos();
    }

    public void cargarPedidosListos() {
        try {
            pedidosListos = pedidoFacade.obtenerPedidosListos();
            LOGGER.info("Se cargaron " + pedidosListos.size() + " pedidos listos para cobrar");
        } catch (Exception e) {
            LOGGER.severe("Error al cargar pedidos listos: " + e.getMessage());
            pedidosListos = List.of(); // Lista vacía en caso de error
        }
    }

    public void actualizarPedidos() {
        cargarPedidosListos();
    }

    public void seleccionarPedido(Pedido pedido) {
        this.pedidoSeleccionado = pedido;
        this.efectivoRecibido = null;
        this.vuelto = null;
    }

    public void calcularVuelto() {
        if (efectivoRecibido != null && pedidoSeleccionado != null) {
            BigDecimal total = pedidoSeleccionado.getPrecioTotal();
            vuelto = efectivoRecibido - total.doubleValue();
            if (vuelto < 0) {
                vuelto = 0.0;
            }
        }
    }

    public void procesarPagoEfectivo() {
        try {
            if (pedidoSeleccionado == null) {
                throw new IllegalArgumentException("No hay pedido seleccionado");
            }

            LOGGER.info("Procesando pago en efectivo para pedido ID: " + pedidoSeleccionado.getId());
            crudService.delete(Pedido.class, pedidoSeleccionado.getId());
            pedidoSeleccionado = null;
            efectivoRecibido = null;
            vuelto = null;
            cargarPedidosListos();
            
            LOGGER.info("Pago procesado y pedido eliminado exitosamente");
            
        } catch (Exception e) {
            LOGGER.severe("Error al procesar pago en efectivo: " + e.getMessage());
            throw new RuntimeException("No se pudo procesar el pago: " + e.getMessage());
        }
    }

    public void procesarPagoTarjeta() {
        try {
            if (pedidoSeleccionado == null) {
                throw new IllegalArgumentException("No hay pedido seleccionado");
            }

            LOGGER.info("Procesando pago con tarjeta para pedido ID: " + pedidoSeleccionado.getId());
            crudService.delete(Pedido.class, pedidoSeleccionado.getId());
            pedidoSeleccionado = null;
            efectivoRecibido = null;
            vuelto = null;
            cargarPedidosListos();
            
            LOGGER.info("Pago con tarjeta procesado y pedido eliminado exitosamente");
            
        } catch (Exception e) {
            LOGGER.severe("Error al procesar pago con tarjeta: " + e.getMessage());
            throw new RuntimeException("No se pudo procesar el pago: " + e.getMessage());
        }
    }

    public List<Pedido> getPedidosListos() {
        return pedidosListos;
    }

    public void setPedidosListos(List<Pedido> pedidosListos) {
        this.pedidosListos = pedidosListos;
    }

    public Pedido getPedidoSeleccionado() {
        return pedidoSeleccionado;
    }

    public void setPedidoSeleccionado(Pedido pedidoSeleccionado) {
        this.pedidoSeleccionado = pedidoSeleccionado;
    }

    public Double getEfectivoRecibido() {
        return efectivoRecibido;
    }

    public void setEfectivoRecibido(Double efectivoRecibido) {
        this.efectivoRecibido = efectivoRecibido;
    }

    public Double getVuelto() {
        return vuelto;
    }

    public void setVuelto(Double vuelto) {
        this.vuelto = vuelto;
    }

    /**
     * Formatea un precio con separador de miles (ej: 12500 -> $12.500)
     */
    public String formatPrecio(BigDecimal precio) {
        if (precio == null) return "$0";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0", symbols);
        return "$" + df.format(precio.longValue());
    }
}