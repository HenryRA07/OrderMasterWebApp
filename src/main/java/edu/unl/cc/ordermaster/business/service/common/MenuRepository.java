package edu.unl.cc.ordermaster.business.service.common;

import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.Menu;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class MenuRepository {
    @Inject
    private CrudGenericService crudgeneric;
    @PersistenceContext
    private EntityManager em;

    /**
     *
     * @param id busca especificamente en el menu
     * @return
     */
    public List<Menu> findAll(Long id) {
        String jpql = "select m from Menu m where m.id = :id";
        TypedQuery<Menu> query = em.createQuery(jpql, Menu.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

}
