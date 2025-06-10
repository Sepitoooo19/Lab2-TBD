package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 *
 *  La clase ProductRepository representa el repositorio de productos en la base de datos.
 *  Esta clase contiene métodos para guardar, actualizar, eliminar y buscar productos en la base de datos.
 *
 */
@Repository
public class ProductRepository implements ProductRepositoryInt {


    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Metodo para encontrar todos los productos en la base de datos.
     * @return Una lista de productos.
     */
   public List<ProductEntity> findAll() {
        String sql="SELECT * FROM products";
        return jdbcTemplate.query(sql,(rs,rownum)->
                new ProductEntity(
                  rs.getInt("id"),
                  rs.getString("name"),
                  rs.getInt("stock"),
                  rs.getFloat("price"),
                  rs.getString("category"),
                  rs.getInt("company_id")));
    }

    /**
     * Metodo para encontrar un producto por su id.
     * @param "id" El id del producto a buscar.
     * @return El producto encontrado.
     */
   public ProductEntity findbyid(int id){
       String sql="SELECT * FROM products WHERE id= ?";
       return jdbcTemplate.queryForObject(sql,new Object[]{id},(rs,rownum)->
               new ProductEntity(
                   rs.getInt("id"),
                   rs.getString("name"),
                   rs.getInt("stock"),
                   rs.getFloat("price"),
                       rs.getString("category"),
                   rs.getInt("company_id")));
   }

    /**
     * Metodo para guardar un producto en la base de datos.
     * @param "p" El producto a guardar.
     * @return void
     */
    public void save(ProductEntity p){
       String sql="INSERTO INTO products (name,stock,price,category,company_id) VALUES (?,?,?,?,?)";
       jdbcTemplate.update(sql,p.getName(),p.getStock(),p.getPrice(),p.getCompanyId());
    }


    /**
     * Metodo para actualizar un producto en la base de datos.
     * @param "p" El producto a actualizar.
     * @return void
     */
    public void update(ProductEntity p){
       String sql="UPDATE products SET name=?,stock=?,price=?,category=?,company_id=? WHERE id=?";
        jdbcTemplate.update(sql,p.getName(),p.getStock(),p.getPrice(),p.getCategory(),p.getCompanyId(),p.getId());
    }



    /**
     * Metodo para eliminar un producto de la base de datos.
     * @param "id" El id del producto a eliminar.
     * @return void
     */
    public void delete(int id){
       String sql="DELETE FROM products WHERE id=?";
       jdbcTemplate.update(sql,id);
    }


    /**
     * Metodo para buscar un producto por su stock.
     * @return Una lista de productos encontrados.
     */
    //find by stock para mostrar lo disponible
    public List<ProductEntity> findByStock(){
        String sql="SELECT * FROM products WHERE stock > 0";
        return jdbcTemplate.query(sql,(rs,rownum)->
                new ProductEntity(
                  rs.getInt("id"),
                  rs.getString("name"),
                  rs.getInt("stock"),
                  rs.getFloat("price"),
                  rs.getString("category"),
                  rs.getInt("company_id")));
    }



    /**
     * Metodo para buscar un producto por su categoria.
     * @param "c" La categoria del producto a buscar.
     * @return Una lista de productos encontrados.
     */
    public List<ProductEntity> findbyCategory(String c){
       String sql="SELECT * FROM products WHERE category=?";
       return jdbcTemplate.query(sql,new Object[]{c},(rs,rownum)->
               new ProductEntity(
                   rs.getInt("id"),
                   rs.getString("name"),
                   rs.getInt("stock"),
                   rs.getFloat("price"),
                   rs.getString("category"),
                       rs.getInt("company_id")));
    }


    /**
     * Metodo para buscar las categorias de los productos.
     * @return Una lista de categorias.
     */
    public List<String> findAllCategories() {
        String sql = "SELECT DISTINCT category FROM products";
        return jdbcTemplate.queryForList(sql, String.class);
    }



    /**
     * Metodo para buscar los productos mas vendidos por categoria en el ultimo mes.
     * @return Una lista de productos mas vendidos por categoria.
     */
    //RF 02: productos más vendidos por categoría en el último mes
    public List<Map<String, Object>> findTopProductsByCategoryForLastMonth() {
        String sql = """
            SELECT 
                p.category, 
                p.name AS product_name, 
                COUNT(op.product_id) AS product_count
            FROM 
                products p
            JOIN 
                order_products op ON p.id = op.product_id
            JOIN 
                orders o ON o.id = op.order_id
            WHERE 
                o.order_date >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month'
                AND o.order_date < DATE_TRUNC('month', CURRENT_DATE)
            GROUP BY 
                p.category, p.name
            ORDER BY 
                p.category, product_count DESC
        """;

        return jdbcTemplate.queryForList(sql);
    }


    /**
     * Metodo para buscar los productos por su id de compañia.
     * @param "companyId" El id de la compañia a buscar.
     * @return Una lista de productos encontrados.
     */
    public List<ProductEntity> findByCompanyId(int companyId) {
        String sql = "SELECT * FROM products WHERE company_id = ?";
        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) ->
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
     * Metodo para buscar el id de la compañia por su id de producto.
     * @param "productId" El id del producto a buscar.
     * @return El id de la compañia encontrada.
     */
    public int getCompanyIdByProductId(int productId) {
        String sql = "SELECT company_id FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class);
    }


}
