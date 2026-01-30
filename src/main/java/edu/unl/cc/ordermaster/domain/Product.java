/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    private boolean active;

    @NotNull
    private Category category;

    public Product() {
        this.price = BigDecimal.ZERO;
        this.active = true;
    }

    public Product(Long id, @NotEmpty String name, String description, @NotEmpty BigDecimal price) {
        this();
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // Getters y Setters
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotNull BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description == null) description = "";
        this.description = description.trim();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(@NotNull Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId()) && Objects.equals(getName(), product.getName()) && Objects.equals(getCategory(), product.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCategory());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Product{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", active=").append(active);
        sb.append(", category=").append(category);
        sb.append('}');
        return sb.toString();
    }
}
