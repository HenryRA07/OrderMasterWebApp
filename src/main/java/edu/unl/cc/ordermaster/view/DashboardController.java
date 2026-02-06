/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.business.JBrewFacade;
import edu.unl.cc.ordermaster.domain.Producto;
import edu.unl.cc.ordermaster.view.security.UserSession;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


import java.util.List;

@Named
@ViewScoped
public class DashboardController implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @Inject
    private UserSession userSession;

    @Inject
    private JBrewFacade jBrewFacade;


    public DashboardController(){
    }

    public List<Producto> getAllProducts(){
        return jBrewFacade.getAllProducts();
    }


}

