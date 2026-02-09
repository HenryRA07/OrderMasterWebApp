package edu.unl.cc.ordermaster.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class ItemMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precio;


    private boolean disponibilidad;
    //extensiones

    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST)
    private Producto producto;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public ItemMenu() {
    }

    public ItemMenu(Long id, @NotNull @Positive(message = "El precio debe ser mayor a 0")
                    BigDecimal precio,
                    @NotNull Producto producto) {
        this.id = id;
        setPrecio(precio);
        setProducto(producto);
        this.disponibilidad = true;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(@NotNull @Positive(message = "El precio debe ser mayor a 0") BigDecimal precio) {
//        if (precio.floatValue() <= 0) {
//            throw new IllegalArgumentException("El precio debe ser mayor a 0");
//        }
        this.precio = precio;
    }

    public boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Producto getProducto() {
        return producto;
    }


    public void setProducto(@NotNull Producto producto) {
//        if (producto == null) {
//            throw new IllegalArgumentException("El producto no puede ser nulo");
//        }
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "ItemMenu{" +
                "precio=" + precio +
                ", disponibilidad=" + disponibilidad +
                ", alimento=" + producto;
    }
}
