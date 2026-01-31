/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.domain.security;

import edu.unl.cc.ordermaster.domain.common.Organization;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user_")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    // Validaciones a nivel de vista y modelo
    @NotNull @NotEmpty @Size(min=5)
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @NotNull @NotEmpty
    private String password;

    // Relationships
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public User() {
        roles = new HashSet<>();
    }

    /**
     *
     * @param id
     * @param name
     * @param password
     * @throws IllegalArgumentException
     */
    public User(Long id,
                @NotNull @NotEmpty String name,
                @NotNull @NotEmpty String password) throws IllegalArgumentException{
        this();
        this.id = id;
        //validateNameRestriction(name);
        this.setName(name);
        this.setPassword(password);
    }

    public User(Long id,
                @NotNull @NotEmpty String name,
                @NotNull @NotEmpty String password,
                @NotNull Organization organization) {
        this(id, name, password);
        this.organization = organization;
    }

    /**
     * Validaciones a nivel de modelo y negocio
     * @param name
     * @throws IllegalArgumentException
     */
    private void validateNameRestriction(String name) throws IllegalArgumentException{
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException("Valor requerido");
        }

        if (name.trim().length() < 5){
            throw new IllegalArgumentException("Valor debe superar los 5 caracteres");
        }
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

    public void setName(@NotNull @NotEmpty @Size(min = 5) String name) {
        this.name = name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @NotEmpty String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

