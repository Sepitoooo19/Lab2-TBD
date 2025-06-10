package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.OrderDetailsEntity;
import bdavanzadas.lab1.repositories.OrderDetailsRepository;

import java.util.List;
import java.util.Map;


/**
 *
 * La clase OrderDetailsService representa el servicio de detalles de órdenes en la aplicación.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar detalles de órdenes en la base de datos.
 *
 */
@Service
public class OrderDetailsService {


    /**
     * Repositorio de detalles de órdenes.
     * Este repositorio se utiliza para interactuar con la base de datos de detalles de órdenes.
     */
    private final OrderDetailsRepository orderDetailsRepository;

    /**
     * Servicio de órdenes.
     * Este servicio se utiliza para interactuar con la base de datos de órdenes.
     */
    private final OrdersService ordersService; // Inyección del servicio de órdenes


    /**
     * Constructor de la clase OrderDetailsService.
     * @param "orderDetailsRepository" El repositorio de detalles de órdenes a utilizar.
     * @param "ordersService" El servicio de órdenes a utilizar.
     */
    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository, OrdersService ordersService) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.ordersService = ordersService; // Inicialización del servicio de órdenes
    }


    /**
     * Metodo para obtener todos los detalles de órdenes de la base de datos.
     * @return Una lista de detalles de órdenes.
     *
     */
    @Transactional(readOnly = true)
    public List<OrderDetailsEntity> getAllOrderDetails() {
        return orderDetailsRepository.findAll();
    }


    /**
     * Metodo para guardar detalles de órdenes en la base de datos.
     * @param "orderDetails" Los detalles de la orden a guardar.
     * @return void
     *
     */
    @Transactional
    public void saveOrderDetails(OrderDetailsEntity orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }


    /**
     * Metodo para eliminar detalles de órdenes de la base de datos en base a su id.
     * @param "id" El id de los detalles de la orden a eliminar.
     * @return void
     *
     */
    @Transactional
    public void deleteOrderDetails(int id) {
        orderDetailsRepository.delete(id);
    }


    /**
     * Metodo para buscar detalles de órdenes por su id.
     * @param "id" El id de los detalles de la orden a buscar.
     * @return Los detalles de la orden encontrados.
     *
     */
    @Transactional(readOnly = true)
    public OrderDetailsEntity getOrderDetailsById(int id) {
        return orderDetailsRepository.findById(id);
    }


    /**
     * Metodo para buscar detalles de órdenes por su orderId.
     * @param "orderId" El id de la orden a buscar.
     * @return Los detalles de la orden encontrados.
     *
     */
    @Transactional(readOnly = true)
    public List<OrderDetailsEntity> getOrderDetailsByOrderId(int orderId) {
        return orderDetailsRepository.findByOrderId(orderId);
    }


    /**
     * Metodo para buscar los metodos de pagos más utilizados en los pedidos urgentes.
     * @return Un mapa con los metodos de pagos más utilizados y su cantidad.
     *
     *
     */
    @Transactional(readOnly = true)
    public Map<String, Integer> getMostUsedPaymentMethodForUrgentOrders() {
        return orderDetailsRepository.getMostUsedPaymentMethodForUrgentOrders();
    }


    /**
     * Metodo para crear detalles de órdenes para el último pedido insertado.
     * @param "paymentMethod" El método de pago utilizado.
     * @param "totalProducts" El total de productos en la orden.
     * @param "price" El precio total de la orden.
     * @return void
     */
    @Transactional
    public void createOrderDetailsForLastOrder(String paymentMethod, int totalProducts, double price) {
        // Obtener el ID del último pedido insertado
        int lastOrderId = ordersService.getLastInsertedOrderId();

        // Crear los detalles de la orden
        OrderDetailsEntity orderDetails = new OrderDetailsEntity();
        orderDetails.setOrderId(lastOrderId);
        orderDetails.setPaymentMethod(paymentMethod);
        orderDetails.setTotalProducts(totalProducts);
        orderDetails.setPrice(price);

        // Guardar los detalles en la base de datos
        orderDetailsRepository.save(orderDetails);
    }
}