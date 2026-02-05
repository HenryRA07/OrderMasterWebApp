package edu.unl.cc.ordermaster.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
public class Platillo extends Producto  implements Serializable {

    public Platillo() {
        super();
    }

    public Platillo(Long id,@NotNull @NotEmpty  String nombre,@NotNull @NotEmpty  String descripcion) {
        super(id, nombre, descripcion);
    }
}
