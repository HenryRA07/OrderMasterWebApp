package edu.unl.cc.ordermaster.business.service.common;

import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.ItemMenu;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


import java.util.List;

@Stateless
public class ItemMenuRepository {
    @Inject
    CrudGenericService crudgeneric;
    @PersistenceContext
    private EntityManager em;

    public List<ItemMenu> findAllItemMenu(String estado, Long id) {
        String jpql = "SELECT i FROM ItemMenu i " +
                "WHERE (LOWER(i.disponibilidad) LIKE LOWER(:texto) OR :texto IS NULL) " +
                "AND (i.id = :catId OR :catId IS NULL)";

        return em.createQuery(jpql, ItemMenu.class)
                .setParameter("texto", estado != null ? "%" + estado + "%" : null)
                .setParameter("catId", id)
                .getResultList();
    }

    /*public ItemMenu save(ItemMenu itemMenu){
       // if (ItemMenu.getId() == null){
            return crudService.create(ItemMenu);
        } else {
            return crudService.update(ItemMenu);
        }*/
    }


