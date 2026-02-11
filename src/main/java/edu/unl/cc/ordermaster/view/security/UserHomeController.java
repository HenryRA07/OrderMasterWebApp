package edu.unl.cc.ordermaster.view.security;

import edu.unl.cc.ordermaster.business.SecurityFacade;
import edu.unl.cc.ordermaster.domain.common.GenderType;
import edu.unl.cc.ordermaster.domain.common.Organization;
import edu.unl.cc.ordermaster.domain.common.Person;
import edu.unl.cc.ordermaster.domain.security.Role;
import edu.unl.cc.ordermaster.domain.security.User;
import edu.unl.cc.ordermaster.exception.EntityNotFoundException;
import edu.unl.cc.ordermaster.faces.FacesUtil;
import edu.unl.cc.ordermaster.util.EncryptorManager;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Named(value = "userHome")
@ViewScoped
public class UserHomeController implements java.io.Serializable{

    private static Logger logger = Logger.getLogger(UserHomeController.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private Long selectedUserId;

    private User user;
    private String firstname;
    private String lastname;
    private String rol;
    private String email;
    private String DNI;

    @Inject
    SecurityFacade securityFacade;

    @Inject
    FacesUtil facesUtil;

    Person person;
    Role role;

    public UserHomeController() {
    }

    @PostConstruct
    public void init() throws EntityNotFoundException {
        person = new Person();
        user = new User();
        user.setOrganization(person);
        role = new Role();
    }


    public void create() {
        try {
            if (person instanceof Person p) {
                p.setFirstName(firstname);
                p.setLastName(lastname);
                p.setGender(GenderType.FEMALE);
            }
            person.setIdentificationNumber(DNI);
            person.setEmail(email);
            person.setName(firstname +" "+ lastname);
            person.setId(10L);
            user.setOrganization(person);

            role.setName(rol);
            role.setId(10L);
            user.setRoles(new HashSet<>(Set.of(role)));
            user.setId(10L);
            securityFacade.createUser(user);
            //decryptPassword(user);
            facesUtil.addSuccessMessageAndKeep("Usuario creado correctamente");
            reiniciarParametros();
        } catch (Exception e) {
            facesUtil.addErrorMessage("Inconveniente al crear usuario: " + e.getMessage());
        }

    }

    public void update() {
        try {
            securityFacade.updateUser(user);
            //decryptPassword(user);
            facesUtil.addSuccessMessageAndKeep("Usuario actualizado correctamente");
        } catch (Exception e) {
            facesUtil.addErrorMessage("Inconveniente al actualizar usuario: " + e.getMessage());
        }
    }

    public boolean isManaged(){
        if (this.user == null || this.user.getId() == null) {
            return false;
        }
        return true;
    }

    public void reiniciarParametros(){
        this.user = new User();
        this.person = new Person();
        this.user.setOrganization(person);
        this.role = new Role();
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.rol = null;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }
}

