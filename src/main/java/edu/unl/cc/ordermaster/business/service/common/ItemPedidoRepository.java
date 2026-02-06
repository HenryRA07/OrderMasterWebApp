package edu.unl.cc.ordermaster.business.service.common;

import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.ItemPedido;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class ItemPedidoRepository {
    @Inject
    private CrudGenericService crudgeneric;
    @PersistenceContext
    private EntityManager em;

    public List<ItemPedido> findAll(Long id) {
        String jpql = "SELECT i FROM ItemPedido i " +
                "JOIN FETCH i.item " +
                "WHERE i.id = :idPedido";
        TypedQuery<ItemPedido> query = em.createQuery(jpql, ItemPedido.class);
        query.setParameter("idPedido", id);
        return query.getResultList();
    }
}
