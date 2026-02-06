package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.domain.*;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Named
@ViewScoped
public class MenuController {

    private String descripcion;

    private BigDecimal precio;

    private String nombre;

    private String nombreMenu;

    private TipoMenu tipoMenu;

    private boolean disponibilidad;

    private Menu menu;

    public void init(){
        //inicializar items desde la base de datos
//        if (menu.getId() != null) {
//            menu = menuService.buscarPorId(menu.getId()); // ✓ AÑADIR
//            itemsMenu = menu.getItems(); // ✓ AÑADIR
//        }
    }

    public void inicializarMenu(){
        if(menu == null){
            menu = new Menu();
        }
        menu.setTipoMenu(tipoMenu);
        menu.setNombreMenu(nombreMenu);

    }

    public void agregarProductoMenu(String tipo){
        Producto productoitem = producto(tipo);
        productoitem.setDescripcion(descripcion);
        productoitem.setNombre(nombre);
        ItemMenu itemMenu = new ItemMenu();
        itemMenu.setPrecio(precio);
        itemMenu.setProducto(productoitem);
        menu.agregar(itemMenu);
        reiniciarParametros();

    }

    private Producto producto(String tipo){
        return switch (tipo) {
            case "BEBIDA" -> new Bebida();
            case "PLATILLO" -> new Platillo();
            default -> throw new IllegalArgumentException("Tipo no válido");
        };

    }

    private void reiniciarParametros() {
        nombre = "";
        descripcion = "";
        precio = BigDecimal.valueOf(1);
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
}
