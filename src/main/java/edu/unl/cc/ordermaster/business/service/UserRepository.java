package edu.unl.cc.ordermaster.business.service;

import jakarta.ejb.Stateless;
import edu.unl.cc.ordermaster.domain.security.User;
import edu.unl.cc.ordermaster.exception.EntityNotFoundException;

import java.util.*;

@Stateless
public class UserRepository {
    private static final Map<Long, User> tableUserBD;
    private static Long sequenceId;

    static {
        tableUserBD = new TreeMap<>();
        sequenceId = 1L;

        tableUserBD.put(1L, new User(1L, "admin", "uVQoLxtZvlhBuamIlWRLGQ=="));

    }

    public UserRepository() {
    }

    public User save(User user){
        // Simulacion de guardar en bd;
        if (user.getId() == null){
            sequenceId++;
            user.setId(sequenceId);
            tableUserBD.put(user.getId(), user);
        } else {
            tableUserBD.replace(user.getId(), user);
        }
        return tableUserBD.get(user.getId());
    }

    public User find(Long id) throws EntityNotFoundException {
        User user = tableUserBD.get(id);
        if (user == null){
            throw new EntityNotFoundException("User no encontrado con [" + id + "]");
        }
        return user;
    }

    public User find(String name) throws EntityNotFoundException{
        User userFound = null;
        for (Map.Entry<Long, User> entry : tableUserBD.entrySet()) {
            User user = entry.getValue();
            if (Objects.equals(user.getName().toLowerCase(), name.toLowerCase())){
                userFound = entry.getValue();
                break;
            }
        }
        if (userFound == null){
            throw new EntityNotFoundException("User no encontrado con [" + name + "]");
        }
        return userFound;
    }

    public List<User> findWithLike(String name) throws EntityNotFoundException{
        List<User> userFounds = new ArrayList<>();
        for (Map.Entry<Long, User> entry : tableUserBD.entrySet()) {
            User user = entry.getValue();
            if (user.getName().contains(name)){
                userFounds.add(user);
            }
        }
        return userFounds;
    }

    public static int getTableUserSize() {
        return tableUserBD.size();
    }

}
