package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.dtos.OrderNameAddressDTO;
import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import bdavanzadas.lab1.dtos.OrderTotalProductsDTO;
import bdavanzadas.lab1.dtos.OrderNameAddressDTO;

import java.util.*;


/**
 *
 *  La clase OrdersRepository representa el repositorio de pedidos en la base de datos.
 *  Esta clase contiene métodos para guardar, actualizar, eliminar y buscar pedidos en la base de datos.
 *
 */
@Repository
public class OrdersRepository implements OrdersRepositoryInt {


    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Metodo para obtener todos los orders de la base de datos.
     * @return Una lista de orders.
     *
     */
    public List<OrdersEntity> findAll() {
        String sql = "SELECT id, order_date, delivery_date, status, client_id, dealer_id, total_price, ST_AsText(estimated_route) AS estimated_route FROM orders";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getObject("dealer_id") != null ? rs.getInt("dealer_id") : null,
                        rs.getDouble("total_price"),
                        rs.getString("estimated_route")
                )
        );
    }

    /**
     * Metodo para guardar un order en la base de datos.
     * @param "order" El order a guardar.
     * @return void
     *
     */
    public void save(OrdersEntity order) {
        String sql = "INSERT INTO orders (order_date, delivery_date, status, client_id,dealer_id , total_price) VALUES (?, ?, ?, ?, ?,?)";
        jdbcTemplate.update(sql, order.getOrderDate(), order.getDeliveryDate(), order.getStatus(), order.getClientId(),order.getDealerId() ,order.getTotalPrice());
    }



    /**
     * Metodo para actualizar un order en la base de datos.
     * @param "order" El order a actualizar.
     * @return void
     *
     */
    public void update(OrdersEntity order) {
        String sql = "UPDATE orders SET order_date = ?, delivery_date = ?, status = ?, client_id = ?,dealer_id= ?, total_price = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getOrderDate(), order.getDeliveryDate(), order.getStatus(), order.getClientId(),order.getDealerId(), order.getTotalPrice(), order.getId());
    }

    /**
     * Metodo para eliminar un order de la base de datos.
     * @param "id" El id del order a eliminar.
     * @return void
     *
     */
    public void delete(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    /**
     * Metodo para buscar un order por su id.
     * @param "id" El id del order a buscar.
     * @return El order encontrado.
     *
     */
    public OrdersEntity findById(int id) {
        String sql = "SELECT id, order_date, delivery_date, status, client_id, dealer_id, total_price, ST_AsText(estimated_route) AS estimated_route FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getObject("dealer_id") != null ? rs.getInt("dealer_id") : null,
                        rs.getDouble("total_price"),
                        rs.getString("estimated_route")
                ));
    }

    /**
     * Metodo para buscar un order por su clientId.
     * @param "clientId" El id del client a buscar.
     * @return El order encontrado.
     *
     */
    public List<OrdersEntity> findByClientId(int clientId) {
        String sql = "SELECT id, order_date, delivery_date, status, client_id, dealer_id, total_price, ST_AsText(estimated_route) AS estimated_route FROM orders WHERE client_id = ?";
        return jdbcTemplate.query(sql, new Object[]{clientId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getObject("dealer_id") != null ? rs.getInt("dealer_id") : null,
                        rs.getDouble("total_price"),
                        rs.getString("estimated_route")
                )
        );
    }
    /**
     * Metodo para buscar un order por su dealerId.
     * @param "dealerId" El id del dealer a buscar.
     * @return El order encontrado.
     *
     */
    public List<OrdersEntity> findByDealerId(int dealerId) {
        String sql = "SELECT id, order_date, delivery_date, status, client_id, dealer_id, total_price, ST_AsText(estimated_route) AS estimated_route FROM orders WHERE dealer_id = ?";
        return jdbcTemplate.query(sql, new Object[]{dealerId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getObject("dealer_id") != null ? rs.getInt("dealer_id") : null,
                        rs.getDouble("total_price"),
                        rs.getString("estimated_route")
                )
        );
    }


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
    public TopSpenderDTO getTopSpender() {
        String sql = """
        SELECT c.id, c.name, c.rut, c.email, c.phone, c.address, SUM(o.total_price) AS total_spent
        FROM orders o
        JOIN clients c ON o.client_id = c.id
        WHERE o.status = 'ENTREGADO'
        GROUP BY c.id, c.name, c.rut, c.email, c.phone, c.address
        ORDER BY total_spent DESC
        LIMIT 1
    """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            TopSpenderDTO topSpender = new TopSpenderDTO();
            topSpender.setId(rs.getInt("id"));
            topSpender.setName(rs.getString("name"));
            topSpender.setRut(rs.getString("rut"));
            topSpender.setEmail(rs.getString("email"));
            topSpender.setPhone(rs.getString("phone"));
            topSpender.setAddress(rs.getString("address"));
            topSpender.setTotalSpent(rs.getDouble("total_spent"));
            return topSpender;
        });
    }

    public List<OrdersEntity> findOrdersByMonth(int month, int year) {
        String sql = "SELECT id, order_date, delivery_date, status, client_id, dealer_id, total_price, ST_AsText(estimated_route) AS estimated_route FROM orders WHERE EXTRACT(MONTH FROM order_date) = ? AND EXTRACT(YEAR FROM order_date) = ?";
        return jdbcTemplate.query(sql, new Object[]{month, year}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getObject("dealer_id") != null ? rs.getInt("dealer_id") : null,
                        rs.getDouble("total_price"),
                        rs.getString("estimated_route")
                )
        );
    }


    /**
     *
     * Metodo para encontrar las entregas fallidas por ID de la compañia.*
     * el metodo realiza una consulta SQL que busca las ordenes fallidas de una compañia en especifico.
     * @param "companyId" El id de la compañia a buscar.
     * @return Una lista de orders fallidas.
     *
     * */
    public List<OrdersEntity> findFailedOrdersByCompanyId(int companyId) {
        String sql = """
    SELECT 
        o.id,
        o.order_date,
        o.delivery_date,
        o.status,
        o.client_id,
        o.dealer_id,
        o.total_price,
        ST_AsText(o.estimated_route) AS estimated_route
    FROM orders o
    JOIN order_products op ON o.id = op.order_id
    JOIN products p ON op.product_id = p.id
    JOIN companies c ON p.company_id = c.id
    WHERE c.id = ? AND o.status = 'FALLIDA'
    """;
        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getObject("dealer_id") != null ? rs.getInt("dealer_id") : null,
                        rs.getDouble("total_price"),
                        rs.getString("estimated_route")
                )
        );
    }



    /**
     *
     * Metodo para encontrar las entregas exitosas por ID de la compañia.*
     * el metodo realiza una consulta SQL que busca las ordenes exitosas de una compañia en especifico.
     * @param "companyId" El id de la compañia a buscar.
     * @return Una lista de orders exitosas.
     *
     * */

    public List<OrdersEntity> findDeliveredOrdersByCompanyId(int companyId) {
        String sql = """
    SELECT 
        o.id,
        o.order_date,
        o.delivery_date,
        o.status,
        o.client_id,
        o.dealer_id,
        o.total_price,
        ST_AsText(o.estimated_route) AS estimated_route
    FROM orders o
    JOIN order_products op ON o.id = op.order_id
    JOIN products p ON op.product_id = p.id
    JOIN companies c ON p.company_id = c.id
    WHERE c.id = ? AND o.status = 'ENTREGADO'
    """;
        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getObject("dealer_id") != null ? rs.getInt("dealer_id") : null,
                        rs.getDouble("total_price"),
                        rs.getString("estimated_route")
                )
        );
    }


    /**
     * Metodo para actualizar el estado de un pedido por ID de repartidor.
     * @param "orderId" El id del pedido a actualizar.
     * @param "dealerId" El id del repartidor que actualiza el pedido.
     * @param "newStatus" El nuevo estado del pedido.
     * @return void
     *
     */
    public void updateOrderStatusByDealerId(int orderId, int dealerId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ? AND dealer_id = ?";
        jdbcTemplate.update(sql, newStatus, orderId, dealerId);
    }
    /**
     * Metodo para obtener los pedidos de una compañia por ID de compañia.
     * @param "companyId" El id de la compañia a buscar.
     * @return Una lista de orders de la compañia.
     */
    //get all orders by company id
    public List<OrdersEntity> findOrdersByCompanyId(int companyId) {
        String sql = """
    SELECT 
        o.id,
        o.order_date,
        o.delivery_date,
        o.status,
        o.client_id,
        o.dealer_id,
        o.total_price,
        ST_AsText(o.estimated_route) AS estimated_route
    FROM orders o
    JOIN dealers d ON o.dealer_id = d.id
    JOIN products p ON d.id = p.company_id
    WHERE p.company_id = ?
    """;
        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getObject("dealer_id") != null ? rs.getInt("dealer_id") : null,
                        rs.getDouble("total_price"),
                        rs.getString("estimated_route")
                )
        );
    }


    /**
     * Metodo para obtener todos los productos de un pedido por ID de pedido.
     * @param "orderId" El id del pedido a buscar.
     * @return Una lista de productos del pedido.
     */
    //Get all products by order id
    public List<ProductEntity> findProductsByOrderId(int orderId) {
        String sql = "SELECT p.* FROM products p JOIN order_products op ON p.id = op.product_id WHERE op.order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) ->
                new ProductEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getFloat("price"),
                        rs.getString("category"),
                        rs.getInt("company_id")
                )
        );
    }


    /**
     * Metodo para obtener la orden En proceso por ID del repartidor.
     * @param "dealerId" El id del repartidor a buscar.
     * @return La orden En proceso encontrada.
     *
     */
    // Obtener la orden En proceso por ID del repartidor
    public OrdersEntity findActiveOrderByDealerId(int dealerId) {
        String sql = "SELECT id, order_date, delivery_date, status, client_id, dealer_id, total_price, ST_AsText(estimated_route) AS estimated_route FROM orders WHERE dealer_id = ? AND status = 'EN PROCESO'";
        List<OrdersEntity> orders = jdbcTemplate.query(sql, new Object[]{dealerId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getObject("dealer_id") != null ? rs.getInt("dealer_id") : null,
                        rs.getDouble("total_price"),
                        rs.getString("estimated_route")
                )
        );
        return orders.isEmpty() ? null : orders.get(0);
    }

    /**
     * Metodo para asignar un pedido a un repartidor.
     * @param "orderId" El id del pedido a asignar.
     * @param "dealerId" El id del repartidor a asignar.
     * @return void
     *
     */
    public void assignOrderToDealer(int orderId, int dealerId) {
        String sql = "UPDATE orders SET dealer_id = ?, status = 'EN PROCESO' WHERE id = ?";
        jdbcTemplate.update(sql, dealerId, orderId);
    }



    /**
     * Metodo para obtener los pedidos de un repartidor con el conteo de productos.
     * @param "dealerId" El id del repartidor a buscar.
     * @return Una lista de pedidos con el conteo de productos.
     *
     */
    public List<OrderTotalProductsDTO> findOrdersWithProductCountByDealerId(int dealerId) {
        String sql = """
        SELECT 
            o.id AS order_id,
            o.order_date,
            o.delivery_date,
            o.status,
            o.total_price,
            COALESCE(od.total_products, 0) AS total_products
        FROM 
            orders o
        LEFT JOIN 
            order_details od ON o.id = od.order_id
        WHERE 
            o.dealer_id = ?
        ORDER BY 
            o.order_date DESC
    """;

        return jdbcTemplate.query(sql, new Object[]{dealerId}, (rs, rowNum) ->
                new OrderTotalProductsDTO(
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date"),
                        rs.getTimestamp("delivery_date"),
                        rs.getString("status"),
                        rs.getDouble("total_price"),
                        rs.getInt("total_products")
                )
        );
    }


    /**
     * Metodo para obtener la orden En proceso por ID del repartidor con nombre y dirección del cliente.
     * @param "dealerId" El id del repartidor a buscar.
     * @return La orden En proceso encontrada con nombre y dirección del cliente.
     *
     */
    public OrderNameAddressDTO findActiveOrderNameAddresDTOByDealerId(int dealerId) {
        String sql = """
        SELECT 
            o.id AS order_id,
            o.order_date,
            o.delivery_date,
            o.status,
            o.total_price,
            o.client_id,
            c.name AS name_client,
            c.address AS client_address
        FROM 
            orders o
        LEFT JOIN 
            clients c ON o.client_id = c.id
        WHERE 
            o.dealer_id = ? AND o.status = 'EN PROCESO'
        LIMIT 1
    """;

        return jdbcTemplate.queryForObject(sql, new Object[]{dealerId}, (rs, rowNum) ->
                new OrderNameAddressDTO(
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date"),
                        rs.getTimestamp("delivery_date"),
                        rs.getString("status"),
                        rs.getDouble("total_price"),
                        rs.getInt("client_id"),
                        rs.getString("name_client"),
                        rs.getString("client_address")
                )
        );
    }

    public void updateEstimatedRoute(int orderId, String lineStringWKT) {
        String sql = "UPDATE orders SET estimated_route = ST_GeomFromText(?, 4326) WHERE id = ?";
        jdbcTemplate.update(sql, lineStringWKT, orderId);
    }

    public void updateEstimatedRouteFromPoints(int orderId, List<Map<String, Double>> points) {
        // Construir el array de puntos para ST_MakeLine
        StringBuilder pointsBuilder = new StringBuilder();
        List<Object> args = new ArrayList<>();

        for (Map<String, Double> point : points) {
            if (pointsBuilder.length() > 0) {
                pointsBuilder.append(", ");
            }
            pointsBuilder.append("ST_SetSRID(ST_MakePoint(?, ?), 4326)");
            args.add(point.get("longitude"));
            args.add(point.get("latitude"));
        }

        args.add(orderId);

        String sql = String.format(
                "UPDATE orders SET estimated_route = ST_MakeLine(ARRAY[%s]) WHERE id = ?",
                pointsBuilder.toString()
        );

        jdbcTemplate.update(sql, args.toArray());
    }

    /**
     * Método para obtener los pedidos cuya ruta estimada cruza más de 2 zonas de reparto.
     * Utiliza funciones geoespaciales para determinar las intersecciones entre la ruta del pedido
     * y las áreas de cobertura definidas en el sistema.
     *
     * @return La lista de ordenes con nombre y dirección del cliente que cruzan mas de 2 zonas.
     */
    public List<OrderNameAddressDTO> findOrdersCrossingMoreThanTwoCoverageAreas() {
        String sql = """
        SELECT 
            o.id AS order_id,
            o.order_date,
            o.delivery_date,
            o.status,
            o.total_price,
            o.client_id,
            c.name AS name_client,
            c.address AS client_address
        FROM 
            orders o
        LEFT JOIN 
            clients c ON o.client_id = c.id
        WHERE (
            SELECT COUNT(DISTINCT ca.coverage_id)
            FROM coverage_area ca
            WHERE ST_Crosses(o.estimated_route, ca.coverageArea)
               OR ST_Intersects(o.estimated_route, ca.coverageArea)
        ) > 2
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrderNameAddressDTO(
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date"),
                        rs.getTimestamp("delivery_date"),
                        rs.getString("status"),
                        rs.getDouble("total_price"),
                        rs.getInt("client_id"),
                        rs.getString("name_client"),
                        rs.getString("client_address")
                ));
    }

    public void updateOrderStatusAndDeliveryDate(int orderId, int dealerId, String status, Date deliveryDate) {
        jdbcTemplate.update(
                "UPDATE orders SET status = ?, delivery_date = ? WHERE id = ? AND dealer_id = ?",
                status, deliveryDate, orderId, dealerId
        );
    }
}

