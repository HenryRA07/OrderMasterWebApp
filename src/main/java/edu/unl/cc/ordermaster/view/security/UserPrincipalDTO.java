/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.view.security;

import edu.unl.cc.ordermaster.domain.security.ActionType;
import edu.unl.cc.ordermaster.domain.security.User;
import jakarta.validation.constraints.NotNull;


import java.io.Serializable;
import java.security.Principal;

public class UserPrincipalDTO implements Principal, Serializable {

    private final User user;

    public UserPrincipalDTO(@NotNull User user) {
        this.user = user;
    }

    public boolean hasPermissionForPage(String pagePath) {
        return this.hasPermission(pagePath, ActionType.READ);
    }

    public boolean hasPermission(String resource, ActionType action) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.matchWith(resource, action));
    }

    @Override
    public String getName() {
        return user.getName();
    }
}
