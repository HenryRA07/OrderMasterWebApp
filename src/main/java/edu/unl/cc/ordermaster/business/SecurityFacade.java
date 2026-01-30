/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.business;

import edu.unl.cc.ordermaster.business.service.RoleRepository;
import edu.unl.cc.ordermaster.business.service.UserRepository;
import edu.unl.cc.ordermaster.domain.security.User;
import edu.unl.cc.ordermaster.exception.AlreadyEntityException;
import edu.unl.cc.ordermaster.exception.CredentialInvalidException;
import edu.unl.cc.ordermaster.exception.EncryptorException;
import edu.unl.cc.ordermaster.exception.EntityNotFoundException;
import edu.unl.cc.ordermaster.util.EncryptorManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.*;

@Stateless
public class SecurityFacade implements Serializable {

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    public User createUser(User user) throws EncryptorException, AlreadyEntityException {
        String pwdEncripted = EncryptorManager.encrypt(user.getPassword());
        user.setPassword(pwdEncripted);
        try {
            userRepository.find(user.getName());
        } catch (EntityNotFoundException e) {
            User userPersisted = userRepository.save(user);
            return userPersisted;
        }
        throw new AlreadyEntityException("Usuario ya existe");
    }

    public User updateUser(User user) throws AlreadyEntityException, EncryptorException {
        if (user.getId() == null){
            return createUser(user);
        }
        String pwdEncrypted = EncryptorManager.encrypt(user.getPassword());
        user.setPassword(pwdEncrypted);
        try {
            User userFound = userRepository.find(user.getName());
            if  (!userFound.getId().equals(user.getId())){
                throw new AlreadyEntityException("Ya existe otro usuario con ese nombre");
            }
        } catch (EntityNotFoundException ignored) {
        }
        return userRepository.save(user);
    }

    public User authenticate(String name, String password) throws CredentialInvalidException {
        try {
            User userFound = userRepository.find(name);
            String pwdEncrypted = EncryptorManager.encrypt(password);
            if (pwdEncrypted.equals(userFound.getPassword())){
                return userFound;
            }
            throw new CredentialInvalidException();
        } catch (EntityNotFoundException e) {
            throw new CredentialInvalidException();
        } catch (EncryptorException e) {
            throw new CredentialInvalidException("Credenciales incorrectas", e);
        }
    }

    public Set<Role> findAllRolesWithPermission(){
        return roleRepository.findAllWithPermissions();
    }

    public Set<Role> findRolesWithPermissionByUser(Long userId) throws EntityNotFoundException {
        User user = userRepository.find(userId);
        // Simulación de usuarios con rol ADMIN
        Role  role = roleRepository.find("ADMIN");
        Set<Role> roles = new LinkedHashSet<>();
        roles.add(role);
        return roles;
    }


    public List<User> findUsers(String criteria) throws EntityNotFoundException {
        return userRepository.findWithLike(criteria);
    }

    public User findUser(Long userId) throws EntityNotFoundException {
        return  userRepository.find(userId);
    }



}

