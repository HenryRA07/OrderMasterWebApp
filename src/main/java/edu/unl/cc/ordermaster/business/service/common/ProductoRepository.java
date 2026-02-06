package edu.unl.cc.ordermaster.business.service.common;

import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.Producto;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class ProductoRepository {
    @Inject
    private CrudGenericService crudGenericService;
    @PersistenceContext
    private EntityManager em;
    public List<Producto> findAll(Long id,String nombre, String descripcion) {
        String Jpql = "SELECT p FROM Producto p " +
                "LEFT JOIN FETCH p.id i" +
                "WHERE p.nombre = :nombre AND p.descripcion = :descripcion ";
        TypedQuery<Producto> query = em.createQuery(Jpql, Producto.class);
        query.setParameter("nombre", nombre);
        query.setParameter("descripcion", descripcion);
        return query.getResultList();
    }
}
