package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.view.security.UserSession;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class NavigationController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(NavigationController.class.getName());

    @Inject
    UserSession userSession;

    // Mapeo de navegación por rol
    private static final Map<String, List<Map<String, Object>>> ROLE_NAVIGATION = Map.of(
            "ADMIN", List.of(
                    Map.of("label", "Menú Diario", "url", "/menuDiario.xhtml", "icon", "pi pi-calendar", "commandLink", false),
                    Map.of("label", "Gestión Usuarios", "url", "/usuario.xhtml", "icon", "pi pi-users", "commandLink", false)
            ),
            "MESERO", List.of(
                    Map.of("label", "Toma de Pedidos", "url", "/mesero.xhtml", "icon", "pi pi-pencil", "commandLink", false),
                    Map.of("label", "Menú del Día", "url", "/menuDiario.xhtml", "icon", "pi pi-book", "commandLink", false)
            ),
            "COCINA", List.of(
                    Map.of("label", "Pedidos Pendientes", "url", "/cocina.xhtml", "icon", "pi pi-clock", "commandLink", false)
            ),
            "CAJERO", List.of(
                    Map.of("label", "Cobros", "url", "/caja.xhtml", "icon", "pi pi-wallet", "commandLink", false)
            )
    );

    public List<Map<String, Object>> getNavigationItems() {
        if (userSession == null || userSession.getUser() == null) {
            logger.warning("Usuario no autenticado");
            return new ArrayList<>();
        }

        // Obtener el rol principal del usuario
        String primaryRole = getPrimaryRole();
        logger.info("Generando navegación para rol: " + primaryRole);

        List<Map<String, Object>> originalItems = ROLE_NAVIGATION.getOrDefault(primaryRole, new ArrayList<>());
        
        // Crear copias MUTABLES de los items
        List<Map<String, Object>> mutableItems = new ArrayList<>();
        for (Map<String, Object> originalItem : originalItems) {
            Map<String, Object> mutableItem = new HashMap<>(originalItem);
            mutableItems.add(mutableItem);
        }

        // Marcar el item activo según la página actual
        markActiveItem(mutableItems);

        return mutableItems;
    }

    private String getPrimaryRole() {
        if (userSession.getUser().getRoles() == null || userSession.getUser().getRoles().isEmpty()) {
            return "MESERO"; // Rol por defecto
        }

        // Usar la misma lógica de prioridad que RoleNavigationService
        List<String> rolePriority = List.of("ADMIN", "CAJERO", "COCINA", "MESERO");

        for (String priorityRole : rolePriority) {
            if (userSession.hasRole(priorityRole)) {
                return priorityRole;
            }
        }

        return userSession.getUser().getRoles().iterator().next().getName();
    }


    private void markActiveItem(List<Map<String, Object>> items) {
        String currentView = getCurrentViewId();
        logger.info("Vista actual: " + currentView);

        for (Map<String, Object> item : items) {
            String itemUrl = (String) item.get("url");
            if (itemUrl != null && !itemUrl.equals("#")) {
                // Normalizar URLs para comparación
                if (currentView.contains(itemUrl) ||
                        currentView.equals(itemUrl) ||
                        currentView.equals(itemUrl.replace("/", ""))) {
                    item.put("active", true);
                    logger.info("Item activo marcado: " + item.get("label"));
                } else {
                    item.put("active", false);
                }
            } else {
                item.put("active", false);
            }
        }
    }

    private String getCurrentViewId() {
        try {
            return jakarta.faces.context.FacesContext.getCurrentInstance()
                    .getViewRoot().getViewId();
        } catch (Exception e) {
            logger.warning("No se pudo obtener la vista actual: " + e.getMessage());
            return "";
        }
    }
}

