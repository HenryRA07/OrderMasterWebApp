package edu.unl.cc.ordermaster.view;


import edu.unl.cc.ordermaster.domain.Cliente;
import edu.unl.cc.ordermaster.domain.ItemMenu;
import edu.unl.cc.ordermaster.domain.ItemPedido;
import edu.unl.cc.ordermaster.domain.Pedido;
import jakarta.inject.Named;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Named
public class PedidoController {

    private Integer cantidad;
    private String observacion;
    private BigDecimal precio;
    private Integer mesa;
    private String nombre;

    private String apellido;

    private String dni;

    private String telefono;

    private String email;


    private Pedido pedidoactual;
    private Cliente clientepedido;

    public List<ItemMenu> itemsMenu;
    private ItemMenu itemseleccionado;

    public void inicilizarItems(){
        //rescatar items desde la base de datos
        //itemsMenu =
    }

    public void seleccionarItem(ItemMenu itemMenu){
        this.itemseleccionado = itemMenu;
        this.cantidad = 1;
        this.observacion = "";
    }

    public void agregarItemPedido(){
        ItemPedido item = new ItemPedido();
        item.setItem(itemseleccionado);
        item.setCantidad(cantidad);
        item.setObservacion(observacion);
        pedidoactual.setMesa(mesa);
        pedidoactual.setCliente(clientepedido);

        //agrgar para guardar en base de datos
        pedidoactual.agregarItem(item);
        limpiarSeleccion();
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
}
