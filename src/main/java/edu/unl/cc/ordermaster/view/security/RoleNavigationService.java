package edu.unl.cc.ordermaster.view.security;

import edu.unl.cc.ordermaster.domain.security.Role;
import edu.unl.cc.ordermaster.domain.security.User;
import jakarta.ejb.Stateless;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

//clase provicionar por tiempo
@Stateless
public class RoleNavigationService implements Serializable {
    private static final Logger logger = Logger.getLogger(RoleNavigationService.class.getName());

    // Mapeo directo de roles a páginas
    private static final Map<String, String> ROLE_HOME_PAGES = Map.of(
            "ADMINISTRADOR", "/admin/dashboard.xhtml",
            "MESERO", "/mesa/pedidos.xhtml",
            "COCINERO", "/cocina/ordenes.xhtml",
            "CAJERO", "/caja/facturacion.xhtml"
    );

    // Prioridad de roles (si un usuario tiene múltiples roles)
    private static final List<String> ROLE_PRIORITY = List.of(
            "ADMINISTRADOR",  // Mayor prioridad
            "CAJERO",
            "COCINERO",
            "MESERO"          // Menor prioridad
    );

    /**
     * Determina la página principal basada únicamente en roles
     */
    public String getHomePageByRoles(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            logger.warning("Usuario sin roles asignados");
            return "/dashboard.xhtml?faces-redirect=true";
        }

        // Convertir a nombres de roles
        Set<String> roleNames = roles.stream()
                .map(Role::getName)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        logger.info("Roles del usuario: " + roleNames);

        // Buscar por prioridad
        for (String priorityRole : ROLE_PRIORITY) {
            if (roleNames.contains(priorityRole)) {
                String homePage = ROLE_HOME_PAGES.get(priorityRole);
                logger.info("Redirigiendo a: " + homePage + " por rol: " + priorityRole);
                return homePage + "?faces-redirect=true";
            }
        }

        // Si tiene un rol no configurado, usar el primero
        String firstRole = roleNames.iterator().next();
        logger.warning("Rol no configurado: " + firstRole);
        return "/dashboard.xhtml?faces-redirect=true";
    }

    /**
     * Versión simplificada para un solo rol principal
     */
    public String getHomePageByPrimaryRole(User user) {
        if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
            return "/login.xhtml?faces-redirect=true";
        }

        // Tomar el primer rol (asumiendo que cada usuario tiene un rol principal)
        String primaryRole = user.getRoles().iterator().next().getName().toUpperCase();

        return ROLE_HOME_PAGES.getOrDefault(primaryRole, "/dashboard.xhtml")
                + "?faces-redirect=true";
    }

    /**
     * Verifica si un usuario tiene un rol específico
     */
    public boolean hasRole(User user, String roleName) {
        if (user == null || user.getRoles() == null) return false;

        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }
}
