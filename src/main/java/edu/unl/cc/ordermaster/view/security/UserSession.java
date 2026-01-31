package edu.unl.cc.ordermaster.view.security;

import edu.unl.cc.ordermaster.business.SecurityFacade;
import edu.unl.cc.ordermaster.domain.security.ActionType;
import edu.unl.cc.ordermaster.domain.security.Role;
import edu.unl.cc.ordermaster.domain.security.User;
import edu.unl.cc.ordermaster.exception.EntityNotFoundException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotNull;


import java.io.Serial;
import java.util.Set;
import java.util.logging.Logger;

@Named
@SessionScoped
public class UserSession implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(UserSession.class.getName());

    @Inject
    private SecurityFacade securityFacade;

    private User user;

    public void postLogin(@NotNull User user) throws EntityNotFoundException {
        logger.info("User logged in: " + user.getName());
        this.user = user;
        Set<Role> roles = securityFacade.findRolesWithPermissionByUser(this.user.getId());
        user.setRoles(roles);
    }

    public boolean hasPermissionForPage(String pagePath) {
        return this.hasPermission(pagePath, ActionType.READ);
    }

    public boolean hasPermission(String resource, ActionType action) {
        if (resource.equals("/dashboard.xhtml")){
            return true;
        }
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.matchWith(resource, action));
    }

    public boolean hasRole(@NotNull String roleName){
        if (user == null){
            return false;
        }
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    public User getUser() {
        return user;
    }
}
