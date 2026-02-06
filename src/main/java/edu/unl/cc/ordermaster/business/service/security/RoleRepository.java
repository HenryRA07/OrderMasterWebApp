package edu.unl.cc.ordermaster.business.service.security;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.security.Role;
import edu.unl.cc.ordermaster.exception.EntityNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wduck (Wilman Chamba Z)
 */
@Stateless
public class RoleRepository {

    @Inject
    private CrudGenericService crudService;

    public Set<Role> findAllWithPermissions(){
        return new HashSet<>(crudService.findWithQuery("Select * from role"));
    }

    public Role find(String name) throws EntityNotFoundException {
        String sqlNative = "SELECT * FROM Role WHERE LOWER(name) LIKE LOWER(?)";
        Query query = crudService.createNativeQuery(sqlNative, Role.class);
        query.setParameter(1, name.toLowerCase());
        Role entity = (Role)crudService.findSingleResultOrNullWithQuery(query);
        if (entity != null)
            return entity;
        throw new EntityNotFoundException("Role not found to name [" + name + "]");
    }


}
