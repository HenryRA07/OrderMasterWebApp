package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.business.service.PedidoFacade;
import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.Pedido;
import edu.unl.cc.ordermaster.domain.ItemPedido;
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
        LOGGER.info("CajaController initialized");
        cargarPedidosListos();
    }
    
    public void actualizarPedidos() {
        cargarPedidosListos();
    }

    public void cargarPedidosListos() {
        try {
            pedidosListos = pedidoFacade.obtenerPedidosListos();
            LOGGER.info("Se cargaron " + pedidosListos.size() + " pedidos listos para cobrar");

            for (Pedido p : pedidosListos) {
                LOGGER.info("Pedido ID: " + p.getId() + 
                           ", Mesa: " + p.getMesa() + 
                           ", PrecioTotal ANTES: " + p.getPrecioTotal() +
                           ", Items: " + (p.getItemPedido() != null ? p.getItemPedido().size() : 0));
                p.calcularTotal();
                LOGGER.info("PrecioTotal DESPUÉS: " + p.getPrecioTotal());
                
                if (p.getItemPedido() != null) {
                    for (int i = 0; i < p.getItemPedido().size(); i++) {
                        ItemPedido item = p.getItemPedido().get(i);
                        LOGGER.info("  Item " + i + ": " + item.getSubtotal());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error al cargar pedidos listos: " + e.getMessage());
            pedidosListos = List.of();
        }
    }

    public void seleccionarPedido(Pedido pedido) {
        LOGGER.info("=== INICIO seleccionarPedido ===");
        LOGGER.info("Pedido recibido: " + (pedido != null ? "ID=" + pedido.getId() + ", Precio=" + pedido.getPrecioTotal() : "NULL"));
        
        this.pedidoSeleccionado = pedido;
        this.efectivoRecibido = null;
        this.vuelto = null;
        
        // FORZAR recálculo del total
        if (this.pedidoSeleccionado != null) {
            this.pedidoSeleccionado.calcularTotal();
            LOGGER.info("Después de calcularTotal: " + this.pedidoSeleccionado.getPrecioTotal());
        }
        
        LOGGER.info("Pedido asignado: " + (this.pedidoSeleccionado != null ? "ID=" + this.pedidoSeleccionado.getId() + ", Precio=" + this.pedidoSeleccionado.getPrecioTotal() : "NULL"));
        LOGGER.info("=== FIN seleccionarPedido ===");
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
            pedidoFacade.cambiarEstadoPedido(pedidoSeleccionado, edu.unl.cc.ordermaster.domain.EstadoPedido.COMPLETADO);
            pedidoSeleccionado = null;
            efectivoRecibido = null;
            vuelto = null;
            cargarPedidosListos();
            
            LOGGER.info("Pago procesado y pedido marcado como completado exitosamente");
            
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
            pedidoFacade.cambiarEstadoPedido(pedidoSeleccionado, edu.unl.cc.ordermaster.domain.EstadoPedido.COMPLETADO);
            pedidoSeleccionado = null;
            efectivoRecibido = null;
            vuelto = null;
            cargarPedidosListos();
            
            LOGGER.info("Pago con tarjeta procesado y pedido marcado como completado exitosamente");
            
        } catch (Exception e) {
            LOGGER.severe("Error al procesar pago con tarjeta: " + e.getMessage());
            throw new RuntimeException("No se pudo procesar el pago: " + e.getMessage());
        }
    }
    /**
     * Formatea un precio con separador de miles (ej: 12500 -> $12.500)
     */
    public String formatPrecio(BigDecimal precio) {
        LOGGER.info("formatPrecio llamado con: " + precio);
        if (precio == null) {
            LOGGER.warning("precio es NULL");
            return "$0";
        }
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');
            DecimalFormat df = new DecimalFormat("#,##0", symbols);
            String resultado = "$" + df.format(precio.longValue());
            LOGGER.info("Resultado formateado: " + resultado);
            return resultado;
        } catch (Exception e) {
            LOGGER.severe("Error formateando precio: " + e.getMessage());
            return "$" + precio.toString();
        }
    }

    public List<Pedido> getPedidosListos() {
        return pedidosListos;
    }

    public void setPedidosListos(List<Pedido> pedidosListos) {
        this.pedidosListos = pedidosListos;
    }

    public Pedido getPedidoSeleccionado() {
        LOGGER.info("getPedidoSeleccionado llamado: " + (pedidoSeleccionado != null ? "ID=" + pedidoSeleccionado.getId() + ", Precio=" + pedidoSeleccionado.getPrecioTotal() : "NULL"));
        return pedidoSeleccionado;
    }

    public void setPedidoSeleccionado(Pedido pedidoSeleccionado) {
        this.pedidoSeleccionado = pedidoSeleccionado;
        if (pedidoSeleccionado != null) {
            LOGGER.info("Pedido seleccionado: ID=" + pedidoSeleccionado.getId() + 
                       ", PrecioTotal=" + pedidoSeleccionado.getPrecioTotal() +
                       ", Mesa=" + pedidoSeleccionado.getMesa());
        }
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

}