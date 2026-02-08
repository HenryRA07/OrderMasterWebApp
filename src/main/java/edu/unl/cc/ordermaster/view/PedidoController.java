package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.business.service.MenuFacade;
import edu.unl.cc.ordermaster.business.service.PedidoFacade;
import edu.unl.cc.ordermaster.domain.*;
import edu.unl.cc.ordermaster.faces.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class PedidoController implements Serializable {

    private static final long serialVersionUID = 1L;

    // Inyección de dependencias
    @Inject
    private MenuFacade menuFacade;
    
    @Inject
    private PedidoFacade pedidoFacade;
    
    @Inject
    private FacesUtil facesUtil;

    // Propiedades existentes del formulario
    private Integer cantidad;
    private String observacion;
    private BigDecimal precio;
    private Integer mesa;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;

    // Propiedades del pedido
    private Pedido pedidoactual;
    private Cliente clientepedido;

    // Propiedades para manejo dinámico del menú
    private List<ItemMenu> itemsMenuDelDia;
    private List<ItemMenu> productosFiltrados;
    private ItemMenu itemseleccionado;
    private TipoMenu tipoSeleccionado = TipoMenu.MIXTO; // Todos por defecto

    @PostConstruct
    public void init() {
        inicilizarItems();
        inicializarValoresPorDefecto();
    }

    /**
     * Carga los items del menú del día desde la base de datos
     */
    public void inicilizarItems(){
        try {
            itemsMenuDelDia = menuFacade.obtenerTodosLosItemsDelDia();
        } catch (Exception e) {
            facesUtil.addErrorMessage("Error al cargar el menú del día: " + e.getMessage());
            itemsMenuDelDia = List.of();
            productosFiltrados = List.of();
        }
    }

    /**
     * Inicializa un nuevo pedido si no existe
     */
    private void inicializarPedido() {
        if (pedidoactual == null) {
            pedidoactual = new Pedido();
            pedidoactual.setEstado(EstadoPedido.PENDIENTE);
            // La fecha de creación se establece automáticamente en el constructor de Pedido
        }
    }

    /**
     * Inicializa valores por defecto para el formulario
     */
    private void inicializarValoresPorDefecto() {
        if (cantidad == null) {
            cantidad = 1;
        }
        if (mesa == null) {
            mesa = 1;
        }
    }

    /**
     * Obtiene el precio total formateado para mostrar en la vista (ej: $46.000)
     */
    public String getPrecioTotalFormateado() {
        if (pedidoactual == null || pedidoactual.getPrecioTotal() == null) {
            return "$0";
        }
        return formatPrecio(pedidoactual.getPrecioTotal());
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

    /**
     * Obtiene la cantidad de items en el pedido actual
     */
    public int getCantidadItemsPedido() {
        if (pedidoactual == null || pedidoactual.getItemPedido() == null) {
            return 0;
        }
        return pedidoactual.getItemPedido().size();
    }

    /**
     * Lista de items del pedido para la vista (nunca null)
     */
    public List<ItemPedido> getItemsDelPedido() {
        if (pedidoactual == null || pedidoactual.getItemPedido() == null) {
            return List.of();
        }
        return pedidoactual.getItemPedido();
    }

    /**
     * Confirma y finaliza el pedido actual
     */
    public String confirmarPedido() {
        try {
            if (pedidoactual == null || pedidoactual.getItemPedido() == null || pedidoactual.getItemPedido().isEmpty()) {
                facesUtil.addErrorMessage("El pedido está vacío. Agregue productos antes de confirmar.");
                return null;
            }

            // Actualizar el pedido para asegurar que esté guardado
            pedidoFacade.actualizarPedido(pedidoactual);

            facesUtil.addSuccessMessage("Pedido confirmado exitosamente! ID: " + pedidoactual.getId());

            // Limpiar para nuevo pedido
            pedidoactual = null;
            inicializarPedido();

            return null; // Permanecer en la misma página
        } catch (Exception e) {
            facesUtil.addErrorMessage("Error al confirmar el pedido: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina un item del pedido
     */
    public void eliminarItemPedido(ItemPedido item) {
        try {
            if (pedidoactual != null && item != null) {
                pedidoactual.eliminarItem(item);
                pedidoFacade.actualizarPedido(pedidoactual);
                facesUtil.addSuccessMessage("Producto eliminado del pedido");
            }
        } catch (Exception e) {
            facesUtil.addErrorMessage("Error al eliminar producto: " + e.getMessage());
        }
    }

    /**
     * Reinicia el pedido actual
     */
    public void reiniciarPedido() {
        pedidoactual = null;
        inicializarPedido();
        facesUtil.addSuccessMessage("Pedido reiniciado");
    }

    public void seleccionarItem(ItemMenu itemMenu){
        this.itemseleccionado = itemMenu;
        this.cantidad = 1;
        this.observacion = "";
    }

    public void agregarItemPedido(){
        try {
            // Validaciones
            if (itemseleccionado == null) {
                facesUtil.addErrorMessage("Debe seleccionar un producto");
                return;
            }
            if (cantidad == null || cantidad <= 0) {
                facesUtil.addErrorMessage("La cantidad debe ser mayor a 0");
                return;
            }
            if (mesa == null || mesa <= 0) {
                facesUtil.addErrorMessage("El número de mesa debe ser válido");
                return;
            }

            // Crear cliente si es necesario
            if (clientepedido == null) {
                clientepedido = new Cliente();
                clientepedido.setNombre(nombre != null ? nombre : "Cliente");
                clientepedido.setApellido(apellido != null ? apellido : "");
                clientepedido.setDni(dni != null ? dni : "N/A");
                clientepedido.setTelefono(telefono != null ? telefono : "");
                clientepedido.setEmail(email != null ? email : "");
            }

            // Crear item del pedido
            ItemPedido item = new ItemPedido();
            item.setItem(itemseleccionado);
            item.setCantidad(cantidad);
            item.setObservacion(observacion != null && !observacion.trim().isEmpty() ? observacion.trim() : "-");

            // Configurar pedido
            pedidoactual.setMesa(mesa);
            pedidoactual.setCliente(clientepedido);

            // Si el pedido es nuevo, crearlo primero
            if (pedidoactual.getId() == null) {
                pedidoactual = pedidoFacade.crearPedido(pedidoactual);
                facesUtil.addSuccessMessage("Pedido creado exitosamente. ID: " + pedidoactual.getId());
            }

            // Agregar item al pedido
            pedidoFacade.agregarItemPedido(item, pedidoactual);

            facesUtil.addSuccessMessage("Producto agregado al pedido: " +
                itemseleccionado.getProducto().getNombre() + " (x" + cantidad + ")");

            limpiarSeleccion();

        } catch (Exception e) {
            facesUtil.addErrorMessage("Error al agregar producto al pedido: " + e.getMessage());
        }
    }

    private void limpiarSeleccion() {
        this.itemseleccionado = null;
        this.cantidad= 0;
        this.observacion = "";
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getMesa() {
        return mesa;
    }

    public void setMesa(Integer mesa) {
        this.mesa = mesa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Cliente getClientepedido() {
        return clientepedido;
    }

    public void setClientepedido(Cliente clientepedido) {
        this.clientepedido = clientepedido;
    }

    public Pedido getPedidoactual() {
        return pedidoactual;
    }

    public void setPedidoactual(Pedido pedidoactual) {
        this.pedidoactual = pedidoactual;
    }
    // Getters y setters para propiedades dinámicas

    public List<ItemMenu> getItemsMenuDelDia() {
        return itemsMenuDelDia;
    }

    public void setItemsMenuDelDia(List<ItemMenu> itemsMenuDelDia) {
        this.itemsMenuDelDia = itemsMenuDelDia;
    }

    public List<ItemMenu> getProductosFiltrados() {
        return productosFiltrados;
    }

    public void setProductosFiltrados(List<ItemMenu> productosFiltrados) {
        this.productosFiltrados = productosFiltrados;
    }

    public ItemMenu getItemseleccionado() {
        return itemseleccionado;
    }

    public void setItemseleccionado(ItemMenu itemseleccionado) {
        this.itemseleccionado = itemseleccionado;
    }

    public TipoMenu getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(TipoMenu tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }
}
