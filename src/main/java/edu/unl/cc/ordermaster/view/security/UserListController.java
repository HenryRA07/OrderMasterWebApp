/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.view.security;

import edu.unl.cc.ordermaster.business.SecurityFacade;
import edu.unl.cc.ordermaster.domain.security.User;
import edu.unl.cc.ordermaster.exception.EntityNotFoundException;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Named (value = "userList")
@ViewScoped
public class UserListController implements java.io.Serializable{

    private static Logger logger = Logger.getLogger(UserListController.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private String criteria;
    private List<User> users;

    @Inject
    SecurityFacade securityFacade;

    public UserListController() {
        users = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        logger.info("****** POST CONSTRUCTOR: " + getCriteria() + " ******");
        this.search();
    }

    public void search()  {
        try {
            logger.info("****** Ingreso a buscar con: " + getCriteriaBuffer() + " ******");
            users = securityFacade.findUsers(getCriteriaBuffer());
        } catch (EntityNotFoundException e) {
            users.clear();
        }
    }

    public String edit(User _selected){
        //FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selected", _selected);
        System.out.println("Editando usuario: " + _selected.getName() + " con id: " + _selected.getId());
        return "userEdit.xhtml?faces-redirect=true&id=" + _selected.getId();
    }

    public void reset() {
        criteria = null;
        users.clear();
    }

    private String getCriteriaBuffer(){
        return criteria != null && !criteria.isEmpty()? criteria : "%";
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
