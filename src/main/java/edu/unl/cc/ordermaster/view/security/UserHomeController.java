/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.view.security;

import edu.unl.cc.ordermaster.business.SecurityFacade;
import edu.unl.cc.ordermaster.domain.common.Person;
import edu.unl.cc.ordermaster.domain.security.User;
import edu.unl.cc.ordermaster.exception.EntityNotFoundException;
import edu.unl.cc.ordermaster.faces.FacesUtil;
import edu.unl.cc.ordermaster.util.EncryptorManager;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.util.logging.Logger;

@Named(value = "userHome")
@ViewScoped
public class UserHomeController implements java.io.Serializable{

    private static Logger logger = Logger.getLogger(UserHomeController.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private Long selectedUserId;

    private User user;

    @Inject
    SecurityFacade securityFacade;


    public UserHomeController() {
    }

    public void loadUser() {
        logger.info("Loading user with id: " + selectedUserId);
        if (selectedUserId != null) {
            try {
                user = securityFacade.findUser(selectedUserId);
            } catch (EntityNotFoundException e) {
                FacesUtil.addErrorMessage("No se pudo encontrar el usuario con id: " + selectedUserId);
                user = new User();
            }
        } else {
            user = new User();
        }
        // Provisional, debe recuperarse desde la BD
        if (user.getOrganization() == null) {
            user.setOrganization(new Person());
        }
        decryptPassword(user);
    }

    private void decryptPassword(User user){
        String pwdDecrypted = null;
        try {
            if (user.getPassword() != null && !user.getPassword().isEmpty()){
                logger.info("Password no nulo y no vacio: " + user.getPassword());
                pwdDecrypted = EncryptorManager.decrypt(user.getPassword());
                user.setPassword(pwdDecrypted);
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage(e.getMessage(), "Invconveniente al decifrar la clave: " + e.getMessage());
        }

    }

    public String create() {
        try {
            user = securityFacade.createUser(user);
            //decryptPassword(user);
            FacesUtil.addSuccessMessageAndKeep("Usuario creado correctamente");
            return "userList?faces-redirect=true";
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Inconveniente al crear usuario: " + e.getMessage());
            return null;
        }
    }

    public String update() {
        try {
            securityFacade.updateUser(user);
            //decryptPassword(user);
            FacesUtil.addSuccessMessageAndKeep("Usuario actualizado correctamente");
            return "userList?faces-redirect=true";
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Inconveniente al actualizar usuario: " + e.getMessage());
            return null;
        }
    }

    public boolean isManaged(){
        return this.user.getId() != null;
    }

    public Long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

