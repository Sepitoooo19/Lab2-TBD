package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.dtos.OrderNameAddressDTO;
import bdavanzadas.lab1.dtos.OrderTotalProductsDTO;
import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.entities.ProductEntity;
import bdavanzadas.lab1.repositories.OrdersRepository;
import bdavanzadas.lab1.services.DealerService;
import bdavanzadas.lab1.services.OrdersService;
import bdavanzadas.lab1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * La clase OrdersController maneja las solicitudes relacionadas con los pedidos.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar pedidos en la base de datos.
 */
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class OrdersController {

    /**
     * Servicio de pedidos.
     * Este servicio se utiliza para interactuar con la base de datos de pedidos.
     */
    private final OrdersService ordersService;

    /**
     * Repositorio de pedidos.
     * Este repositorio se utiliza para realizar operaciones CRUD en la base de datos de pedidos.
     */
    @Autowired
    private OrdersRepository ordersRepository;

    /**
     * jdbc sirve para ejecutar consultas SQL directas.
     * Este objeto se utiliza para ejecutar consultas SQL en la base de datos.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Servicio de dealers.
     * Este servicio se utiliza para interactuar con la base de datos de dealers.
     */
    @Autowired
    private UserService userService;


    /**
     * Constructor de la clase OrdersController.
     * @param ordersService El servicio de pedidos a utilizar.
     */
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    /**
     * Endpoint para obtener todos los pedidos.
     * Este endpoint devuelve una lista de todos los pedidos en la base de datos.
     */
    @GetMapping
    public ResponseEntity<List<OrdersEntity>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }


    /**
     * Endpoint para obtener un pedido por su ID.
     * Este endpoint devuelve un pedido específico basado en su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrdersEntity> getOrderById(@PathVariable int id) {
        OrdersEntity order = ordersService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para crear un nuevo pedido.
     * Este endpoint guarda un nuevo pedido en la base de datos.
     */
    @PostMapping
    public ResponseEntity<Void> addOrder(@RequestBody OrdersEntity order) {
        ordersService.addOrder(order);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * Endpoint para actualizar un pedido existente.
     * Este endpoint actualiza un pedido existente en la base de datos.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrder(@PathVariable int id, @RequestBody OrdersEntity order) {
        order.setId(id);
        ordersService.updateOrder(order);
        return ResponseEntity.noContent().build();
    }


    /**
     * Endpoint para eliminar un pedido por su ID.
     * Este endpoint elimina un pedido específico basado en su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        ordersService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Pedidos para el cliente autenticado (según contexto de seguridad).
     * Devuelve 403 si el usuario no es un cliente válido.
     */
    @GetMapping("/client/orders")
    public ResponseEntity<List<OrdersEntity>> getOrdersByClient() {
        try {
            List<OrdersEntity> orders = ordersService.getOrdersByClientId();
            return ResponseEntity.ok(orders); // Devuelve un array vacío si no hay pedidos
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Devuelve 403 si hay un error de validación
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Devuelve 500 para errores inesperados
        }
    }


    /**
     * Endpoint para obtener pedidos por ID de cliente.
     * Este endpoint devuelve una lista de pedidos específicos basados en el ID del cliente.
     */
    @GetMapping("/client/{id}")
    public ResponseEntity<List<OrdersEntity>> getOrdersByClientId(@PathVariable int id) {
        List<OrdersEntity> orders = ordersService.getOrdersByClientId(id);
        return ResponseEntity.ok(orders);
    }


    /**
     * Endpoint para obtener el cliente que más ha gastado.
     * Este endpoint devuelve el cliente que ha realizado el mayor gasto en pedidos.
     */
    @GetMapping("/top-spender")
    public ResponseEntity<TopSpenderDTO> getTopSpender() {
        TopSpenderDTO topSpender = ordersService.getTopSpender();
        return ResponseEntity.ok(topSpender);
    }


    /**
     * Endpoint para crear un pedido con productos.
     * Este endpoint guarda un nuevo pedido en la base de datos y asocia productos a él.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestBody OrdersEntity order,
            @RequestParam List<Integer> productIds) {

        try {
            // Asume que order.getEstimatedRoute() contiene el WKT
            ordersService.createOrderWithProducts(order, productIds, null);
            return ResponseEntity.ok("Orden creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /**
     * Endpoint para marcar un pedido como entregado.
     * Este endpoint actualiza el estado de un pedido a "ENTREGADO".
     */
    @PutMapping("/{id}/deliver")
    public ResponseEntity<String> marcarComoEntregado(@PathVariable int id) {
        ordersService.markAsDelivered(id, new Date());
        return ResponseEntity.ok("Pedido marcado como entregado");
    }



    /**
     * Endpoint para marcar un pedido como fallido.
     * Este endpoint actualiza el estado de un pedido a "FALLIDO".
     */
    @GetMapping("/failed/company/{companyId}")
    public ResponseEntity<List<OrdersEntity>> getFailedOrdersByCompanyId(@PathVariable int companyId) {
        List<OrdersEntity> orders = ordersService.findFailedOrdersByCompanyId(companyId);
        return ResponseEntity.ok(orders);
    }



    /**
     * Endpoint para obtener los pedidos entregados por ID de empresa.
     * Este endpoint devuelve una lista de pedidos entregados específicos basados en el ID de la empresa.
     */
    @GetMapping("/delivered/company/{companyId}")
    public ResponseEntity<List<OrdersEntity>> getDeliveredOrdersByCompanyId(@PathVariable int companyId) {
        List<OrdersEntity> orders = ordersService.findDeliveredOrdersByCompanyId(companyId);
        return ResponseEntity.ok(orders);
    }



    /**
     * Endpoint para obtener los pedidos por ID de empresa.
     * Este endpoint devuelve una lista de pedidos específicos basados en el ID de la empresa.
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<OrdersEntity>> getOrdersByCompanyId(@PathVariable int companyId) {
        List<OrdersEntity> orders = ordersService.findOrdersByCompanyId(companyId);
        if (orders != null && !orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    /**
     * Endpoint para obtener los pedidos por ID de dealer.
     * Este endpoint devuelve una lista de pedidos específicos basados en el ID del dealer.
     */
    @GetMapping("/dealer/{id}")
    public ResponseEntity<List<OrdersEntity>> getOrdersByDealerId(@PathVariable int id) {
        List<OrdersEntity> orders = ordersService.getOrdersByDealerId(id);
        if (orders != null && !orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Actualiza el estado del pedido (ENTREGADO / FALLIDA) asegurando
     * que el dealer autenticado sea el propietario de la orden.
     */
    @PutMapping("/{id}/dealer/{dealerId}/status")
    public ResponseEntity<Void> updateOrderStatusByDealerId(
            @PathVariable int id,
            @PathVariable int dealerId,
            @RequestBody Map<String, String> requestBody) {
        System.out.println("Actualizando estado de la orden...");
        System.out.println("ID de la orden: " + id);
        System.out.println("ID del dealer: " + dealerId);
        System.out.println("Nuevo estado: " + requestBody.get("status"));

        ordersService.updateOrderStatusByDealerId(id, dealerId, requestBody.get("status"));
        return ResponseEntity.noContent().build();
    }


    /**
     * Endpoint para obtener los productos por ID de pedido.
     * Este endpoint devuelve una lista de productos específicos basados en el ID del pedido.
     */
    @GetMapping("/{orderId}/products")
    public ResponseEntity<List<ProductEntity>> getProductsByOrderId(@PathVariable int orderId) {
        try {
            List<ProductEntity> products = ordersService.findProductsByOrderId(orderId);
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build(); // Devuelve 204 si no hay productos
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devuelve 500 para errores inesperados
        }
    }



    /**
     * Endpoint para obtener el último ID de pedido insertado.
     * Este endpoint devuelve el ID del último pedido insertado en la base de datos.
     */
    @GetMapping("/last-inserted")
    public ResponseEntity<Integer> getLastInsertedOrderId() {
        try {
            int lastInsertedOrderId = ordersService.getLastInsertedOrderId(); // Llama al servicio
            return ResponseEntity.ok(lastInsertedOrderId); // Devuelve el ID en la respuesta
        } catch (Exception e) {
            e.printStackTrace(); // Registra el error en los logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devuelve un error 500
        }
    }




    /**
     * Endpoint para obtener la dirección del cliente autenticado.
     * Este endpoint devuelve la dirección del cliente autenticado en la base de datos.
     */
    @GetMapping("/address")
    public ResponseEntity<String> getAddressOfLoggedClient() {
        try {
            String address = ordersService.getAddressOfLoggedClient();
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    /**
     * Endpoint para marcar un pedido como urgente.
     * Este endpoint actualiza el estado de un pedido a "URGENTE".
     */
    @PutMapping("/{id}/urgent")
    public ResponseEntity<String> markOrderAsUrgent(@PathVariable int id) {
        try {
            ordersService.markAsUrgent(id);
            return ResponseEntity.ok("Pedido marcado como URGENTE");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al marcar el pedido como URGENTE");
        }
    }


    /**
     * Endpoint para obtener los pedidos por ID de dealer.
     * Este endpoint devuelve una lista de pedidos específicos basados en el ID del dealer.
     */
    @GetMapping("/dealer/orders")
    public ResponseEntity<List<OrdersEntity>> getOrdersByDealer() {
        try {
            List<OrdersEntity> orders = ordersService.getOrdersByDealerId();
            return ResponseEntity.ok(orders); // Devuelve un array vacío si no hay pedidos
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Devuelve 403 si hay un error de validación
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Devuelve 500 para errores inesperados
        }
    }

    /**
     * Endpoint para obtener la orden activa del dealer autenticado.
     * Este endpoint devuelve la orden activa del dealer autenticado en la base de datos.
     */
    @GetMapping("/dealer/active-order")
    public ResponseEntity<OrdersEntity> getActiveOrderByDealer() {
        try {
            System.out.println("Llamada al endpoint /dealer/active-order recibida");
            OrdersEntity activeOrder = ordersService.getActiveOrderByDealer();
            System.out.println("Orden activa encontrada: " + activeOrder);
            return ResponseEntity.ok(activeOrder);
        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Endpoint para asignar un pedido a un dealer.
     * Este endpoint asigna un pedido específico a un dealer basado en su ID.
     */
    @PutMapping("/{id}/assign")
    public ResponseEntity<Void> assignOrderToDealer(@PathVariable int id) {
        try {
            ordersService.assignOrderToDealer(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Endpoint para actualizar el estado de un pedido.
     * Este endpoint actualiza el estado de un pedido específico basado en su ID.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> requestBody) {

        try {
            String newStatus = requestBody.get("status");

            // Obtener dealerId del usuario autenticado
            Long userId = userService.getAuthenticatedUserId();
            Integer dealerId = jdbcTemplate.queryForObject(
                    "SELECT id FROM dealers WHERE user_id = ?",
                    Integer.class, userId
            );

            if (dealerId == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Verificar que la orden pertenece al dealer
            OrdersEntity order = ordersRepository.findById(id);
            if (order == null || !dealerId.equals(order.getDealerId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Actualizar estado
            if ("ENTREGADO".equals(newStatus)) {
                ordersService.markAsDelivered(id, new Date());
            } else if ("FALLIDA".equals(newStatus)) {
                ordersService.markAsFailed(id);
            } else {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Endpoint para obtener los pedidos con el conteo de productos por dealer.
     * Este endpoint devuelve una lista de pedidos con el conteo de productos específicos basados en el ID del dealer.
     */
    @GetMapping("/dealer/dto/orders")
    public ResponseEntity<List<OrderTotalProductsDTO>> getOrdersByDealerDto() {
        try {
            List<OrderTotalProductsDTO> orders = ordersService.getOrdersWithProductCountByDealerId();
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Error de validación
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Error inesperado
        }
    }

    /**
     * Endpoint para obtener la dirección y nombre de la orden activa del dealer autenticado.
     * Este endpoint devuelve la dirección y nombre de la orden activa del dealer autenticado en la base de datos.
     */
    @GetMapping("/dealer/dto/active-order")
    public ResponseEntity<OrderNameAddressDTO> getActiveOrderNameAddressByDealer() {
        try {
            OrderNameAddressDTO activeOrder = ordersService.getActiveOrderNameAddresDTOByDealerId();
            return ResponseEntity.ok(activeOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Error de validación
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Error inesperado
        }
    }


    /**
     * Endpoint para obtener los pedidos fallidos por ID de cliente.
     * Este endpoint devuelve una lista de pedidos fallidos específicos basados en el ID del cliente.
     */
    @GetMapping("/client/failed")
    public ResponseEntity<List<OrdersEntity>> getFailedOrdersByClient() {
        Long userId = userService.getAuthenticatedUserId();
        String sql = "SELECT id FROM clients WHERE user_id = ?";
        Integer clientId = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        String query = "SELECT * FROM orders WHERE client_id = ? AND status = 'FALLIDA' ORDER BY order_date DESC";
        List<OrdersEntity> failedOrders = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(OrdersEntity.class), clientId);

        return ResponseEntity.ok(failedOrders);
    }


    /**
     * Endpoint para actualizar la ruta de un pedido utilizando WKT o puntos.
     * Este endpoint actualiza la ruta de un pedido específico basado en su ID.
     */
    @PutMapping("/orders/{id}/route/wkt")
    public ResponseEntity<Void> updateRouteWithWKT(
            @PathVariable int id,
            @RequestBody Map<String, String> request) {

        String wkt = request.get("wkt");
        ordersService.updateOrderRouteWithWKT(id, wkt);
        return ResponseEntity.ok().build();
    }


    /**
     * Endpoint para actualizar la ruta de un pedido utilizando puntos.
     * Este endpoint actualiza la ruta de un pedido específico basado en su ID.
     */
    @PutMapping("/orders/{id}/route/points")
    public ResponseEntity<Void> updateRouteWithPoints(
            @PathVariable int id,
            @RequestBody List<Map<String, Double>> points) {

        ordersService.updateOrderRouteWithPoints(id, points);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene los pedidos cuya ruta estimada cruza más de 2 zonas de reparto
     * @return Lista de OrderNameAddressDTO con información de pedidos y clientes
     */
    @GetMapping("/crossing-multiple-zones")
    public ResponseEntity<List<OrderNameAddressDTO>> getOrdersCrossingMultipleZones() {
        List<OrderNameAddressDTO> orders = ordersService.getOrdersCrossingMultipleCoverageAreas();
        return ResponseEntity.ok(orders);
    }
}
