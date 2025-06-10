package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.OrderDetailsEntity;
import bdavanzadas.lab1.services.OrderDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;


/**
 *
 * La clase OrderDetailsController maneja las solicitudes relacionadas con los detalles de las órdenes.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar detalles de órdenes en la base de datos.
 *
 *
 * */
@RestController
@RequestMapping("/order-details")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class OrderDetailsController {


    /**
     *
     * Servicio de detalles de órdenes.
     * Este servicio se utiliza para interactuar con la base de datos de detalles de órdenes.
     *
     * */
    private final OrderDetailsService orderDetailsService;

    /**
     *
     * Constructor de la clase OrderDetailsController.
     * @param "orderDetailsService" El servicio de detalles de órdenes a utilizar.
     *
     * */
    public OrderDetailsController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }


    /**
     *
     * Endpoint para obtener todos los detalles de órdenes.
     * Este endpoint devuelve una lista de todos los detalles de órdenes en la base de datos.
     *
     * */
    @GetMapping
    public ResponseEntity<List<OrderDetailsEntity>> getAllOrderDetails() {
        return ResponseEntity.ok(orderDetailsService.getAllOrderDetails());
    }


    /**
     *
     * Endpoint para obtener un detalle de orden por su ID.
     * Este endpoint devuelve un detalle de orden específico basado en su ID.
     *
     * */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsEntity> getOrderDetailById(@PathVariable int id) {
        OrderDetailsEntity orderDetail = orderDetailsService.getOrderDetailsById(id);
        if (orderDetail != null) {
            return ResponseEntity.ok(orderDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     *
     * Endpoint para crear un nuevo detalle de orden.
     * Este endpoint guarda un nuevo detalle de orden en la base de datos.
     *
     * */
    @PostMapping
    public ResponseEntity<Void> addOrderDetail(@RequestBody OrderDetailsEntity orderDetail) {
        orderDetailsService.saveOrderDetails(orderDetail);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     *
     * Endpoint para actualizar un detalle de orden existente.
     * Este endpoint actualiza un detalle de orden existente en la base de datos.
     *
     * */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrderDetail(@PathVariable int id, @RequestBody OrderDetailsEntity orderDetail) {
        orderDetail.setId(id);
        orderDetailsService.saveOrderDetails(orderDetail);
        return ResponseEntity.noContent().build();
    }



    /**
     *
     * Endpoint para eliminar un detalle de orden.
     * Este endpoint elimina un detalle de orden específico basado en su ID.
     *
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable int id) {
        orderDetailsService.deleteOrderDetails(id);
        return ResponseEntity.noContent().build();
    }


    /**
     *
     * Endpoint para obtener detalles de orden por ID de orden.
     * Este endpoint devuelve una lista de detalles de orden específicos basados en el ID de la orden.
     *
     * */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailsEntity>> getOrderDetailsByOrderId(@PathVariable int orderId) {
        List<OrderDetailsEntity> orderDetails = orderDetailsService.getOrderDetailsByOrderId(orderId);
        if (orderDetails != null && !orderDetails.isEmpty()) {
            return ResponseEntity.ok(orderDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Devuelve el metodo de pago más utilizado en órdenes urgentes.
     *
     * @return 200 OK con un mapa {@code {paymentMethod: cantidad}}.
     */
    @GetMapping("/urgent/payment-method")
    public ResponseEntity<Map<String, Integer>> getMostUsedPaymentMethodForUrgentOrders() {
        Map<String, Integer> result = orderDetailsService.getMostUsedPaymentMethodForUrgentOrders();
        return ResponseEntity.ok(result);
    }
    /**
     * Crea detalles de orden para la última orden registrada.
     * @param requestBody mapa con los datos extraídos del body.
     * @return 201 CREATED o 500 INTERNAL SERVER ERROR.
     */
    @PostMapping("/create-for-last-order")
    public ResponseEntity<Void> createOrderDetailsForLastOrder(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extraer los valores del JSON
            String paymentMethod = (String) requestBody.get("paymentMethod");
            int totalProducts = (int) requestBody.get("totalProducts");
            double price = ((Number) requestBody.get("price")).doubleValue();

            // Llama al servicio para crear los detalles de la orden
            orderDetailsService.createOrderDetailsForLastOrder(paymentMethod, totalProducts, price);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
