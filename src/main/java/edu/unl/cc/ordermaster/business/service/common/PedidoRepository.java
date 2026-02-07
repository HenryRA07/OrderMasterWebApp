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

    /**
     * Busca pedidos por estado específico
     * @param estado Estado del pedido
     * @return Lista de pedidos en ese estado
     */
    public List<Pedido> findPedidosByEstado(EstadoPedido estado) {
        try {
            String jpql = "SELECT p FROM Pedido p " +
                          "LEFT JOIN FETCH p.itemPedido " +
                          "WHERE p.estado = :estado " +
                          "ORDER BY p.fechaPedidoCreacion DESC, p.id DESC";
            
            return em.createQuery(jpql, Pedido.class)
                    .setParameter("estado", estado)
                    .getResultList();
        } catch (Exception e) {
            return List.of(); // Retornar lista vacía en caso de error
        }
    }

    /**
     * Busca pedidos por fecha específica
     * @param fecha Fecha a consultar
     * @return Lista de pedidos del día
     */
    public List<Pedido> findPedidosByFecha(LocalDate fecha) {
        try {
            String jpql = "SELECT p FROM Pedido p " +
                          "LEFT JOIN FETCH p.itemPedido " +
                          "WHERE p.fechaPedidoCreacion = :fecha " +
                          "ORDER BY p.fechaPedidoCreacion DESC, p.id DESC";
            
            return em.createQuery(jpql, Pedido.class)
                    .setParameter("fecha", fecha)
                    .getResultList();
        } catch (Exception e) {
            return List.of(); // Retornar lista vacía en caso de error
        }
    }

    /**
     * Busca todos los pedidos (para administración)
     * @return Lista de todos los pedidos
     */
    public List<Pedido> findAllPedidos() {
        try {
            String jpql = "SELECT p FROM Pedido p " +
                          "LEFT JOIN FETCH p.itemPedido " +
                          "ORDER BY p.fechaPedidoCreacion DESC, p.id DESC";
            
            return em.createQuery(jpql, Pedido.class)
                    .getResultList();
        } catch (Exception e) {
            return List.of(); // Retornar lista vacía en caso de error
        }
    }

    /**
     * Busca pedido por ID con items cargados
     * @param id ID del pedido
     * @return Pedido encontrado o null
     */
    public Pedido findPedidoWithItems(Long id) {
        try {
            String jpql = "SELECT p FROM Pedido p " +
                          "LEFT JOIN FETCH p.itemPedido " +
                          "LEFT JOIN FETCH p.cliente " +
                          "WHERE p.id = :id";
            
            return em.createQuery(jpql, Pedido.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
