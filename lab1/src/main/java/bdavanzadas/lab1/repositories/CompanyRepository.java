package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CompanyRepository implements CompanyRepositoryInt {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    public void delete(int id) {
        String sql = "DELETE FROM companies WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

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
}