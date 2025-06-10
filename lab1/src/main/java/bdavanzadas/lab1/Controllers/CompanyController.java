package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.CompanyEntity;
import bdavanzadas.lab1.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 *
 * La clase CompanyController maneja las solicitudes relacionadas con las compañías.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar compañías en la base de datos.
 *
 * */
@RestController
@RequestMapping("/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

    /**
     *
     * Servicio de compañías.
     * Este servicio se utiliza para interactuar con la base de datos de compañías.
     *
     * */
    @Autowired
    private CompanyService service;


    /**
     *
     * Endpoint para obtener todas las compañías.
     * Este endpoint devuelve una lista de todas las compañías en la base de datos.
     *
     * */
    @GetMapping
    public ResponseEntity<List<CompanyEntity>> getAllCompanies() {
        List<CompanyEntity> companies = service.getAllCompanies();
        return ResponseEntity.ok(companies);
    }


    /**
     *
     * Endpoint para obtener una compañía por su ID.
     * Este endpoint devuelve una compañía específica basada en su ID.
     *
     * */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyEntity> getCompanyById(@PathVariable int id) {
        System.out.println("Solicitud recibida para obtener la empresa con ID: " + id);
        CompanyEntity company = service.findbyid(id);
        if (company == null) {
            System.out.println("Empresa no encontrada");
            return ResponseEntity.notFound().build();
        }
        System.out.println("Empresa encontrada: " + company.getName());
        return ResponseEntity.ok(company);
    }
    /**
     *
     *
     * Endpoint para crear una nueva compañía.
     * Este endpoint guarda una nueva compañía en la base de datos.
     * */
    @PostMapping("/crear")
    public void create(@RequestBody CompanyEntity c) {
        service.saveCompany(c);
    }


    /**
     *
     * Endpoint para actualizar una compañía existente.
     * Este endpoint actualiza una compañía existente en la base de datos.
     *
     * */
    @PostMapping("/update")
    public void update(@RequestBody CompanyEntity c) {
        service.updateCompany(c);
    }


    /**
     *
     * Endpoint para eliminar una compañía.
     * Este endpoint elimina una compañía específica basada en su ID.
     *
     * */
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.deleteCompany(id);
    }

    /**
     *
     * Endpoint para obtener las compañías con más entregas fallidas.
     * Este endpoint devuelve una lista de compañías ordenadas por el número de entregas fallidas.
     *
     * */

    @GetMapping("/failed-deliveries")
    public ResponseEntity<List<CompanyEntity>> getCompaniesWithMostFailedDeliveries() {
        List<CompanyEntity> companies = service.getCompaniesWithMostFailedDeliveries();
        return ResponseEntity.ok(companies);
    }

    /**
     *
     * Endpoint para actualizar las metricas de la compañia.
     * Este endpoint actualiza las métricas de todas las compañías en la base de datos.
     *
     * */
    //- Actualiza las métricas de todas las compañías
    @PostMapping("/update-metrics")
    public ResponseEntity<Void> updateCompanyMetrics() {
        service.updateCompanyMetrics();
        return ResponseEntity.ok().build();
    }

    /**
     *
     * Endpoint para obtener las compañías ordenadas por volumen de comida entregada.
     * Este endpoint devuelve una lista de compañías ordenadas por el volumen de comida entregada.
     *
     * */
    @GetMapping("/delivered-food-volume")
    public ResponseEntity<List<Map<String, Object>>> getCompaniesByDeliveredFoodVolume() {
        List<Map<String, Object>> companies = service.getCompaniesByDeliveredFoodVolume();
        return ResponseEntity.ok(companies);
    }
}