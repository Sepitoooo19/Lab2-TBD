package bdavanzadas.lab1.repositories;


import bdavanzadas.lab1.entities.ProductEntity;
import java.util.List;
import java.util.Map;


/**
 * La interfaz ProductRepositoryInt define los métodos para interactuar con la base de datos de productos.
 * Esta interfaz contiene métodos para guardar, actualizar, eliminar y buscar productos en la base de datos.
 *
 */
public interface ProductRepositoryInt {

    /**
     * Metodo para encontrar todos los productos en la base de datos.
     * @return Una lista de productos.
     */
    List<ProductEntity> findAll();

    /**
     * Metodo para encontrar un producto por su id.
     * @param "id" El id del producto a buscar.
     * @return El producto encontrado.
     */
    ProductEntity findbyid(int id);

    /**
     * Metodo para guardar un producto en la base de datos.
     * @param "p" El producto a guardar.
     * @return void
     */
    void save(ProductEntity p);

    /**
     * Metodo para actualizar un producto en la base de datos.
     * @param "p" El producto a actualizar.
     * @return void
     */
    void update(ProductEntity p);

    /**
     * Metodo para eliminar un producto de la base de datos.
     * @param "id" El id del producto a eliminar.
     * @return void
     */
    void delete(int id);

    /**
     * Metodo para buscar un producto por su stock.
     * @return Una lista de productos encontrados.
     */
    //find by stock para mostrar lo disponible
    List<ProductEntity> findByStock();

    /**
     * Metodo para buscar un producto por su categoria.
     * @param "c" La categoria del producto a buscar.
     * @return Una lista de productos encontrados.
     */
    List<ProductEntity> findbyCategory(String c);


    /**
     * Metodo para buscar las categorias de los productos.
     * @return Una lista de categorias.
     */
    List<String> findAllCategories();

    /**
     * Metodo para buscar los productos mas vendidos por categoria en el ultimo mes.
     * @return Una lista de productos mas vendidos por categoria.
     */
    //RF 02: productos más vendidos por categoría en el último mes
    List<Map<String, Object>> findTopProductsByCategoryForLastMonth();

    /**
     * Metodo para buscar los productos por su id de compañia.
     * @param "companyId" El id de la compañia a buscar.
     * @return Una lista de productos encontrados.
     */
    List<ProductEntity> findByCompanyId(int companyId);

    /**
     * Metodo para buscar el id de la compañia por su id de producto.
     * @param "productId" El id del producto a buscar.
     * @return El id de la compañia encontrada.
     */
    int getCompanyIdByProductId(int productId);
}
