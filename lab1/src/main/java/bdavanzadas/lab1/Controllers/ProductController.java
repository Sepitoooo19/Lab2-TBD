package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.ProductEntity;
import bdavanzadas.lab1.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



/**
 *
 * La clase ProductController maneja las solicitudes relacionadas con los productos.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar productos en la base de datos.
 *
 * */
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    /**
     *
     * Servicio de productos.
     * Este servicio se utiliza para interactuar con la base de datos de productos.
     *
     * */
    @Autowired
    private ProductService service;

    // -----------------------------------------------------------------
    // CRUD BÁSICO
    // -----------------------------------------------------------------



    /**
     * Endpoint para obtener todos los productos.
     * Este endpoint devuelve una lista de todos los productos en la base de datos.
     * */
    @GetMapping("/obtenertodos")
    public List<ProductEntity> getAll() {
        return service.getAllProducts();
    }



    /**
     * Endpoint para obtener todos los productos por stock.
     * Este endpoint devuelve una lista de todos los productos en la base de datos ordenados por stock.
     * */
    @GetMapping("/obtenerporstock")
    public List<ProductEntity> getStock() {
        return service.getProductsByStock();
    }



    /**
     * Endpoint para obtener un producto por su ID.
     * Este endpoint devuelve un producto específico basado en su ID.
     * */
    @GetMapping("/obtenerporid/{id}")
    public ProductEntity getById(@PathVariable int id) {
        return service.getProductById(id);
    }



    /**
     * Endpoint para obtener un producto por su nombre.
     * Este endpoint devuelve un producto específico basado en su nombre.
     * */
    @PostMapping("/crear")
    public void create(@RequestBody ProductEntity p) {
        service.saveProduct(p);
    }


    /**
     * Endpoint para actualizar un producto.
     * Este endpoint actualiza un producto existente en la base de datos.
     * */
    @PostMapping("/update")
    public void update(@RequestBody ProductEntity p) {
        service.updateProduct(p);
    }



    /**
     * Endpoint para eliminar un producto por su ID.
     * Este endpoint elimina un producto específico basado en su ID.
     * */
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.deleteProduct(id);
    }

    // -----------------------------------------------------------------
    // CONSULTAS ESPECÍFICAS
    // -----------------------------------------------------------------



    /**
     * Endpoint para obtener productos por categoría.
     * Este endpoint devuelve una lista de productos específicos basados en su categoría.
     * */
    @GetMapping("/obtenerporcategoria/{category}")
    public List<ProductEntity> getProductsByCategory(@PathVariable String category) {
        return service.getProductsByCategory(category);
    }


    /**
     * Endpoint para obtener todas las categorias
     * Este endpoint devuelve una lista de todas las categorias en la base de datos.
     * */
    @GetMapping("/categorias")
    public List<String> findallCategories() {
        return service.findallCategories();
    }


    /**
     * Endpoint para obtener los productos más vendidos por categoría en el último mes.
     * Este endpoint devuelve una lista de productos más vendidos por categoría en el último mes.
     * */
    @GetMapping("/top-by-category")
    public ResponseEntity<List<Map<String, Object>>> getTopProductsByCategoryForLastMonth() {
        List<Map<String, Object>> topProducts = service.getTopProductsByCategoryForLastMonth();
        return ResponseEntity.ok(topProducts);
    }


    /**
     * Endpoint para obtener productos por el ID de la compañía.
     * Este endpoint devuelve una lista de productos específicos basados en el ID de la compañía.
     * */
    @GetMapping("/company/{id}")
    public List<ProductEntity> getProductsByCompanyId(@PathVariable int id) {
        return service.getProductsByCompanyId(id);
    }

    // -----------------------------------------------------------------
    // UTILIDAD
    // -----------------------------------------------------------------


    /**
     * Endpoint para obtener el ID de la compañía por el ID del producto.
     * Este endpoint devuelve el ID de la compañía específica basada en el ID del producto.
     * */
    @GetMapping("/companyid/{id}")
    public int getCompanyIdByProductId(@PathVariable int id) {
        return service.getCompanyIdByProductId(id);
    }
}
