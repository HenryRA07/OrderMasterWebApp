/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.domain.security;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Role implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;

    // Validaciones a nivel de vista y modelo
    @NotNull
    @NotEmpty
    private String name;

    private Set<Permission> permissions;

    public Role() {
        permissions = new HashSet<>();
    }

    public Role(Long id, @NotNull @NotEmpty String name) {
        this();
        this.id = id;
        this.setName(name);
    }
    public void add(Permission permission){
        if(permission !=null){
            if(!getPermissions().contains(permission)){
                this.permissions.add(permission);
            }
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

    public void setName(@NotNull @NotEmpty String name) {
        this.name = name.trim();
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(getId(), role.getId()) && Objects.equals(getName(), role.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Role{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

