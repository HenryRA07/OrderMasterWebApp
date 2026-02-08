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
import org.primefaces.PrimeFaces;

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
        inicializarItems();
        inicializarPedido();
        inicializarValoresPorDefecto();
    }

    /**
     * Carga los items del menú del día desde la base de datos
     */
    public void inicializarItems(){
        try {
            itemsMenuDelDia = menuFacade.obtenerTodosLosItemsDelDia();
            filtrarPorTipo();
        } catch (Exception e) {
            facesUtil.addErrorMessage("Error al cargar el menú del día: " + e.getMessage());
            itemsMenuDelDia = List.of();
            productosFiltrados = List.of();
        }
    }

    /**
     * Filtra los productos según el tipo de menú seleccionado
     */
    private void filtrarPorTipo() {
        if (itemsMenuDelDia == null || itemsMenuDelDia.isEmpty()) {
            productosFiltrados = List.of();
            return;
        }

        if (tipoSeleccionado == null || tipoSeleccionado == TipoMenu.MIXTO) {
            productosFiltrados = itemsMenuDelDia;
        } else {
            productosFiltrados = itemsMenuDelDia.stream()
                    .filter(item -> item.getMenu() != null && item.getMenu().getTipoMenu() == tipoSeleccionado)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Método llamado cuando el usuario cambia el tipo de menú
     */
    public void onTipoChange() {
        filtrarPorTipo();
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

    public void seleccionarItem(ItemMenu itemMenu){
        this.itemseleccionado = itemMenu;
        this.cantidad = 1;
        this.observacion = "";
    }

    public void agregarItemPedido(){
        try {
            // Validaciones básicas
            if (itemseleccionado == null) {
                facesUtil.addErrorMessage("Debe seleccionar un producto");
                return;
            }
            if (cantidad == null || cantidad <= 0) {
                facesUtil.addErrorMessage("La cantidad debe ser mayor a 0");
                return;
            }

            // Asegurar que el pedido esté inicializado
            if (pedidoactual == null) {
                inicializarPedido();
            }

            // Crear item del pedido (solo en memoria)
            ItemPedido item = new ItemPedido();
            item.setItem(itemseleccionado);
            item.setCantidad(cantidad);
            String obsFinal = observacion != null && !observacion.trim().isEmpty() && !observacion.trim().equals("-") ? observacion.trim() : "";
            System.out.println("DEBUG: Observación original: '" + observacion + "'");
            System.out.println("DEBUG: Observación final: '" + obsFinal + "'");
            item.setObservacion(obsFinal);

            // Agregar item al pedido temporal (sin persistir en BD)
            pedidoactual.agregarItem(item);

            facesUtil.addSuccessMessage("Producto agregado al pedido: " +
                    itemseleccionado.getProducto().getNombre() + " (x" + cantidad + ")");

            limpiarSeleccion();

        } catch (Exception e) {
            facesUtil.addErrorMessage("Error al agregar producto al pedido: " + e.getMessage());
        }
    }

    private void limpiarSeleccion() {
        this.itemseleccionado = null;
        this.cantidad= 1;
        this.observacion = "";
    }

    private void reiniciarParametros(){

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
        if (pedidoactual == null) {
            return 0;
        }
        if (pedidoactual.getItemPedido() == null) {
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
     * Verifica si hay productos y muestra diálogo de cliente
     */
    public void verificarYMostrarDialogoCliente() {
        System.out.println("=== DEBUG: verificarYMostrarDialogoCliente() llamado ===");
        System.out.println("Cantidad de items en pedido: " + getCantidadItemsPedido());

        if (getCantidadItemsPedido() > 0) {
            System.out.println("HAY productos - preparando para mostrar diálogo");

            // Agregar mensaje de éxito para asegurar que se muestre en el update
            facesUtil.addSuccessMessage("Preparando confirmación del pedido...");

            // Intentar mostrar diálogo con JavaScript
            try {
                PrimeFaces.current().executeScript("console.log('Intentando mostrar modal desde backend...'); PF('dlgCliente').show();");
                System.out.println("Script de diálogo ejecutado desde backend");
            } catch (Exception e) {
                System.out.println("ERROR al mostrar diálogo desde backend: " + e.getMessage());
                e.printStackTrace();
            }

            // También lo forzamos con oncomplete en el XHTML
            System.out.println("El modal debería aparecer con oncomplete también");

        } else {
            System.out.println("ERROR: No hay productos en el pedido");
            facesUtil.addErrorMessage("Debe agregar productos al pedido antes de confirmar");
        }
        System.out.println("=== FIN: verificarYMostrarDialogoCliente ===");
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
     * Confirma el pedido con los datos del cliente
     */
    public String confirmarPedidoConDatos() {
        System.out.println("=== DEBUG: confirmarPedidoConDatos() llamado ===");

        try {
            // Validaciones
            if (pedidoactual == null || pedidoactual.getItemPedido() == null || pedidoactual.getItemPedido().isEmpty()) {
                System.out.println("ERROR: El pedido está vacío");
                facesUtil.addErrorMessage("El pedido está vacío. Agregue productos antes de confirmar.");
                return null;
            }
            System.out.println("Items en pedido: " + pedidoactual.getItemPedido().size());

            // Validar datos del cliente (opcional)
            if (mesa != null && mesa <= 0) {
                System.out.println("ERROR: Mesa inválida");
                facesUtil.addErrorMessage("El número de mesa debe ser mayor a 0");
                return null;
            }
            if (email != null && !email.trim().isEmpty() && !email.trim().contains("@")) {
                System.out.println("ERROR: Formato de email incorrecto");
                facesUtil.addErrorMessage("El formato del email es incorrecto");
                return null;
            }

            System.out.println("=== DATOS COMPLETOS DEL CLIENTE ===");
            System.out.println("Mesa: " + mesa);
            System.out.println("Nombre: '" + nombre + "' (longitud: " + (nombre != null ? nombre.length() : "null") + ")");
            System.out.println("Apellido: '" + apellido + "' (longitud: " + (apellido != null ? apellido.length() : "null") + ")");
            System.out.println("Email: '" + email + "' (longitud: " + (email != null ? email.length() : "null") + ")");
            System.out.println("Telefono: '" + telefono + "' (longitud: " + (telefono != null ? telefono.length() : "null") + ")");
            System.out.println("DNI: '" + dni + "' (longitud: " + (dni != null ? dni.length() : "null") + ")");
            System.out.println("========================================");

            // Crear cliente
            clientepedido = new Cliente();

            // Asignar valores DIRECTAMENTE sin validaciones adicionales
            // (las validaciones ya se hicieron arriba)
            clientepedido.setNombre(nombre.trim());
            clientepedido.setApellido(apellido.trim());
            clientepedido.setEmail(email.trim() + "+pedido" + System.currentTimeMillis() + "@ordermaster.local");

            // DNI y teléfono opcionales
            if (dni != null && dni.trim().length() > 0) {
                clientepedido.setDni(dni.trim());
            }
            if (telefono != null && telefono.trim().length() > 0) {
                clientepedido.setTelefono(telefono.trim());
            }

            System.out.println("=== CLIENTE CREADO ===");
            System.out.println("Nombre: " + clientepedido.getNombre());
            System.out.println("Apellido: " + clientepedido.getApellido());
            System.out.println("Email: " + clientepedido.getEmail());

            // Asignar cliente al pedido y número de mesa
            pedidoactual.setCliente(clientepedido);
            pedidoactual.setMesa(mesa);

            System.out.println("Cliente asignado al pedido");
            System.out.println("Enviando pedido a cocina...");

            // Crear y persistir el pedido con manejo específico de errores
            try {
                pedidoactual = pedidoFacade.crearPedido(pedidoactual);
                System.out.println("Pedido CREADO y ENVIADO A COCINA - ID: " + pedidoactual.getId());
            } catch (Exception persistenceError) {
                System.out.println("ERROR DE PERSISTENCIA: " + persistenceError.getMessage());
                persistenceError.printStackTrace();

                // Error más específico para el usuario
                if (persistenceError.getMessage().contains("ConstraintViolation")) {
                    facesUtil.addErrorMessage("Error: Hay datos obligatorios que no se completaron correctamente. Verifique todos los campos marcados con *");
                } else {
                    facesUtil.addErrorMessage("Error al guardar el pedido: " + persistenceError.getMessage());
                }
                return null;
            }

            System.out.println("Pedido CREADO y ENVIADO A COCINA - ID: " + pedidoactual.getId());
            System.out.println("Estado del pedido: " + pedidoactual.getEstado());

            facesUtil.addSuccessMessage("Pedido confirmado exitosamente! ID: " + pedidoactual.getId() + " - Enviado a cocina");

            // Limpiar para nuevo pedido
            limpiarDatosCliente();
            pedidoactual = null;
            inicializarPedido();

            System.out.println("=== FIN: Pedido enviado a cocina exitosamente ===");
            return null;
        } catch (Exception e) {
            System.out.println("ERROR GENERAL: " + e.getMessage());
            e.printStackTrace();
            facesUtil.addErrorMessage("Error al procesar el pedido: " + e.getMessage());
            return null;
        }
    }

    /**
     * Limpia los datos del formulario de cliente
     */
    private void limpiarDatosCliente() {
        this.nombre = "";
        this.apellido = "";
        this.dni = "";
        this.telefono = "";
        this.email = "";
        this.observacion = "";
    }

    /**
     * Elimina un item del pedido
     */
    public void eliminarItemPedido(ItemPedido item) {
        System.out.println("=== DEBUG: eliminarItemPedido() llamado ===");
        System.out.println("Item a eliminar: " + (item != null ? item.toString() : "NULL"));
        System.out.println("Items en pedido antes de eliminar: " +
                (pedidoactual != null && pedidoactual.getItemPedido() != null ? pedidoactual.getItemPedido().size() : "NULL"));

        try {
            if (pedidoactual == null) {
                System.out.println("ERROR: pedidoactual es NULL");
                facesUtil.addErrorMessage("Error: pedidoactual es nulo");
                return;
            }

            if (item == null) {
                System.out.println("ERROR: item es NULL");
                facesUtil.addErrorMessage("Error: item es nulo");
                return;
            }

            // Método alternativo: buscar por producto y cantidad
            boolean eliminado = false;
            for (int i = 0; i < pedidoactual.getItemPedido().size(); i++) {
                ItemPedido currentItem = pedidoactual.getItemPedido().get(i);
                if (currentItem.getItem() != null && item.getItem() != null &&
                        currentItem.getItem().getProducto().getId().equals(item.getItem().getProducto().getId()) &&
                        currentItem.getCantidad().equals(item.getCantidad())) {

                    pedidoactual.getItemPedido().remove(i);
                    eliminado = true;
                    System.out.println("Item eliminado por índice: " + i);
                    break;
                }
            }

            if (!eliminado) {
                System.out.println("Intentando eliminar por equals()...");
                pedidoactual.eliminarItem(item);
            }

            pedidoactual.calcularTotal();
            System.out.println("Items en pedido después de eliminar: " + pedidoactual.getItemPedido().size());

            // Solo actualizar en BD si el pedido ya fue persistido
            if (pedidoactual.getId() != null) {
                pedidoFacade.actualizarPedido(pedidoactual);
                System.out.println("Pedido actualizado en BD");
            } else {
                System.out.println("Pedido no persistido, solo se actualizó en memoria");
            }

            facesUtil.addSuccessMessage("Producto eliminado del pedido");
            System.out.println("=== FIN: eliminación exitosa ===");

        } catch (Exception e) {
            System.out.println("ERROR en eliminación: " + e.getMessage());
            e.printStackTrace();
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
}
