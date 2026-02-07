package edu.unl.cc.ordermaster.business.service.common;

import edu.unl.cc.ordermaster.business.service.CrudGenericService;
import edu.unl.cc.ordermaster.domain.ItemMenu;
import edu.unl.cc.ordermaster.domain.Menu;
import edu.unl.cc.ordermaster.domain.TipoMenu;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

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

    /**
     * Obtiene items del menú del día filtrados por tipo
     * @param fecha Fecha del menú
     * @param tipo Tipo de menú (DESAYUNO, ALMUERZO, MERIENDA)
     * @return Lista de items disponibles
     */
    public List<ItemMenu> findItemsByDateAndTipo(LocalDate fecha, TipoMenu tipo) {
        try {
            String jpql = "SELECT im FROM ItemMenu im " +
                          "JOIN im.menu m " +
                          "WHERE m.fechaCreacion = :fecha " +
                          "AND m.tipoMenu = :tipo " +
                          "AND im.disponibilidad = true " +
                          "ORDER BY im.producto.nombre";
            TypedQuery<ItemMenu> query = em.createQuery(jpql, ItemMenu.class);
            query.setParameter("fecha", fecha);
            query.setParameter("tipo", tipo);
            return query.getResultList();
        } catch (Exception e) {
            // En caso de error, retornar lista vacía para evitar NullPointerException
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene todos los items disponibles del menú del día
     * @param fecha Fecha del menú
     * @return Lista completa de items disponibles
     */
    public List<ItemMenu> findAllAvailableItems(LocalDate fecha) {
        try {
            String jpql = "SELECT DISTINCT im FROM ItemMenu im " +
                          "JOIN FETCH im.menu m " +
                          "JOIN FETCH im.producto " +
                          "WHERE m.fechaCreacion = :fecha " +
                          "AND im.disponibilidad = true " +
                          "ORDER BY m.tipoMenu, im.producto.nombre";
            TypedQuery<ItemMenu> query = em.createQuery(jpql, ItemMenu.class);
            query.setParameter("fecha", fecha);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Busca menú por fecha y tipo
     * @param fecha Fecha del menú
     * @param tipo Tipo de menú
     * @return Menú encontrado o null
     */
    public Menu findMenuByDateAndTipo(LocalDate fecha, TipoMenu tipo) {
        try {
            String jpql = "SELECT m FROM Menu m " +
                          "WHERE m.fechaCreacion = :fecha " +
                          "AND m.tipoMenu = :tipo";
            TypedQuery<Menu> query = em.createQuery(jpql, Menu.class);
            query.setParameter("fecha", fecha);
            query.setParameter("tipo", tipo);
            List<Menu> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene todos los menús de una fecha específica
     * @param fecha Fecha a consultar
     * @return Lista de menús del día
     */
    public List<Menu> findMenusByDate(LocalDate fecha) {
        try {
            String jpql = "SELECT m FROM Menu m WHERE m.fechaCreacion = :fecha ORDER BY m.tipoMenu";
            TypedQuery<Menu> query = em.createQuery(jpql, Menu.class);
            query.setParameter("fecha", fecha);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
