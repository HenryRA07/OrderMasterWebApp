package edu.unl.cc.ordermaster.business.service.security;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotNull;
import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.security.User;
import edu.unl.cc.ordermaster.exception.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class UserRepository {

    @Inject
    private CrudGenericService crudService;
    @PersistenceContext//(name = "JbrewPU", unitName = "JbrewPU")
    private EntityManager em;

    public User save(User user){
            return crudService.create(user);
    }

    public User find(@NotNull Long id) throws EntityNotFoundException {
        User user = crudService.find(User.class, id);
        if (user == null){
            throw new EntityNotFoundException("User no encontrado con [" + id + "]");
        }
        return user;
    }

    public User find(@NotNull String name) throws EntityNotFoundException{
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        User userFound = crudService.findSingleResultOrNullWithNamedQuery("User.findLikeName", params);
        if (userFound == null){
            throw new EntityNotFoundException("User no encontrado con [" + name + "]");
        }
        return userFound;
    }

    public List<User> findWithLike(@NotNull String name) throws EntityNotFoundException{
        Map<String, Object> params = new HashMap<>();
        params.put("name", "%" + name + "%");
        return crudService.findWithNamedQuery("User.findLikeName", params);
    }

    //metodo provicionar por tiempo
    public User findWithRoles(@NotNull String name) throws EntityNotFoundException {
        Map<String, Object> params = new HashMap<>();
        params.put("username", name);
        User userFound = crudService.findSingleResultOrNullWithNamedQuery("User.findWithRoles", params);
        if (userFound == null) {
            throw new EntityNotFoundException("Usuario no encontrado: " + name);
        }
        return userFound;
    }

    //metodo provicionar por tiempo
    public User findWithRoles(Long userId) throws EntityNotFoundException {
        String jpql = "SELECT DISTINCT u FROM User u " +
                "LEFT JOIN FETCH u.roles " +
                "WHERE u.id = :userId";

        try {
            return em.createQuery(jpql, User.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Usuario no encontrado ID: " + userId);
        }
    }


}
