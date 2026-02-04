package edu.unl.cc.ordermaster.domain;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public abstract class Producto {

    @NotBlank
    private String nombre;

    @NotBlank
    private String descripcion;

    public Producto() {
    }

    public Producto(@NotBlank String nombre, @NotBlank String descripcion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank String nombre) {
//        if(nombre == null || nombre.trim().isEmpty()){
//            throw new IllegalArgumentException("El nombre del producto es obligatorio");
//        }
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NotBlank String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(nombre, producto.nombre) && Objects.equals(descripcion, producto.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, descripcion);
    }

    @Override
    public String toString() {
        return "Alimento{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}' + "\n";
    }
}
