package edu.unl.cc.ordermaster.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate fechaCreacion;

    @NotNull @NotEmpty
    private String nombreMenu;

    @Enumerated(EnumType.STRING)
    private TipoMenu tipoMenu;
    //Extensiones

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<ItemMenu> itemMenu;

    public Menu() {
        fechaCreacion = LocalDate.now();
    }

    public Menu(Long id,@NotNull @NotEmpty String nombreMenu, TipoMenu tipoMenu) {
        this();
        this.id = id;
        this.nombreMenu = nombreMenu;
        this.tipoMenu = tipoMenu;
    }

    public void agregar(ItemMenu item) {
        if (itemMenu == null) {
            itemMenu = new ArrayList<>();
        }
        if (!this.itemMenu.contains(item)) {
            itemMenu.add(item);
        }
    }

    public void eliminar(ItemMenu item) {
        if (itemMenu == null) {
            throw new IllegalArgumentException("La lista de items no está inicializada");
        }
        if (item == null) {
            throw new IllegalArgumentException("El item a eliminar no puede ser nulo");
        }
        this.itemMenu.remove(item);
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fecharCreacion) {
        this.fechaCreacion = fecharCreacion;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(@NotNull @NotEmpty String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public TipoMenu getTipoMenu() {
        return tipoMenu;
    }

    public void setTipoMenu(TipoMenu tipoMenu) {
        this.tipoMenu = tipoMenu;
    }

    public List<ItemMenu> getItemMenu() {
        return itemMenu;
    }

    public void setItemMenu(List<ItemMenu> itemMenu) {
        if (itemMenu == null) {
            throw new IllegalArgumentException("La lista de items no ha sido inicializada");
        }
        this.itemMenu = itemMenu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Menu {" +
                "fecharCreacion=" + fechaCreacion +
                ", nombreMenu='" + nombreMenu + '\'' +
                ", tipoMenu=" + tipoMenu;
    }
}
