package bdavanzadas.lab1.services;

import bdavanzadas.lab1.dtos.OrderNameAddressDTO;
import bdavanzadas.lab1.dtos.TopSpenderDTO;

import bdavanzadas.lab1.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.repositories.OrdersRepository;
import bdavanzadas.lab1.dtos.OrderTotalProductsDTO;



import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 * La clase OrdersService representa el servicio de órdenes en la aplicación.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar órdenes en la base de datos.
 *
 */
@Service
public class OrdersService {

    /**
     * Repositorio de órdenes.
     * Este repositorio se utiliza para interactuar con la base de datos de órdenes.
     */
    @Autowired
    private OrdersRepository ordersRepository;


    /**
     * jdbc se usa para ejecutar consultas SQL directamente en la base de datos.
     * Este objeto se utiliza para ejecutar consultas SQL y obtener resultados.
     *
     * */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Servicio de usuarios.
     * Este servicio se utiliza para interactuar con la base de datos de usuarios.
     */
    @Autowired
    private UserService userService;




    /**
     * Metodo para obtener todos los pedidos de la base de datos.
     * @return Una lista de pedidos.
     */
    @Transactional(readOnly = true)
    public List<OrdersEntity> getAllOrders() {
        return ordersRepository.findAll();
    }


    /**
     * Metodo para guardar un pedido en la base de datos.
     * @param "order" El pedido a guardar.
     * @return void
     */
    @Transactional
    public void addOrder(OrdersEntity order) {
        ordersRepository.save(order);
    }


    /**
     * Metodo para actualizar un pedido en la base de datos.
     * @param "order" El pedido a actualizar.
     * @return void
     */
    @Transactional
    public void updateOrder(OrdersEntity order) {
        ordersRepository.update(order);
    }


    /**
     * Metodo para eliminar un pedido de la base de datos en base a su id.
     * @param "id" El id del pedido a eliminar.
     * @return void
     */
    @Transactional
    public void deleteOrder(int id) {
        ordersRepository.delete(id);
    }


    /**
     * Metodo para buscar un pedido por su id.
     * @param "id" El id del pedido a buscar.
     * @return El pedido encontrado.
     */
    @Transactional(readOnly = true)
    public OrdersEntity getOrderById(int id) {
        return ordersRepository.findById(id);
    }


    /**
     * Metodo para buscar un pedido por el id del cliente autenticado.
     *
     * Este método obtiene el ID del usuario autenticado y luego busca el ID del cliente asociado a ese usuario.
     * Luego, utiliza el ID del cliente para buscar todos los pedidos asociados a ese cliente.
     *
     * @return Una lista de pedidos asociados al cliente autenticado.
     *
     */

    @Transactional(readOnly = true)
    public List<OrdersEntity> getOrdersByClientId() {
        // Obtener el userId del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();

        // Obtener el clientId asociado al userId
        String sql = "SELECT id FROM clients WHERE user_id = ?";
        Integer clientId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (clientId == null) {
            throw new IllegalArgumentException("No se encontró un cliente asociado al usuario con ID " + userId);
        }

        // Obtener los pedidos del cliente
        return ordersRepository.findByClientId(clientId);
    }




    /**
     * Metodo para buscar un pedido por el id de un cliente.
     * @param "clientId" El id del cliente a buscar.
     * @return Una lista de pedidos asociados al cliente.
     */

    @Transactional(readOnly = true)
    public List<OrdersEntity> getOrdersByClientId(int clientId) {
        return ordersRepository.findByClientId(clientId);
    }


    /**
     * Metodo para buscar un pedido por el id de un dealer.
     * @param "dealerId" El id del dealer a buscar.
     * @return Una lista de pedidos asociados al dealer.
     */

    @Transactional(readOnly = true)
    public List<OrdersEntity> getOrdersByDealerId(int dealerId) {
        return ordersRepository.findByDealerId(dealerId);
    }


    /**
     * Metodo para buscar el pedido con mayor gasto por cliente en pedidos entregados.
     * @return El pedido con mayor gasto por cliente.
     */

    @Transactional(readOnly = true)
    public TopSpenderDTO getTopSpender() {
        return ordersRepository.getTopSpender();
    }


    /**
     * Método para crear una orden con sus productos asociados
     * @param "order" La entidad de la orden a crear
     * @param "productIds" Lista de IDs de productos a asociar a la orden
     *
     * Este método utiliza el procedimiento almacenado 'register_order_with_products' para:
     * 1. Registrar la orden en la base de datos
     * 2. Asociar todos los productos de la lista a la orden creada
     *
     * El procedimiento recibe como parámetros:
     * - Fecha de la orden
     * - Estado de la orden
     * - ID del cliente
     * - Array de IDs de productos
     * - ID del dealer (opcional)
     */
    @Transactional
    public void createOrderWithProducts(OrdersEntity order, List<Integer> productIds,
                                        List<Map<String, Double>> routePoints) {
        // 1. Obtener clientId del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();
        String sql = "SELECT id FROM clients WHERE user_id = ?";
        Integer clientId = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        if (clientId == null) {
            throw new IllegalArgumentException("No se encontró cliente asociado al usuario ID: " + userId);
        }
        order.setClientId(clientId);

        // 2. Registrar el pedido con los productos (procedimiento almacenado)
        sql = "CALL register_order_with_products(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                order.getOrderDate(),
                order.getStatus(),
                order.getClientId(),
                productIds.toArray(new Integer[0]),
                order.getDealerId()
        );

        // 3. Obtener el ID del pedido recién creado
        Integer orderId = getLastInsertedOrderId();

        // 4. Si se proporcionaron puntos para la ruta, calcular y guardar
        if (routePoints != null && !routePoints.isEmpty()) {
            updateOrderRouteWithPoints(orderId, routePoints);
        }

        // Opcional: Si el pedido viene con WKT directamente
        if (order.getEstimatedRoute() != null && !order.getEstimatedRoute().isEmpty()) {
            updateOrderRouteWithWKT(orderId, order.getEstimatedRoute());
        }
    }


    /**
     * Método para obtener el ID del último pedido insertado
     * @return El ID del último pedido insertado
     *
     * Este método utiliza una consulta SQL para obtener el ID del último pedido insertado en la tabla de pedidos.
     */
    @Transactional(readOnly = true)
    public Integer getLastInsertedOrderId() {
        String sql = "SELECT id FROM orders ORDER BY id DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * Método para marcar un pedido como entregado
     * Este metodo utiliza un procedimiento almacenado para cambiar el estado de un pedido a "ENTREGADO"
     * y registrar la fecha de entrega.
     *
     * @param "orderId" El ID del pedido a marcar como entregado
     * @param "deliveryDate" La fecha de entrega del pedido
     * @return void
     */
    @Transactional
    public void markAsDelivered(int orderId, Date deliveryDate) {
        String sql = "CALL change_order_status(?, ?, ?)";
        jdbcTemplate.update(sql, orderId, "ENTREGADO", deliveryDate);
    }



    /**
     * Método para marcar un pedido como fallido
     * Este metodo utiliza un procedimiento almacenado para cambiar el estado de un pedido a "FALLIDA"
     * y no requiere una fecha de entrega.
     *
     * @param "orderId" El ID del pedido a marcar como fallido
     * @return void
     */
    @Transactional
    public void markAsFailed(int orderId) {
        String sql = "CALL change_order_status(?, ?, ?)";
        jdbcTemplate.update(sql, orderId, "FALLIDA", null); // no se requiere fecha
    }


    /**
     * Método para obtener los pedidos fallidos por ID de la empresa
     * @param "companyId" El ID de la empresa a buscar
     * @return Una lista de pedidos fallidos asociados a la empresa
     */
    @Transactional(readOnly = true)
    public List<OrdersEntity> findFailedOrdersByCompanyId(int companyId) {
        return ordersRepository.findFailedOrdersByCompanyId(companyId);
    }

    /**
     * Método para obtener los pedidos entregados por ID de la empresa
     * @param "companyId" El ID de la empresa a buscar
     * @return Una lista de pedidos entregados asociados a la empresa
     */
    @Transactional(readOnly = true)
    public List<OrdersEntity> findDeliveredOrdersByCompanyId(int companyId) {
        return ordersRepository.findDeliveredOrdersByCompanyId(companyId);
    }


    /**
     * Método para obtener los pedidos por ID de la empresa
     * @param "companyId" El ID de la empresa a buscar
     * @return Una lista de pedidos asociados a la empresa
     */
    @Transactional(readOnly = true)
    public List<OrdersEntity> findOrdersByCompanyId(int companyId) {
        return ordersRepository.findOrdersByCompanyId(companyId);
    }

    /**
     * Método para actualizar el estado de un pedido por ID de dealer
     * @param "orderId" El ID del pedido a actualizar
     * @param "dealerId" El ID del dealer asociado al pedido
     * @param "newStatus" El nuevo estado del pedido
     * @return void
     */
    @Transactional
    public void updateOrderStatusByDealerId(int orderId, int dealerId, String newStatus) {
        OrdersEntity order = ordersRepository.findById(orderId);
        if (order == null || !dealerIdEquals(order.getDealerId(), dealerId)) {
            throw new IllegalArgumentException("No autorizado o no existe la orden.");
        }
        if ("ENTREGADO".equalsIgnoreCase(newStatus)) {
            // Actualiza status y fecha de entrega directamente vía repositorio (sin procedimiento)
            ordersRepository.updateOrderStatusAndDeliveryDate(orderId, dealerId, newStatus, new Date());
        } else if ("FALLIDA".equalsIgnoreCase(newStatus)) {
            // Borra la fecha de entrega
            ordersRepository.updateOrderStatusAndDeliveryDate(orderId, dealerId, newStatus, null);
        } else {
            ordersRepository.updateOrderStatusByDealerId(orderId, dealerId, newStatus);
        }
    }


    /**
     * Método para obtener los productos asociados a un pedido por ID de pedido
     * @param "orderId" El ID del pedido a buscar
     * @return Una lista de productos asociados al pedido
     */
    @Transactional(readOnly = true)
    public List<ProductEntity> findProductsByOrderId(int orderId) {
        return ordersRepository.findProductsByOrderId(orderId);
    }


    /**
     * Método para obtener la dirección del cliente autenticado
     * @return La dirección del cliente autenticado
     */
    @Transactional(readOnly = true)
    public String getAddressOfLoggedClient() {
        Long userId = userService.getAuthenticatedUserId();
        String sql = "SELECT address FROM clients WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, userId);
    }


    /**
     * Método para marcar un pedido como urgente
     * @param "orderId" El ID del pedido a marcar como urgente
     * @return void
     *
     * Este método utiliza una consulta SQL para actualizar el estado de un pedido a "URGENTE".
     */
    @Transactional
    public void markAsUrgent(int orderId) {
        String sql = "UPDATE orders SET status = 'URGENTE' WHERE id = ?";
        jdbcTemplate.update(sql, orderId);
    }


    /**
     * Método para obtener los pedidos por ID de dealer
     * @return Una lista de pedidos asociados al dealer autenticado
     *
     * Este método obtiene el ID del usuario autenticado y luego busca el ID del dealer asociado a ese usuario.
     * Luego, utiliza el ID del dealer para buscar todos los pedidos asociados a ese dealer.
     */
    @Transactional(readOnly = true)
    public List<OrdersEntity> getOrdersByDealerId() {
        // Obtener el userId del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();

        // Obtener el dealerId asociado al userId
        String sql = "SELECT id FROM dealers WHERE user_id = ?";
        Integer dealerId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (dealerId == null) {
            throw new IllegalArgumentException("No se encontró un dealer asociado al usuario con ID " + userId);
        }

        // Obtener los pedidos del dealer
        return ordersRepository.findByDealerId(dealerId);
    }


    /**
     * Método para obtener la orden activa del dealer autenticado
     * @return La orden activa asociada al dealer autenticado
     *
     * Este método obtiene el ID del usuario autenticado y luego busca el ID del dealer asociado a ese usuario.
     * Luego, utiliza el ID del dealer para buscar la orden activa asociada a ese dealer.
     */
    @Transactional(readOnly = true)
    public OrdersEntity getActiveOrderByDealer() {
        // Obtener el userId del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();

        // Obtener el dealerId asociado al userId
        String sql = "SELECT id FROM dealers WHERE user_id = ?";
        Integer dealerId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (dealerId == null) {
            throw new IllegalArgumentException("No se encontró un dealer asociado al usuario con ID " + userId);
        }

        // Obtener la orden activa del dealer
        return ordersRepository.findActiveOrderByDealerId(dealerId);
    }


    /**
     * Método para asignar una orden a un dealer
     * @param "orderId" El ID de la orden a asignar
     * @return void
     *
     * Este método obtiene el ID del usuario autenticado y luego busca el ID del dealer asociado a ese usuario.
     * Luego, verifica si el dealer ya tiene una orden activa y, si no es así, asigna la orden al dealer.
     */
    @Transactional
    public void assignOrderToDealer(int orderId) {
        // Obtener dealerId del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();
        Integer dealerId = jdbcTemplate.queryForObject(
                "SELECT id FROM dealers WHERE user_id = ?",
                Integer.class, userId
        );

        if (dealerId == null) {
            throw new IllegalArgumentException("No se encontró un dealer asociado al usuario");
        }

        // Verificar que el dealer no tenga otra orden activa
        OrdersEntity activeOrder = ordersRepository.findActiveOrderByDealerId(dealerId);
        if (activeOrder != null) {
            throw new IllegalStateException("El dealer ya tiene una orden activa");
        }

        // Asignar orden al dealer
        ordersRepository.assignOrderToDealer(orderId, dealerId);
    }


    /**
     * Método para obtener la cantidad de productos por pedido y dealer
     * @return Una lista de objetos OrderTotalProductsDTO con la cantidad de productos por pedido y dealer
     *
     * Este método obtiene el ID del usuario autenticado y luego busca el ID del dealer asociado a ese usuario.
     * Luego, utiliza el ID del dealer para obtener la cantidad de productos por pedido y dealer.
     */
    @Transactional(readOnly = true)
    public List<OrderTotalProductsDTO> getOrdersWithProductCountByDealerId() {
        Long userId = userService.getAuthenticatedUserId();

        String sql = "SELECT id FROM dealers WHERE user_id = ?";
        Integer dealerId = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        if (dealerId == null) {
            throw new IllegalArgumentException("No se encontró un dealer asociado al usuario con ID " + userId);
        }

        return ordersRepository.findOrdersWithProductCountByDealerId(dealerId);
    }


    /**
     * Método para obtener la dirección y nombre de la orden activa del dealer autenticado
     * @return Un objeto OrderNameAddressDTO con la dirección y nombre de la orden activa
     *
     * Este método obtiene el ID del usuario autenticado y luego busca el ID del dealer asociado a ese usuario.
     * Luego, utiliza el ID del dealer para obtener la dirección y nombre de la orden activa.
     */
    @Transactional(readOnly = true)
    public OrderNameAddressDTO getActiveOrderNameAddresDTOByDealerId() {
        // Obtener el ID del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();

        // Obtener el dealerId asociado al usuario autenticado
        String sql = "SELECT id FROM dealers WHERE user_id = ?";
        Integer dealerId = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        if (dealerId == null) {
            throw new IllegalArgumentException("No se encontró un dealer asociado al usuario con ID " + userId);
        }

        // Llamar al repositorio para obtener la orden activa
        return ordersRepository.findActiveOrderNameAddresDTOByDealerId(dealerId);
    }

    @Transactional
    public void updateOrderRouteWithWKT(int orderId, String lineStringWKT) {
        // Validación básica del WKT
        if (!lineStringWKT.toUpperCase().startsWith("LINESTRING(")) {
            throw new IllegalArgumentException("Formato WKT inválido. Debe comenzar con 'LINESTRING('");
        }
        ordersRepository.updateEstimatedRoute(orderId, lineStringWKT);
    }

    @Transactional
    public void updateOrderRouteWithPoints(int orderId, List<Map<String, Double>> points) {
        // Validación básica
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("Se necesitan al menos 2 puntos para crear una ruta");
        }

        for (Map<String, Double> point : points) {
            if (!point.containsKey("longitude") || !point.containsKey("latitude")) {
                throw new IllegalArgumentException("Cada punto debe tener 'longitude' y 'latitude'");
            }
        }

        ordersRepository.updateEstimatedRouteFromPoints(orderId, points);
    }

    /**
     * Obtiene los pedidos cuya ruta estimada cruza más de 2 zonas de reparto
     * @return Lista de OrderNameAddressDTO con los pedidos que cumplen el criterio
     */
    public List<OrderNameAddressDTO> getOrdersCrossingMultipleCoverageAreas() {
        return ordersRepository.findOrdersCrossingMoreThanTwoCoverageAreas();
    }

    /**
     * Compara si el dealerId de la entidad OrdersEntity es igual al dealerId proporcionado.
     * @param a El dealerId de la entidad OrdersEntity.
     * @param b El dealerId proporcionado para comparar.
     * @return true si son iguales, false en caso contrario.
     */
    private boolean dealerIdEquals(Integer a, int b) {
        return a != null && a == b;
    }

}
