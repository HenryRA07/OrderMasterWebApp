package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.business.service.MenuFacade;
import edu.unl.cc.ordermaster.domain.*;
import edu.unl.cc.ordermaster.faces.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class MenuController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private MenuFacade menuFacade;
    
    @Inject
    private FacesUtil facesUtil;

    private String descripcion;

    private BigDecimal precio;

    private String nombre;

    private String nombreMenu;

    private TipoMenu tipoMenu;

    private boolean disponibilidad = true;

    private Menu menu;

    /** Tipo de producto: PLATILLO o BEBIDA */
    private String tipoProductoSeleccionado = "PLATILLO";

    /** Producto seleccionado del catálogo para agregar con precio */
    private Producto productoSeleccionado;
    private BigDecimal precioProductoCatalogo;

    @PostConstruct
    public void init(){
        inicializarValoresPorDefecto();
    }

    private void inicializarValoresPorDefecto() {
        if (precio == null) {
            precio = BigDecimal.valueOf(1.0);
        }
        if (disponibilidad == false) {
            disponibilidad = true;
        }
    }

    public void inicializarMenu(){
        if(menu == null){
            menu = new Menu();
        }
        menu.setTipoMenu(tipoMenu);
        menu.setNombreMenu(nombreMenu);

    }

    /**
     * Agrega un producto nuevo al menú (en memoria). Se persiste al confirmar el menú.
     */
    public void agregarProductoMenu(){
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                facesUtil.addErrorMessage("El nombre del producto es obligatorio");
                return;
            }
            if (descripcion == null || descripcion.trim().isEmpty()) {
                facesUtil.addErrorMessage("La descripción del producto es obligatoria");
                return;
            }
            if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
                facesUtil.addErrorMessage("El precio debe ser mayor a 0");
                return;
            }
            if (nombreMenu == null || nombreMenu.trim().isEmpty()) {
                facesUtil.addErrorMessage("El nombre del menú es obligatorio");
                return;
            }
            if (tipoMenu == null) {
                facesUtil.addErrorMessage("El tipo de menú es obligatorio");
                return;
            }

            if (menu == null) {
                inicializarMenu();
            }

            Producto productoitem = producto(tipoProductoSeleccionado);
            productoitem.setDescripcion(descripcion);
            productoitem.setNombre(nombre);

            ItemMenu itemMenu = new ItemMenu();
            itemMenu.setPrecio(precio);
            itemMenu.setProducto(productoitem);
            itemMenu.setDisponibilidad(disponibilidad);

            menu.agregar(itemMenu);

            facesUtil.addSuccessMessage("Producto agregado al menú: " + nombre);
            reiniciarParametros();

        } catch (Exception e) {
            facesUtil.addErrorMessage("Error al agregar producto: " + e.getMessage());
        }
    }

    /**
     * Agrega un producto existente del catálogo al menú con el precio indicado.
     */
    public void agregarProductoDelCatalogo(){
        try {
            if (productoSeleccionado == null) {
                facesUtil.addErrorMessage("Seleccione un producto del catálogo");
                return;
            }
            if (precioProductoCatalogo == null || precioProductoCatalogo.compareTo(BigDecimal.ZERO) <= 0) {
                facesUtil.addErrorMessage("El precio debe ser mayor a 0");
                return;
            }
            if (nombreMenu == null || nombreMenu.trim().isEmpty()) {
                facesUtil.addErrorMessage("El nombre del menú es obligatorio");
                return;
            }
            if (tipoMenu == null) {
                facesUtil.addErrorMessage("El tipo de menú es obligatorio");
                return;
            }

            if (menu == null) {
                inicializarMenu();
            }

            ItemMenu itemMenu = new ItemMenu();
            itemMenu.setPrecio(precioProductoCatalogo);
            itemMenu.setProducto(productoSeleccionado);
            itemMenu.setDisponibilidad(true);

            menu.agregar(itemMenu);

            facesUtil.addSuccessMessage("Producto agregado al menú: " + productoSeleccionado.getNombre());
            productoSeleccionado = null;
            precioProductoCatalogo = BigDecimal.ZERO;

        } catch (Exception e) {
            facesUtil.addErrorMessage("Error al agregar producto: " + e.getMessage());
        }
    }

    /**
     * Elimina un item del menú actual (en memoria).
     */
    public void eliminarItemDelMenu(ItemMenu item) {
        if (menu != null && item != null) {
            menu.eliminar(item);
            facesUtil.addSuccessMessage("Producto eliminado del menú");
        }
    }

    /**
     * Prepara la selección de un producto del catálogo para agregarlo.
     */
    public void prepararAgregarDelCatalogo(Producto producto) {
        this.productoSeleccionado = producto;
        this.precioProductoCatalogo = BigDecimal.ZERO;
    }

    private Producto producto(String tipo){
        if (tipo == null) return new Platillo();
        return switch (tipo.toUpperCase()) {
            case "BEBIDA" -> new Bebida();
            case "PLATILLO" -> new Platillo();
            default -> new Platillo();
        };
    }

    /**
     * Guarda el menú completo en la base de datos
     */
    public void guardarMenu() {
        try {
            if (menu == null || menu.getItemMenu() == null || menu.getItemMenu().isEmpty()) {
                facesUtil.addErrorMessage("El menú está vacío. Agregue productos primero.");
                return;
            }

            menuFacade.crearMenu(menu);
            facesUtil.addSuccessMessage("Menú '" + menu.getNombreMenu() + "' guardado exitosamente");
            
            // Crear nuevo menú para seguir agregando
            menu = null;
            
        } catch (Exception e) {
            facesUtil.addErrorMessage("Error al guardar menú: " + e.getMessage());
        }
    }

    private void reiniciarParametros() {
        nombre = "";
        descripcion = "";
        precio = BigDecimal.valueOf(1.0);
        disponibilidad = true;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public TipoMenu getTipoMenu() {
        return tipoMenu;
    }

    public void setTipoMenu(TipoMenu tipoMenu) {
        this.tipoMenu = tipoMenu;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public List<ItemMenu> getItemsDelMenu() {
        if (menu == null || menu.getItemMenu() == null) {
            return new ArrayList<>();
        }
        return menu.getItemMenu();
    }

    public List<Producto> getProductosExistentes() {
        return menuFacade.obtenerTodosLosProductos();
    }

    public String getTipoProductoSeleccionado() {
        return tipoProductoSeleccionado;
    }

    public void setTipoProductoSeleccionado(String tipoProductoSeleccionado) {
        this.tipoProductoSeleccionado = tipoProductoSeleccionado;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public BigDecimal getPrecioProductoCatalogo() {
        return precioProductoCatalogo;
    }

    public void setPrecioProductoCatalogo(BigDecimal precioProductoCatalogo) {
        this.precioProductoCatalogo = precioProductoCatalogo;
    }

    public String getFechaActualFormateada() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy"));
    }

    public Menu getMenu() {
        return menu;
    }
}
