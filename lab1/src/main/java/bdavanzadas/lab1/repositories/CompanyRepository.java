package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.dtos.NearestDeliveryPointDTO;
import bdavanzadas.lab1.entities.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * La clase CompanyRepository representa el repositorio de compañías en la base de datos.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar compañías en la base de datos.
 */
@Repository
public class CompanyRepository implements CompanyRepositoryInt {


    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Método para obtener todas las compañías con sus métricas de entregas, ventas y fallidas.
     * @return Lista de CompanyEntity con las métricas correspondientes.
     */
    public List<CompanyEntity> findAll() {
        String sql = """
        SELECT 
            c.id,
            c.name,
            c.email,
            c.phone,
            c.address,
            c.rut,
            c.type,
            ST_AsText(c.ubication) AS ubication,
            COUNT(o.id) AS deliveries,
            SUM(CASE WHEN o.status = 'FALLIDA' THEN 1 ELSE 0 END) AS failed_deliveries,
            SUM(o.total_price) AS total_sales
        FROM 
            companies c
        LEFT JOIN orders o ON c.id = o.client_id
        GROUP BY 
            c.id, c.name, c.email, c.phone, c.address, c.rut, c.type, c.ubication
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CompanyEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("rut"),
                        rs.getString("type"),
                        rs.getInt("deliveries"),
                        rs.getInt("failed_deliveries"),
                        rs.getInt("total_sales"),
                        rs.getString("ubication")
                )
        );
    }


    /**
     * Método para buscar una compañía por su ID.
     * @param id ID de la compañía a buscar.
     * @return CompanyEntity con los datos de la compañía.
     */
    public CompanyEntity findbyid(int id) {
        String sql = "SELECT id, name, email, phone, address, rut, type, deliveries, failed_deliveries, total_sales, ST_AsText(ubication) AS ubication FROM companies WHERE id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new CompanyEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("rut"),
                        rs.getString("type"),
                        rs.getInt("deliveries"),
                        rs.getInt("failed_deliveries"),
                        rs.getInt("total_sales"),
                        rs.getString("ubication"))
        );
    }

    /**
     * Método para crear una nueva compañía en la base de datos.
     * @param c CompanyEntity con los datos de la compañía a crear.
     *          * Este método inserta una nueva compañía en la tabla "companies" con los datos proporcionados.
     */
    public void save(CompanyEntity c) {
        String sql = "INSERT INTO companies (name, email, phone, address, rut, type, deliveries, failed_deliveries, total_sales, ubication) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 4326))";
        jdbcTemplate.update(sql,
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getAddress(),
                c.getRut(),
                c.getType(),
                c.getDeliveries(),
                c.getFailedDeliveries(),
                c.getTotalSales(),
                c.getUbication());
    }


    /**
     * Método para actualizar una compañía en la base de datos.
     * @param c CompanyEntity con los datos de la compañía a actualizar.
     *          * Este método actualiza los datos de una compañía existente en la tabla "companies".
     */
    public void update(CompanyEntity c) {
        String sql = "UPDATE companies SET name = ?, email = ?, phone = ?, address = ?, rut = ?, type = ?, " +
                "deliveries = ?, failed_deliveries = ?, total_sales = ?, ubication = ST_GeomFromText(?, 4326) " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getAddress(),
                c.getRut(),
                c.getType(),
                c.getDeliveries(),
                c.getFailedDeliveries(),
                c.getTotalSales(),
                c.getUbication(),
                c.getId());
    }


    /**
     * Método para eliminar una compañía de la base de datos.
     * @param id ID de la compañía a eliminar.
     *          * Este método elimina una compañía de la tabla "companies" por su ID.
     */
    public void delete(int id) {
        String sql = "DELETE FROM companies WHERE id=?";
        jdbcTemplate.update(sql, id);
    }


    /**
     * Método para obtener las compañías con más entregas fallidas.
     * @return Lista de CompanyEntity con las compañías y sus métricas de entregas fallidas.
     */
    public List<CompanyEntity> getCompaniesWithMostFailedDeliveries() {
        String sql = """
        SELECT
            c.id,
            c.name,
            c.email,
            c.phone,
            c.address,
            c.rut,
            c.type,
            ST_AsText(c.ubication) AS ubication,
            COUNT(o.id) AS deliveries,
            SUM(CASE WHEN o.status = 'FALLIDA' THEN 1 ELSE 0 END) AS failed_deliveries,
            SUM(o.total_price) AS total_sales
        FROM
            companies c
        LEFT JOIN orders o ON c.id = o.client_id
        GROUP BY
            c.id, c.name, c.email, c.phone, c.address, c.rut, c.type, c.ubication
        ORDER BY failed_deliveries DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CompanyEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("rut"),
                        rs.getString("type"),
                        rs.getInt("deliveries"),
                        rs.getInt("failed_deliveries"),
                        rs.getInt("total_sales"),
                        rs.getString("ubication")
                )
        );
    }


    /**
     * Método para actualizar las métricas de las compañías en la base de datos.
     * Este método recalcula el número de entregas, entregas fallidas y ventas totales para cada compañía.
     */

    public void updateCompanyMetrics() {
        String sql = """
        UPDATE companies c
        SET 
            deliveries = COALESCE((
                SELECT COUNT(o.id)
                FROM orders o
                JOIN order_products op ON o.id = op.order_id
                JOIN products p ON op.product_id = p.id
                WHERE p.company_id = c.id
            ), 0),
            failed_deliveries = COALESCE((
                SELECT COUNT(o.id)
                FROM orders o
                JOIN order_products op ON o.id = op.order_id
                JOIN products p ON op.product_id = p.id
                WHERE p.company_id = c.id AND o.status = 'FALLIDA'
            ), 0),
            total_sales = COALESCE((
                SELECT SUM(o.total_price)
                FROM orders o
                JOIN order_products op ON o.id = op.order_id
                JOIN products p ON op.product_id = p.id
                WHERE p.company_id = c.id
            ), 0);
        """;
        jdbcTemplate.update(sql);
    }


    /**
     * Método para obtener las compañías ordenadas por el volumen de comida entregada.
     * @return Lista de mapas con los datos de las compañías y el volumen total de comida entregada.
     */
    public List<Map<String, Object>> getCompaniesByDeliveredFoodVolume() {
        String sql = """
            SELECT 
                c.id AS company_id, 
                c.name AS company_name, 
                c.type AS company_type,
                ST_AsText(c.ubication) AS company_ubication,
                SUM(od.total_products) AS total_food_delivered
            FROM 
                companies c
            JOIN 
                products p ON c.id = p.company_id
            JOIN 
                order_products op ON p.id = op.product_id
            JOIN 
                orders o ON op.order_id = o.id
            JOIN 
                order_details od ON o.id = od.order_id
            WHERE 
                o.status = 'ENTREGADO'
            GROUP BY 
                c.id, c.name, c.type, c.ubication
            ORDER BY 
                total_food_delivered DESC;
        """;
        return jdbcTemplate.queryForList(sql);
    }



    /**
     * Método para encontrar los puntos de entrega más cercanos a una compañía específica.
     * @param companyId ID de la compañía para la cual se buscan los puntos de entrega.
     * @param limit Número máximo de puntos de entrega a retornar.
     * @return Lista de NearestDeliveryPointDTO con los puntos de entrega más cercanos.
     */
    public List<NearestDeliveryPointDTO> findNearestDeliveryPoints(int companyId, int limit) {
        String sql = """
            SELECT 
                c.id AS client_id,
                c.name AS client_name,
                c.address AS client_address,
                ST_AsText(c.ubication) AS client_location,
                comp.name AS company_name,
                ST_Distance(c.ubication::geography, comp.ubication::geography) AS distance_meters
            FROM 
                clients c
            CROSS JOIN 
                companies comp
            WHERE 
                comp.id = ? 
                AND c.ubication IS NOT NULL
                AND comp.ubication IS NOT NULL
            ORDER BY 
                distance_meters ASC
            LIMIT ?
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new NearestDeliveryPointDTO(
                        rs.getInt("client_id"),
                        rs.getString("client_name"),
                        rs.getString("client_address"),
                        rs.getString("client_location"),
                        rs.getString("company_name"),
                        rs.getDouble("distance_meters")
                ), companyId, limit);
    }



    /**
     * Método para encontrar el punto de entrega más lejano para cada compañía.
     * @return Lista de mapas con el nombre de la compañía y el punto de entrega más lejano.
     */
    public List<Map<String, Object>> findFarthestDeliveryPointForEachCompany() {
        String sql = """
        WITH RankedDeliveries AS (
            SELECT
                c.name AS company_name,
                ST_EndPoint(o.estimated_route) AS delivery_point,
                ROW_NUMBER() OVER(
                    PARTITION BY c.id
                    ORDER BY ST_Distance(c.ubication::geography, ST_EndPoint(o.estimated_route)::geography) DESC
                ) as rn
            FROM
                companies c
            JOIN products p ON c.id = p.company_id
            JOIN order_products op ON p.id = op.product_id
            JOIN orders o ON op.order_id = o.id
            WHERE
                o.status IN ('URGENTE', 'PENDIENTE')
                AND o.estimated_route IS NOT NULL
                AND c.ubication IS NOT NULL
        )
        SELECT
            company_name,
            ST_AsText(delivery_point) AS farthest_delivery_point
        FROM
            RankedDeliveries
        WHERE
            rn = 1;
    """;
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * Método para obtener el ID de una compañía por su nombre
     * @param name Nombre de la compañía a buscar
     * @return ID de la compañía o null si no se encuentra
     */
    public Integer findIdByName(String name) {
        String sql = "SELECT id FROM companies WHERE name = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}