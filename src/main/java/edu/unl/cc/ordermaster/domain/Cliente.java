package edu.unl.cc.ordermaster.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String nombre;

    @NotNull @NotEmpty
    private String apellido;

    @NotNull @NotEmpty
    private String dni;

    @NotNull @NotEmpty
    private String telefono;

    @NotNull @NotEmpty
    @Email(message = "Formato de email incorrecto")
    private String email;

    public Cliente() {
    }

    public Cliente(Long id, @NotNull @NotEmpty String nombre,
                   @NotNull @NotEmpty String apellido,
                   @NotNull @NotEmpty String dni,
                   @NotNull @NotEmpty String telefono,
                   @NotNull @NotEmpty String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
    }

    public String getNombreCompleto(){
        return getNombre() + " " + getApellido();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NotNull @NotEmpty String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(@NotNull @NotEmpty String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(@NotNull @NotEmpty String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NotNull @NotEmpty String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @NotEmpty String email) {
        this.email = email;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(nombre, cliente.nombre) && Objects.equals(apellido, cliente.apellido) && Objects.equals(dni, cliente.dni) && Objects.equals(telefono, cliente.telefono) && Objects.equals(email, cliente.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, apellido, dni, telefono, email);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
