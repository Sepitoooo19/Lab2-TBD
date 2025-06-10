package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.entities.ProductEntity;
import bdavanzadas.lab1.repositories.OrdersRepository;
import bdavanzadas.lab1.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/**
 *
 *
 * La clase ProductService representa el servicio de productos en la aplicación.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar productos en la base de datos.
 *
 * */
@Service
public class ProductService {

    /**
     * Repositorio de productos.
     * Este repositorio se utiliza para interactuar con la base de datos de productos.
     */
    @Autowired
    private ProductRepository repo;


    /**
     * Metodo para obtener todos los productos de la base de datos.
     * @return Una lista de productos.
     */

    @Transactional(readOnly = true)
    public List<ProductEntity> getAllProducts() {
        return repo.findAll();
    }

    /**
     * Metodo para buscar un producto por su id.
     * @param "id" El id del producto a buscar.
     * @return El producto encontrado.
     *
     */
    @Transactional(readOnly = true)
    public ProductEntity getProductById(int id) {
        return repo.findbyid(id);
    }

    /**
     * Metodo para guardar un producto en la base de datos.
     * @param "product" El producto a guardar.
     * @return void
     */
    @Transactional
    public void saveProduct(ProductEntity product) {
        repo.save(product);
    }

    /**
     * Metodo para actualizar un producto en la base de datos.
     * @param "product" El producto a actualizar.
     * @return void
     */
    @Transactional
    public void updateProduct(ProductEntity product) {
        repo.update(product);
    }

    /**
     * Metodo para eliminar un producto de la base de datos.
     * @param "id" El id del producto a eliminar.
     * @return void
     */
    @Transactional
    public void deleteProduct(int id) {
        repo.delete(id);
    }

    /**
     * Metodo para buscar productos por su stock
     * @return Una lista de productos ordenados por stock.
     *
     *
     */
    @Transactional(readOnly = true)
    public List<ProductEntity> getProductsByStock() {
        return repo.findByStock();
    }

    /**
     * Metodo para buscar productos por su categoria
     * @param "category" La categoria de los productos a buscar.
     * @return Una lista de productos encontrados.
     *
     */
    @Transactional
    public List<ProductEntity> getProductsByCategory(String category) {return repo.findbyCategory(category);}

    /**
     * Metodo para buscar encontrar todas las categorias
     * @return Una lista de categorias.
     *
     */
    @Transactional
    public List<String> findallCategories(){
        return repo.findAllCategories();
    }


    /**
     * Metodo para encontrar los productos más pedidos en el mes por categoría.
     * @return Una lista de productos con más pedidos en el mes.
     */
    //RF 02: obtener los productos con mas pedidos en el mes segun categoria
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTopProductsByCategoryForLastMonth() {
        return repo.findTopProductsByCategoryForLastMonth();
    }

    /**
     * Metodo para encontrar los productos por su id de empresa.
     * @param "companyId" El id de la empresa a buscar.
     * @return Una lista de productos encontrados.
     */
    //findByCompanyId en repository
    @Transactional(readOnly = true)
    public List<ProductEntity> getProductsByCompanyId(int companyId) {
        return repo.findByCompanyId(companyId);
    }



    /**
     * Metodo para buscar la compañia por su id de producto.
     * @param "productId" El id del producto a buscar.
     * @return El id de la compañia encontrada.
     */
    //GetCompanyIdByProductId
    @Transactional(readOnly = true)
    public int getCompanyIdByProductId(int productId) {
        return repo.getCompanyIdByProductId(productId);
    }





}
