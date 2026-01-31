package edu.unl.cc.ordermaster.domain.security;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;


@Entity
public class Permission implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Representa un recurso URI. Ej.: "/admin/usuarios"
     */
    @NotNull
    private String resource;

    @Enumerated(EnumType.STRING)
    private ActionType action;

    public Permission() {
    }

    public Permission(Long id, String resource, ActionType action) {
        this.id = id;
        this.resource = resource;
        this.action = action;
    }

    public boolean matchWith(String requestResource, ActionType requestAction) {
        return this.resource.equals(requestResource) &&
                (this.action.equals(ActionType.ALL) || this.action.equals(requestAction));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getResource(), that.getResource()) && getAction() == that.getAction();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getResource(), getAction());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Permission{");
        sb.append("id=").append(id);
        sb.append(", resource='").append(resource).append('\'');
        sb.append(", action=").append(action);
        sb.append('}');
        return sb.toString();
    }
}
