/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.domain.security.User;
import edu.unl.cc.ordermaster.view.security.UserSession;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.logging.Logger;

@WebFilter("*.xhtml")
public class AuthorizationFilter implements Filter {

    private static Logger logger = Logger.getLogger(AuthorizationFilter.class.getName());

    @Inject
    UserSession userSession;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        // Obtener la ruta solicitada
        String requestPath = httpReq.getRequestURI().substring(httpReq.getContextPath().length());

        //logger.info("-----> Request path: " + requestPath);
        System.out.println("-----> Request path: " + requestPath);

        // 1. Permitir recursos públicos
        if (requestPath.startsWith("/public/") || requestPath.equals("/login.xhtml") || requestPath.equals("/index.xhtml")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 2. Obtener usuario autenticado
        //UserPrincipal user = FacesUtil.getCurrentUser();
        User user = userSession.getUser();

        // 3. Redirigir si no está autenticado
        if (user == null) {
            ((HttpServletResponse) servletResponse).sendRedirect(httpReq.getContextPath() + "/login.xhtml");
            return;
        }

        // 4. Verificar permisos para la página solicitada
        //logger.info("-----> userSession.hasPermissionForPage: " + requestPath + " return:  " + userSession.hasPermissionForPage(requestPath));
        System.out.println("-----> userSession.hasPermissionForPage: " + requestPath + " return:  " + userSession.hasPermissionForPage(requestPath));

        if (userSession.hasPermissionForPage(requestPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
        }

    }
}


