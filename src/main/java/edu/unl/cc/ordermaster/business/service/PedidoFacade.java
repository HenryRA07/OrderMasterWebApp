package edu.unl.cc.ordermaster.business.service;

import edu.unl.cc.ordermaster.business.service.common.PedidoRepository;
import edu.unl.cc.ordermaster.domain.ItemPedido;
import edu.unl.cc.ordermaster.domain.Pedido;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Facade de negocio para gestión de pedidos
 * Centraliza la lógica de negocio relacionada con la toma y gestión de pedidos
 */
@ApplicationScoped
public class PedidoFacade {
    
    private static final Logger LOGGER = Logger.getLogger(PedidoFacade.class.getName());
    
    @Inject
    private CrudGenericService crudService;
    
    @Inject
    private PedidoRepository pedidoRepository;
    
    /**
     * Crea un nuevo pedido en la base de datos
     * @param pedido Pedido a crear
     * @return Pedido creado con ID asignado
     */
    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        try {
            // Validar que el pedido tenga los datos mínimos
            if (pedido.getCliente() == null) {
                throw new IllegalArgumentException("El pedido debe tener un cliente");
            }
            if (pedido.getMesa() == null || pedido.getMesa() <= 0) {
                throw new IllegalArgumentException("El pedido debe tener una mesa válida");
            }
            
            // Establecer valores por defecto
            if (pedido.getPrecioTotal() == null) {
                pedido.setPrecioTotal(BigDecimal.ZERO);
            }
            if (pedido.getEstado() == null) {
                pedido.setEstado(edu.unl.cc.ordermaster.domain.EstadoPedido.PENDIENTE);
            }
            
            Pedido pedidoCreado = crudService.create(pedido);
            
            // Si tiene items, guardarlos también
            if (pedido.getItemPedido() != null && !pedido.getItemPedido().isEmpty()) {
                for (ItemPedido item : pedido.getItemPedido()) {
                    item.setPedido(pedidoCreado);
                    crudService.create(item);
                }
                // Recalcular total después de guardar todos los items
                pedidoCreado.calcularTotal();
                crudService.update(pedidoCreado);
            }
            
            LOGGER.info("Pedido creado exitosamente: ID=" + pedidoCreado.getId() + 
                       ", Mesa=" + pedidoCreado.getMesa());
            return pedidoCreado;
        } catch (Exception e) {
            LOGGER.severe("Error al crear pedido: " + e.getMessage());
            throw new RuntimeException("No se pudo crear el pedido: " + e.getMessage());
        }
    }
    @Transactional
    public Pedido confirmarPedido(
            Pedido pedido,
            String nombre,
            String apellido,
            String dni,
            String telefono,
            String email,
            Integer mesa
    ) {

        if (pedido == null || pedido.getItemPedido() == null || pedido.getItemPedido().isEmpty()) {
            throw new RuntimeException("El pedido está vacío");
        }

        if (mesa == null || mesa <= 0) {
            throw new RuntimeException("El número de mesa es inválido");
        }

        if (email != null && !email.isBlank() && !email.contains("@")) {
            throw new RuntimeException("El formato del email es incorrecto");
        }

        // Crear cliente
        var cliente = new edu.unl.cc.ordermaster.domain.Cliente();
        cliente.setNombre(nombre.trim());
        cliente.setApellido(apellido.trim());
        cliente.setEmail(
                email.trim() + "+pedido" + System.currentTimeMillis() + "@ordermaster.local"
        );

        if (dni != null && !dni.isBlank()) {
            cliente.setDni(dni.trim());
        }

        if (telefono != null && !telefono.isBlank()) {
            cliente.setTelefono(telefono.trim());
        }

        pedido.setCliente(cliente);
        pedido.setMesa(mesa);

        LOGGER.info("Confirmando pedido con cliente y mesa");

        return crearPedido(pedido);
    }
    /**
     * Actualiza un pedido existente
     * @param pedido Pedido a actualizar
     * @return Pedido actualizado
     */
    @Transactional
    public Pedido actualizarPedido(Pedido pedido) {
        try {
            pedido.calcularTotal();
            
            Pedido pedidoActualizado = crudService.update(pedido);
            
            LOGGER.info("Pedido actualizado exitosamente: ID=" + pedidoActualizado.getId());
            return pedidoActualizado;
        } catch (Exception e) {
            LOGGER.severe("Error al actualizar pedido: " + e.getMessage());
            throw new RuntimeException("No se pudo actualizar el pedido: " + e.getMessage());
        }
    }
    
    /**
     * Agrega un item a un pedido existente
     * @param itemPedido Item a agregar
     * @param pedido Pedido al que se agregará el item
     * @return Item creado con ID
     */
    @Transactional
    public ItemPedido agregarItemPedido(ItemPedido itemPedido, Pedido pedido) {
        try {
            itemPedido.setPedido(pedido);
            ItemPedido itemCreado = crudService.create(itemPedido);
            pedido.agregarItem(itemCreado);
            actualizarPedido(pedido);
            
            LOGGER.info("Item agregado exitosamente al pedido: ID Pedido=" + pedido.getId() + 
                       ", Producto=" + itemPedido.getItem().getProducto().getNombre());
            return itemCreado;
        } catch (Exception e) {
            LOGGER.severe("Error al agregar item al pedido: " + e.getMessage());
            throw new RuntimeException("No se pudo agregar el item al pedido: " + e.getMessage());
        }
    }

    @Transactional
    public void eliminarItemPedido(Pedido pedido, ItemPedido item) {
        try {
            if (pedido == null || item == null) {
                throw new RuntimeException("Pedido o item nulo");
            }
            pedido.eliminarItem(item);
            pedido.calcularTotal();
            crudService.update(pedido);
            LOGGER.info("Item eliminado del pedido ID=" + pedido.getId());

        } catch (Exception e) {
            LOGGER.severe("Error al eliminar item del pedido: " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar el item del pedido");
        }
    }

    /**
     * Busca un pedido por su ID
     * @param id ID del pedido
     * @return Pedido encontrado o null
     */
    public Pedido buscarPedido(Long id) {
        try {
            return crudService.find(Pedido.class, id);
        } catch (Exception e) {
            LOGGER.severe("Error al buscar pedido: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene todos los pedidos en estado PENDIENTE
     * @return Lista de pedidos pendientes
     */
    public List<Pedido> obtenerPedidosPendientes() {
        try {
            return pedidoRepository.findPedidosByEstado(edu.unl.cc.ordermaster.domain.EstadoPedido.PENDIENTE);
        } catch (Exception e) {
            LOGGER.severe("Error al obtener pedidos pendientes: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * Obtiene todos los pedidos en estado LISTO
     * @return Lista de pedidos listos
     */
    public List<Pedido> obtenerPedidosListos() {
        try {
            return pedidoRepository.findPedidosByEstado(edu.unl.cc.ordermaster.domain.EstadoPedido.LISTO);
        } catch (Exception e) {
            LOGGER.severe("Error al obtener pedidos listos: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * Cambia el estado de un pedido
     * @param pedido Pedido a modificar
     * @param nuevoEstado Nuevo estado del pedido
     * @return Pedido actualizado
     */
    @Transactional
    public Pedido cambiarEstadoPedido(Pedido pedido, edu.unl.cc.ordermaster.domain.EstadoPedido nuevoEstado) {
        try {
            pedido.setEstado(nuevoEstado);
            Pedido pedidoActualizado = crudService.update(pedido);
            
            LOGGER.info("Estado del pedido actualizado: ID=" + pedido.getId() + 
                       ", Nuevo estado=" + nuevoEstado);
            return pedidoActualizado;
        } catch (Exception e) {
            LOGGER.severe("Error al cambiar estado del pedido: " + e.getMessage());
            throw new RuntimeException("No se pudo cambiar el estado del pedido: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un pedido (soft delete: cambia estado a CANCELADO si existiera)
     * @param pedido Pedido a eliminar
     */
    @Transactional
    public void eliminarPedido(Pedido pedido) {
        try {
            crudService.delete(Pedido.class, pedido.getId());
            LOGGER.info("Pedido eliminado: ID=" + pedido.getId());
        } catch (Exception e) {
            LOGGER.severe("Error al eliminar pedido: " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar el pedido: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todos los pedidos de una fecha específica
     * @param fecha Fecha a consultar
     * @return Lista de pedidos del día
     */
    public List<Pedido> obtenerPedidosPorFecha(java.time.LocalDate fecha) {
        try {
            return pedidoRepository.findPedidosByFecha(fecha);
        } catch (Exception e) {
            LOGGER.severe("Error al obtener pedidos por fecha: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}