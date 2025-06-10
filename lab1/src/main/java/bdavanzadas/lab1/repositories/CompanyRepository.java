package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 *
 *  La clase CompanyRepository representa el repositorio de empresas en la base de datos.
 *  Esta clase contiene métodos para guardar, actualizar, eliminar y buscar empresas en la base de datos.
 *
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
     * Metodo para buscar todas las empresas en la base de datos, incluyendo el total de entregas, entregas fallidas y total de ventas.
     * @return Una lista de empresas.
     *
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
            COUNT(o.id) AS deliveries, -- Total de entregas
            SUM(CASE WHEN o.status = 'FALLIDA' THEN 1 ELSE 0 END) AS failed_deliveries, -- Total de entregas fallidas
            SUM(o.total_price) AS total_sales -- Total de ventas
        FROM 
            companies c
        LEFT JOIN orders o ON c.id = o.client_id -- Relación entre empresas y pedidos
        GROUP BY 
            c.id, c.name, c.email, c.phone, c.address, c.rut, c.type
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
                        rs.getInt("deliveries"), // Total de entregas
                        rs.getInt("failed_deliveries"), // Total de entregas fallidas
                        rs.getInt("total_sales") // Total de ventas
                )
        );
    }


    /**
     * Metodo para buscar una empresa por su id.
     * @param "id" El id de la empresa a buscar.
     * @return La empresa encontrada.
     *
     */
    public CompanyEntity findbyid(int id){
        String sql = "SELECT * FROM companies WHERE id=?";
        return jdbcTemplate.queryForObject(sql,new Object[]{id},(rs,rowNum)->
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
                        rs.getInt("total_sales")
                ));
    }



    /**
     * Metodo para buscar una empresa por su rut.
     * @param "rut" El rut de la empresa a buscar.
     * @return La empresa encontrada.
     *
     */
    public void save(CompanyEntity c) {
        String sql = "INSERT INTO companies (name, email, phone, address, rut, type, deliveries, failed_deliveries, total_sales) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, c.getName(), c.getEmail(), c.getPhone(), c.getAddress(), c.getRut(), c.getType(), c.getDeliveries(), c.getFailedDeliveries(), c.getTotalSales());
    }



    /**
     * Metodo para actualizar una empresa en la base de datos.
     * @param "c" La empresa a actualizar.
     * @return void
     *
     */
    public void update(CompanyEntity c) {
        String sql = "UPDATE companies SET name = ?, email = ?, phone = ?, address = ?, rut = ?, type = ?, deliveries = ?, failed_deliveries = ?, total_sales = ? WHERE id = ?";
        jdbcTemplate.update(sql, c.getName(), c.getEmail(), c.getPhone(), c.getAddress(), c.getRut(), c.getType(), c.getDeliveries(), c.getFailedDeliveries(), c.getTotalSales(), c.getId());
    }


    /**
     * Metodo para eliminar una empresa de la base de datos.
     * @param "id" El id de la empresa a eliminar.
     * @return void
     *
     */
   public  void delete(int id){
        String sql = "DELETE FROM companies WHERE id=?";
        jdbcTemplate.update(sql,id);
   }


    /**
     * Metodo para buscar las empresas con más entregas fallidas.
     * Realiza una consulta SQL que cuenta el número de entregas fallidas por empresa y las ordena de mayor a menor.
     * @return Una lista de empresas ordenadas por el número de entregas fallidas.
     */
    //RF 3: empresas con más entregas fallidas
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
            COUNT(o.id) AS deliveries, -- Total de entregas
            SUM(CASE WHEN o.status = 'FALLIDA' THEN 1 ELSE 0 END) AS failed_deliveries, -- Total de entregas fallidas
            SUM(o.total_price) AS total_sales -- Total de ventas
        FROM
            companies c
                LEFT JOIN orders o ON c.id = o.client_id -- Relación entre empresas y pedidos
        GROUP BY
            c.id, c.name, c.email, c.phone, c.address, c.rut, c.type
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
                        rs.getInt("deliveries"), // Total de entregas
                        rs.getInt("failed_deliveries"), // Total de entregas fallidas
                        rs.getInt("total_sales") // Total de ventas
                )
        );
    }


    /**
     * Metodo para actualizar los metadatos de las empresas.
     * Este método actualiza el número de entregas, entregas fallidas y total de ventas de cada empresa en la base de datos.
     *
     * @return void
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
     * Metodo para buscar las empresas con más volumen de comida entregada.
     * Realiza una consulta SQL que suma el volumen de comida entregada por empresa y las ordena de mayor a menor.
     * @return Una lista de empresas ordenadas por el volumen de comida entregada.
     */
    public List<Map<String, Object>> getCompaniesByDeliveredFoodVolume() {
        String sql = """
            SELECT 
                c.id AS company_id, 
                c.name AS company_name, 
                c.type AS company_type, 
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
                c.id, c.name, c.type
            ORDER BY 
                total_food_delivered DESC;
        """;

        return jdbcTemplate.queryForList(sql);
    }


}
