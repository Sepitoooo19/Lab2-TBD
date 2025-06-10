package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.OrderDetailsEntity;

import java.util.List;
import java.util.Map;


/**
 * La interfaz OrderDetailsRepositoryInt define los métodos para interactuar con la base de datos de orderDetails.
 * Esta interfaz contiene métodos para guardar, actualizar, eliminar y buscar orderDetails en la base de datos.
 *
 */
public interface OrderDetailsRepositoryInt {

    /**
     * Metodo para obtener todos los orderDetails de la base de datos.
     * @return Una lista de orderDetails.
     *
     */
    List<OrderDetailsEntity> findAll();

    /**
     * Metodo para guardar un orderDetails en la base de datos.
     * @param "orderDetails" El orderDetails a guardar.
     * @return void
     *
     */
    void save(OrderDetailsEntity orderDetails);

    /**
     * Metodo para actualizar un orderDetails en la base de datos.
     * @param "orderDetails" El orderDetails a actualizar.
     * @return void
     *
     */
    void update(OrderDetailsEntity orderDetails);

    /**
     * Metodo para eliminar un orderDetails de la base de datos.
     * @param "id" El id del orderDetails a eliminar.
     * @return void
     *
     */
    void delete(int id);

    /**
     * Metodo para buscar un orderDetails por su id.
     * @param "id" El id del orderDetails a buscar.
     * @return El orderDetails encontrado.
     *
     */
    OrderDetailsEntity findById(int id);

    /**
     * Metodo para buscar un orderDetails por su orderId.
     * @param "orderId" El id del orderDetails a buscar.
     * @return El orderDetails encontrado.
     *
     */
    List<OrderDetailsEntity> findByOrderId(int orderId);

    /**
     * Metodo para encontrar el medio de pago mas utilizado en los pedidos urgentes.
     * Cuenta la cantidad de veces que fue utilizado y lo devuelve en un mapa.
     * @return Un mapa con el medio de pago y la cantidad de veces que fue utilizado.
     *
     */
    //RF 06: medio de pago en pedidos urgentes
    Map<String, Integer> getMostUsedPaymentMethodForUrgentOrders();


}
