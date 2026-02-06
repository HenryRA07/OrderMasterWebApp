package edu.unl.cc.ordermaster.business.service.common;

import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.EstadoPedido;
import edu.unl.cc.ordermaster.domain.Pedido;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class PedidoRepository {

    @Inject
    CrudGenericService crudgeneric;

    @PersistenceContext
    private EntityManager em;

    /**
     * Busca pedidos por rango de fechas y estado
     *
     * @param fechaDesde Fecha inicial (inclusive)
     * @param fechaHasta Fecha final (inclusive)
     * @param estado     Estado del pedido (enum)
     * @return Lista de pedidos que cumplen los criterios
     */
    public List<Pedido> findPedidosForDateAndEstado(LocalDate fechaDesde, LocalDate fechaHasta, EstadoPedido estado) {
        String jpql = "SELECT p FROM Pedido p LEFT JOIN FETCH p.itemPedido i " +
                "WHERE p.fechaPedidoCreacion BETWEEN :fechaDesde  AND :fechaHasta " +
                "AND p.estado = :estado " +
                "ORDER BY p.fechaPedidoCreacion DESC, p.id DESC";

        return em.createQuery(jpql, Pedido.class)
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .setParameter("estado", estado)
                .getResultList();
    }
}
