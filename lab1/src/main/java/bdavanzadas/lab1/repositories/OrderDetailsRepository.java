package bdavanzadas.lab1.repositories;
import bdavanzadas.lab1.entities.OrderDetailsEntity;
import bdavanzadas.lab1.entities.PaymentMethodEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 *
 *  La clase OrderDetailsRepository representa el repositorio de detalles de pedidos en la base de datos.
 *  Esta clase contiene métodos para guardar, actualizar, eliminar y buscar detalles de pedidos en la base de datos.
 *
 */
@Repository
public class OrderDetailsRepository implements OrderDetailsRepositoryInt {


    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Metodo para obtener todos los orderDetails de la base de datos.
     * @return Una lista de orderDetails.
     *
     */
    public List<OrderDetailsEntity> findAll()  {
        String sql = "SELECT * FROM order_details";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrderDetailsEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getString("payment_method"),
                        rs.getInt("total_products"),
                        rs.getDouble("price")
                )
        );
    }


    /**
     * Metodo para guardar un orderDetails en la base de datos.
     * @param "orderDetails" El orderDetails a guardar.
     * @return void
     *
     */
    public void save(OrderDetailsEntity orderDetails) {
        String sql = "INSERT INTO order_details (order_id, payment_method, total_products, price) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderDetails.getOrderId(), orderDetails.getPaymentMethod(), orderDetails.getTotalProducts(), orderDetails.getPrice());
    }


    /**
     * Metodo para actualizar un orderDetails en la base de datos.
     * @param "orderDetails" El orderDetails a actualizar.
     * @return void
     *
     */
    public void update(OrderDetailsEntity orderDetails) {
        String sql = "UPDATE order_details SET order_id = ?, payment_method = ?, total_products = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderDetails.getOrderId(), orderDetails.getPaymentMethod(), orderDetails.getTotalProducts(), orderDetails.getPrice());
    }

    /**
     * Metodo para eliminar un orderDetails de la base de datos.
     * @param "id" El id del orderDetails a eliminar.
     * @return void
     *
     */
    public void delete(int id) {
        String sql = "DELETE FROM order_details WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Metodo para buscar un orderDetails por su id.
     * @param "id" El id del orderDetails a buscar.
     * @return El orderDetails encontrado.
     *
     */
    public OrderDetailsEntity findById(int id) {
        String sql = "SELECT * FROM order_details WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new OrderDetailsEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getString("payment_method"),
                        rs.getInt("total_products"),
                        rs.getDouble("price")
                )
        );
    }

    /**
     * Metodo para buscar un orderDetails por su orderId.
     * @param "orderId" El id del orderDetails a buscar.
     * @return El orderDetails encontrado.
     *
     */
    public List<OrderDetailsEntity> findByOrderId(int orderId) {
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) ->
                new OrderDetailsEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getString("payment_method"),
                        rs.getInt("total_products"),
                        rs.getDouble("price")
                )
        );
    }


    /**
     * Metodo para encontrar el medio de pago mas utilizado en los pedidos urgentes.
     * Cuenta la cantidad de veces que fue utilizado y lo devuelve en un mapa.
     * @return Un mapa con el medio de pago y la cantidad de veces que fue utilizado.
     *
     */
    //RF 06: medio de pago en pedidos urgentes
    public Map<String, Integer> getMostUsedPaymentMethodForUrgentOrders() {
        String sql = """
            SELECT 
                od.payment_method, 
                COUNT(*) AS usage_count
            FROM 
                orders o
            JOIN 
                order_details od ON o.id = od.order_id
            WHERE 
                o.status = 'URGENTE'
            GROUP BY 
                od.payment_method
            ORDER BY 
                usage_count DESC
            LIMIT 1;
        """;

        return jdbcTemplate.query(sql, rs -> {
            Map<String, Integer> result = new HashMap<>();
            if (rs.next()) {
                result.put(rs.getString("payment_method"), rs.getInt("usage_count"));
            }
            return result;
        });
    }


}
