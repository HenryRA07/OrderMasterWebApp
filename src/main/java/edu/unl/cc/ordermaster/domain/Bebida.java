package edu.unl.cc.ordermaster.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
public class Bebida extends Producto implements Serializable {

    public Bebida() {
        super();
    }

    public Bebida(Long id,@NotNull @NotEmpty String nombre, @NotNull @NotEmpty String descripcion) {
        super(id,nombre, descripcion);
    }

}
