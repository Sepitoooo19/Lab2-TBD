package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.DealerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;



/**
 *
 *  La clase DealerRepository representa el repositorio de repartidores en la base de datos.
 *  Esta clase contiene métodos para guardar, actualizar, eliminar y buscar repartidores en la base de datos.
 *
 * */
@Repository
public class DealerRepository  implements DealerRepositoryInt {

    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Metodo para guardar un dealer en la base de datos.
     * @param "dealer" El dealer a guardar.
     * @return void
     *
     */
    public void save(DealerEntity dealer) {
        String sql = "INSERT INTO dealers (user_id, name, rut, email, phone, vehicle, plate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, dealer.getUserId(), dealer.getName(), dealer.getRut(), dealer.getEmail(), dealer.getPhone(), dealer.getVehicle(), dealer.getPlate());
    }


    /**
     * Metodo para actualizar un dealer en la base de datos.
     * @param "dealer" El dealer a actualizar.
     * @return void
     *
     */
    public void update(DealerEntity dealer) {
        String sql = "UPDATE dealers SET name = ?, rut = ?, email = ?, phone = ?, vehicle = ?, plate = ? WHERE id = ?";
        jdbcTemplate.update(sql, dealer.getName(), dealer.getRut(), dealer.getEmail(), dealer.getPhone(), dealer.getVehicle(), dealer.getPlate(), dealer.getId());
    }

    /**
     * Metodo para eliminar un dealer de la base de datos.
     * @param "id" El id del dealer a eliminar.
     * @return void
     *
     */
    public void delete(int id) {
        String sql = "DELETE FROM dealers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Metodo para buscar un dealer por su id.
     * @param "id" El id del dealer a buscar.
     * @return El dealer encontrado.
     *
     */
    public DealerEntity findById(int id) {
        String sql = "SELECT * FROM dealers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            DealerEntity dealer = new DealerEntity();
            dealer.setId(rs.getInt("id"));
            dealer.setName(rs.getString("name"));
            dealer.setRut(rs.getString("rut"));
            dealer.setEmail(rs.getString("email"));
            dealer.setPhone(rs.getString("phone"));
            dealer.setVehicle(rs.getString("vehicle"));
            dealer.setPlate(rs.getString("plate"));
            return dealer;
        });
    }



    /**
     * Metodo para buscar todos los dealers.
     * @return Una lista de todos los dealers.
     *
     */
    public List<DealerEntity> findAll() {
        String sql = "SELECT * FROM dealers";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DealerEntity dealer = new DealerEntity();
            dealer.setId(rs.getInt("id"));
            dealer.setName(rs.getString("name"));
            dealer.setRut(rs.getString("rut"));
            dealer.setEmail(rs.getString("email"));
            dealer.setPhone(rs.getString("phone"));
            dealer.setVehicle(rs.getString("vehicle"));
            dealer.setPlate(rs.getString("plate"));
            return dealer;
        });
    }


    /**
     * Metodo para obtener el tiempo promedio de entrega por repartidor.
     * Este método realiza una consulta SQL que calcula el tiempo promedio entre la fecha de pedido y la fecha de entrega para cada repartidor.
     * El resultado se agrupa por el ID y nombre del repartidor, y se ordena de menor a mayor tiempo promedio.
     * @return Una lista de mapas, donde cada mapa contiene el ID y nombre del repartidor, y el tiempo promedio de entrega en horas.
     *
     */
    //RF 04: tiempo promedio entre entrega y pedido por repartidor
    public List<Map<String, Object>> getAverageDeliveryTimeByDealer() {
        String sql = """
            SELECT 
                d.id AS dealer_id,
                d.name AS dealer_name,
                AVG(EXTRACT(EPOCH FROM (o.delivery_date - o.order_date)) / 3600) AS avg_delivery_time_hours
            FROM 
                orders o
            JOIN 
                dealers d ON o.dealer_id = d.id
            WHERE 
                o.status = 'ENTREGADO' AND o.delivery_date IS NOT NULL
            GROUP BY 
                d.id, d.name
            ORDER BY 
                avg_delivery_time_hours ASC;
        """;

        return jdbcTemplate.queryForList(sql);
    }


    /**
     * Metodo para buscar el nombre de un dealer por su id.
     * @param "dealerId" El id del dealer a buscar.
     * @return El nombre del dealer encontrado o "Sin asignar" si no se encuentra.
     *
     */
    public String findDealerNameById(int dealerId) {
        String sql = "SELECT name FROM dealers WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{dealerId}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "Sin asignar"; // Si no se encuentra el dealer, devuelve "Sin asignar"
        }
    }


    /**
     * Metodo para buscar los 3 mejores repartidores.
     * Este método realiza una consulta SQL que calcula el puntaje de rendimiento de cada repartidor basado en el número de entregas y la calificación promedio.
     * El puntaje se calcula como 70% del número de entregas y 30% de la calificación promedio.
     * El resultado se agrupa por el ID y nombre del repartidor, y se ordena de mayor a menor puntaje.
     * @return Una lista de mapas, donde cada mapa contiene el ID y nombre del repartidor, el total de entregas, la calificación promedio y el puntaje de rendimiento.
     *
     */
    //RF 05: tres mejores repartidores
    public List<Map<String, Object>> getTopPerformingDealers() {
        String sql = """
            SELECT 
                d.id AS dealer_id,
                d.name AS dealer_name,
                COUNT(o.id) AS total_deliveries,
                COALESCE(AVG(r.rating), 0) AS avg_rating,
                (COUNT(o.id) * 0.7 + COALESCE(AVG(r.rating), 0) * 0.3) AS performance_score
            FROM 
                dealers d
            LEFT JOIN 
                orders o ON d.id = o.dealer_id AND o.status = 'ENTREGADO'
            LEFT JOIN 
                ratings r ON d.id = r.dealer_id
            GROUP BY 
                d.id, d.name
            ORDER BY 
                performance_score DESC
            LIMIT 3;
        """;

        return jdbcTemplate.queryForList(sql);
    }


    /**
     * Metodo para obtener el tiempo promedio de entrega por repartidor autenticado.
     * Este método realiza una consulta SQL que calcula el tiempo promedio entre la fecha de pedido y la fecha de entrega para el repartidor autenticado.
     * El resultado se agrupa por el ID y nombre del repartidor, y se ordena de menor a mayor tiempo promedio.
     * @param "userId" El id del usuario autenticado.
     * @return El tiempo promedio de entrega en horas.
     *
     */
    public Double getAverageDeliveryTimeByAuthenticatedDealer(Long userId) {
        // Obtener el dealerId asociado al usuario autenticado
        String sqlDealerId = "SELECT id FROM dealers WHERE user_id = ?";
        Integer dealerId = jdbcTemplate.queryForObject(sqlDealerId, new Object[]{userId}, Integer.class);

        if (dealerId == null) {
            throw new IllegalArgumentException("No se encontró un repartidor asociado al usuario autenticado.");
        }

        // Calcular el tiempo promedio de espera para el dealerId
        String sqlAvgTime = """
            SELECT AVG(EXTRACT(EPOCH FROM (o.delivery_date - o.order_date)) / 3600) AS avg_delivery_time_hours
            FROM orders o
            WHERE o.dealer_id = ? AND o.status = 'ENTREGADO' AND o.delivery_date IS NOT NULL
        """;

        return jdbcTemplate.queryForObject(sqlAvgTime, new Object[]{dealerId}, Double.class);
    }


    /**
     * Metodo para contar el número de entregas realizadas por el repartidor autenticado.
     * Este método realiza una consulta SQL que cuenta el número de entregas realizadas por el repartidor autenticado.
     * @param "userId" El id del usuario autenticado.
     * @return El número de entregas realizadas.
     *
     */
    public Integer getDeliveryCountByAuthenticatedDealer(Long userId) {
        // Obtener el dealerId asociado al usuario autenticado
        String sqlDealerId = "SELECT id FROM dealers WHERE user_id = ?";
        Integer dealerId = jdbcTemplate.queryForObject(sqlDealerId, new Object[]{userId}, Integer.class);

        if (dealerId == null) {
            throw new IllegalArgumentException("No se encontró un repartidor asociado al usuario autenticado.");
        }

        // Contar las entregas realizadas por el dealerId
        String sqlDeliveryCount = """
            SELECT COUNT(*) 
            FROM orders 
            WHERE dealer_id = ? AND status = 'ENTREGADO'
        """;

        return jdbcTemplate.queryForObject(sqlDeliveryCount, new Object[]{dealerId}, Integer.class);
    }

    /**
     * Obtiene la calificación promedio de un repartidor
     * @param dealerId ID del repartidor
     * @return Calificación promedio o null si no hay calificaciones
     */
    public Double getAverageRating(Integer dealerId) {
        String sql = """
        SELECT AVG(r.rating) 
        FROM ratings r 
        WHERE r.dealer_id = ?
    """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{dealerId}, Double.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Obtiene el número de entregas de un repartidor
     * @param dealerId ID del repartidor
     * @return Número de entregas
     */
    public Integer getDeliveryCount(Integer dealerId) {
        String sql = """
        SELECT COUNT(*) 
        FROM orders 
        WHERE dealer_id = ? AND status = 'ENTREGADO'
    """;

        return jdbcTemplate.queryForObject(sql, new Object[]{dealerId}, Integer.class);
    }

    /**
     * Obtiene el tiempo promedio de entrega de un repartidor
     * @param dealerId ID del repartidor
     * @return Tiempo promedio en horas o null si no hay entregas
     */
    public Double getAverageDeliveryTime(Integer dealerId) {
        String sql = """
        SELECT AVG(EXTRACT(EPOCH FROM (o.delivery_date - o.order_date)) / 3600)
        FROM orders o
        WHERE o.dealer_id = ? AND o.status = 'ENTREGADO' AND o.delivery_date IS NOT NULL
    """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{dealerId}, Double.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    /**
     * Busca un repartidor por el ID de usuario
     * @param userId ID del usuario asociado al repartidor
     * @return Entidad del repartidor
     * Este metodo es para que funcione otro metodo
     */
    public DealerEntity findByUserId(int userId) {
        String sql = "SELECT * FROM dealers WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> {
                DealerEntity dealer = new DealerEntity();
                dealer.setId(rs.getInt("id"));
                dealer.setUserId(rs.getInt("user_id"));
                dealer.setName(rs.getString("name"));
                dealer.setRut(rs.getString("rut"));
                dealer.setEmail(rs.getString("email"));
                dealer.setPhone(rs.getString("phone"));
                dealer.setVehicle(rs.getString("vehicle"));
                dealer.setPlate(rs.getString("plate"));
                return dealer;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }




}