package bdavanzadas.lab1.repositories;

import java.util.List;


import bdavanzadas.lab1.dtos.OrderNameAddressDTO;
import bdavanzadas.lab1.dtos.OrderTotalProductsDTO;
import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.entities.ProductEntity;



/**
 * La interfaz OrdersRepositoryInt define los métodos para interactuar con la base de datos de orders.
 * Esta interfaz contiene métodos para guardar, actualizar, eliminar y buscar orders en la base de datos.
 *
 */
public interface OrdersRepositoryInt {


    /**
     * Metodo para obtener todos los orders de la base de datos.
     * @return Una lista de orders.
     *
     */
    List<OrdersEntity> findAll();

    /**
     * Metodo para guardar un order en la base de datos.
     * @param "order" El order a guardar.
     * @return void
     *
     */
    void save(OrdersEntity order);

    /**
     * Metodo para actualizar un order en la base de datos.
     * @param "order" que se actualizará.
     * @return void
     *
     */
    void update(OrdersEntity order);

    /**
     * Metodo para eliminar un order de la base de datos.
     * @param "id" El id del order a eliminar.
     * @return void
     *
     */
    void delete(int id);

    /**
     * Metodo para buscar un order por su id.
     * @param "id" El id del order a buscar.
     * @return El order encontrado.
     *
     */
    OrdersEntity findById(int id);

    /**
     * Metodo para buscar un order por su clientId.
     * @param "clientId" El id del cliente.
     * @return Una lista de orders encontrados.
     *
     */
    List<OrdersEntity> findByClientId(int clientId);

    /**
     * Metodo para buscar un order por su dealerId.
     * @param "dealerId" El id del dealer.
     * @return Una lista de orders encontrados.
     *
     */
    List<OrdersEntity> findByDealerId(int dealerId);

    /**
     * Metodo para obtener el cliente que más ha gastado en pedidos entregados.
     * Se realiza una consulta SQL que agrupa los pedidos por cliente y suma el total gastado.
     * El cliente con el mayor gasto se selecciona y se devuelve como un objeto TopSpenderDTO.
     * @return Un objeto TopSpenderDTO que contiene la información del cliente que más ha gastado.
     *
     *
     *
     * */
    //RF 1: obtener el cliente que más ha gastado
    TopSpenderDTO getTopSpender();

    /**
     *
     * Metodo para encontrar las entregas fallidas por ID de la compañia.*
     * el metodo realiza una consulta SQL que busca las ordenes fallidas de una compañia en especifico.
     * @param "companyId" El id de la compañia a buscar.
     * @return Una lista de orders fallidas.
     *
     * */
    List<OrdersEntity> findFailedOrdersByCompanyId(int companyId);

    /**
     *
     * Metodo para encontrar las entregas exitosas por ID de la compañia.*
     * el metodo realiza una consulta SQL que busca las ordenes exitosas de una compañia en especifico.
     * @param "companyId" El id de la compañia a buscar.
     * @return Una lista de orders exitosas.
     *
     * */

    List<OrdersEntity> findDeliveredOrdersByCompanyId(int companyId);

    /**
     * Metodo para actualizar el estado de un pedido por ID de repartidor.
     * @param "orderId" El id del pedido a actualizar.
     * @param "dealerId" El id del repartidor que actualiza el pedido.
     * @param "newStatus" El nuevo estado del pedido.
     * @return void
     *
     */
    void updateOrderStatusByDealerId(int orderId, int dealerId, String newStatus);

    /**
     * Metodo para obtener los pedidos de una compañia por ID de compañia.
     * @param "companyId" El id de la compañia a buscar.
     * @return Una lista de orders de la compañia.
     */
    //get all orders by company id
    List<OrdersEntity> findOrdersByCompanyId(int companyId);

    /**
     * Metodo para obtener todos los productos de un pedido por ID de pedido.
     * @param "orderId" El id del pedido a buscar.
     * @return Una lista de productos del pedido.
     */
     List<ProductEntity> findProductsByOrderId(int orderId);

    /**
     * Metodo para obtener la orden En proceso por ID del repartidor.
     * @param "dealerId" El id del repartidor a buscar.
     * @return La orden En proceso encontrada.
     *
     */
    OrdersEntity findActiveOrderByDealerId(int dealerId);

    /**
     * Metodo para asignar un pedido a un repartidor.
     * @param "orderId" El id del pedido a asignar.
     * @param "dealerId" El id del repartidor a asignar.
     * @return void
     *
     */
    void assignOrderToDealer(int orderId, int dealerId);

    /**
     * Metodo para obtener los pedidos de un repartidor con el conteo de productos.
     * @param "dealerId" El id del repartidor a buscar.
     * @return Una lista de pedidos con el conteo de productos.
     *
     */
    List<OrderTotalProductsDTO> findOrdersWithProductCountByDealerId(int dealerId);

    /**
     * Metodo para obtener la orden En proceso por ID del repartidor con nombre y dirección del cliente.
     * @param "dealerId" El id del repartidor a buscar.
     * @return La orden En proceso encontrada con nombre y dirección del cliente.
     *
     */
    OrderNameAddressDTO findActiveOrderNameAddresDTOByDealerId(int dealerId);
}
