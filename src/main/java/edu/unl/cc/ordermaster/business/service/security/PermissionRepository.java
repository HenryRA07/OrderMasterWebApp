package edu.unl.cc.ordermaster.business.service.security;

/**
 * @author wduck (Wilman Chamba Z)
 */

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.security.Permission;
import edu.unl.cc.ordermaster.exception.EntityNotFoundException;

import java.util.List;

@Stateless
public class PermissionRepository {

    @Inject
    private CrudGenericService crudService;

    public List<Permission> findAll(){
        return crudService.findWithNativeQuery("Select * from permission", Permission.class);
    }

    public Permission find(Long id) throws EntityNotFoundException {
        Permission entity = crudService.find(Permission.class, id);
        if (entity != null){
            return entity;
        }
        throw new EntityNotFoundException("Permission not found [" + id + "]");
    }

}
