package edu.unl.cc.ordermaster.business.service;

import edu.unl.cc.ordermaster.business.service.common.MenuRepository;
import edu.unl.cc.ordermaster.domain.ItemMenu;
import edu.unl.cc.ordermaster.domain.Menu;
import edu.unl.cc.ordermaster.domain.Producto;
import edu.unl.cc.ordermaster.domain.TipoMenu;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Facade de negocio para gestión de menús
 * Centraliza la lógica de negocio relacionada con menús diarios
 */
@ApplicationScoped
public class MenuFacade {
    
    private static final Logger LOGGER = Logger.getLogger(MenuFacade.class.getName());
    
    @Inject
    private MenuRepository menuRepository;
    
    @Inject
    private CrudGenericService crudService;
    
    /**
     * Obtiene los items del menú del día filtrados por tipo específico
     * @param tipo Tipo de menú (DESAYUNO, ALMUERZO, MERIENDA)
     * @return Lista de items disponibles para el tipo solicitado
     */
    public List<ItemMenu> getMenuDelDiaPorTipo(TipoMenu tipo) {
        try {
            LocalDate hoy = LocalDate.now();
            List<ItemMenu> items = menuRepository.findItemsByDateAndTipo(hoy, tipo);
            LOGGER.info("Se encontraron " + items.size() + " items para el tipo " + tipo);
            return items;
        } catch (Exception e) {
            LOGGER.severe("Error al obtener menú del día por tipo: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
     /**
      * @param menu Menú a actualizar
      * @return Menú actualizado
      */
     @Transactional
     public Menu actualizarMenu(Menu menu) {
        try {
            Menu menuActualizado = crudService.update(menu);
            LOGGER.info("Menú actualizado exitosamente: " + menuActualizado.getNombreMenu());
            return menuActualizado;
        } catch (Exception e) {
            LOGGER.severe("Error al actualizar menú: " + e.getMessage());
            throw new RuntimeException("No se pudo actualizar el menú: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene un menú específico por fecha y tipo
     * @param fecha Fecha del menú
     * @param tipo Tipo de menú
     * @return Menú encontrado o null
     */
    public Menu obtenerMenuPorFechaYTipo(LocalDate fecha, TipoMenu tipo) {
        try {
            return menuRepository.findMenuByDateAndTipo(fecha, tipo);
        } catch (Exception e) {
            LOGGER.severe("Error al buscar menú por fecha y tipo: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Verifica si existe un menú para la fecha y tipo especificados
     * @param fecha Fecha a verificar
     * @param tipo Tipo de menú a verificar
     * @return true si existe, false otherwise
     */
    public boolean existeMenuParaFechaYTipo(LocalDate fecha, TipoMenu tipo) {
        try {
            return obtenerMenuPorFechaYTipo(fecha, tipo) != null;
        } catch (Exception e) {
            LOGGER.severe("Error al verificar existencia de menú: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene todos los menús de una fecha específica
     * @param fecha Fecha a consultar
     * @return Lista de menús del día
     */
    public List<Menu> obtenerMenusPorFecha(LocalDate fecha) {
        try {
            return menuRepository.findMenusByDate(fecha);
        } catch (Exception e) {
            LOGGER.severe("Error al obtener menús por fecha: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene todos los items disponibles del menú del día (para la vista del mesero)
     * @return Lista de todos los items del día
     */
    public List<ItemMenu> obtenerTodosLosItemsDelDia() {
        try {
            LocalDate hoy = LocalDate.now();
            return menuRepository.findAllAvailableItems(hoy);
        } catch (Exception e) {
            LOGGER.severe("Error al obtener items del día: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene todos los productos existentes (para el catálogo al crear menú)
     * @return Lista de productos (Platillos y Bebidas)
     */
    @SuppressWarnings("unchecked")
    public List<Producto> obtenerTodosLosProductos() {
        try {
            return crudService.findWithQuery("SELECT p FROM Producto p ORDER BY p.nombre");
        } catch (Exception e) {
            LOGGER.severe("Error al obtener productos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Verifica si existe un producto con el nombre indicado (ignorando mayúsculas)
     */
    public boolean existeProductoPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return false;
        try {
            String nombreNorm = nombre.trim().toLowerCase();
            List<Producto> lista = crudService.findWithQuery(
                "SELECT p FROM Producto p WHERE LOWER(p.nombre) = :nombre",
                java.util.Map.of("nombre", nombreNorm)
            );
            return lista != null && !lista.isEmpty();
        } catch (Exception e) {
            LOGGER.severe("Error al verificar producto por nombre: " + e.getMessage());
            return false;
        }
    }

    /**
     * Persiste un producto en la base de datos. Se usa al crear un producto nuevo para que
     * aparezca inmediatamente en el catálogo.
     */
    @Transactional
    public Producto guardarProducto(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto no puede ser nulo");
        try {
            return crudService.create(producto);
        } catch (Exception e) {
            LOGGER.severe("Error al guardar producto: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar el producto: " + e.getMessage());
        }
    }

    /**
     * Agrega un item al menú y lo persiste en base de datos.
     * Si el menú es nuevo (sin id), lo crea primero.
     * @param itemMenu Item a agregar (debe contener producto, precio, disponibilidad)
     * @param menu Menú al que pertenece el item
     */
    @Transactional
    public void agregarItemMenu(ItemMenu itemMenu, Menu menu) {
        try {
            if (menu.getId() == null) {
                menu = crudService.create(menu);
            }
            if (itemMenu.getProducto() != null && itemMenu.getProducto().getId() == null) {
                crudService.create(itemMenu.getProducto());
            }
            itemMenu.setMenu(menu);
            crudService.create(itemMenu);
            LOGGER.info("Item agregado al menú exitosamente");
        } catch (Exception e) {
            LOGGER.severe("Error al agregar item al menú: " + e.getMessage());
            throw new RuntimeException("No se pudo agregar el item al menú: " + e.getMessage());
        }
    }

    /**
     * Crea o actualiza el menú completo en base de datos.
     * Si el menú es nuevo, persiste menú e items. Si ya existe, actualiza.
     * @param menu Menú a crear o actualizar
     */
    @Transactional
    public void crearMenu(Menu menu) {
        try {
            if (menu.getId() == null) {
                // Establecer la relación bidireccional antes de persistir (cascade lo hace automáticamente)
                if (menu.getItemMenu() != null) {
                    for (ItemMenu item : menu.getItemMenu()) {
                        item.setMenu(menu);
                    }
                }
                crudService.create(menu);
            } else {
                crudService.update(menu);
            }
            LOGGER.info("Menú guardado exitosamente: " + menu.getNombreMenu());
        } catch (Exception e) {
            LOGGER.severe("Error al crear/guardar menú: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar el menú: " + e.getMessage());
        }
    }
}