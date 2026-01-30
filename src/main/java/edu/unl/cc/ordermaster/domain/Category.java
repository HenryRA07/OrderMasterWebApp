/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.domain;

import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public class Category implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotEmpty
    private String name;

    public Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.setName(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotEmpty String name) {
        this.name = name.trim().toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId()) && Objects.equals(getName(), category.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Category{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

