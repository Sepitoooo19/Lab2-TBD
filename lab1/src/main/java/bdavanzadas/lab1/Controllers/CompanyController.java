package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.dtos.NearestDeliveryPointDTO;
import bdavanzadas.lab1.entities.CompanyEntity;
import bdavanzadas.lab1.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    @GetMapping("/nearest/{companyId}")
    public ResponseEntity<?> getNearestDeliveryPoints(@PathVariable int companyId) {
        try {
            List<NearestDeliveryPointDTO> result = service.getTop5NearestDeliveryPoints(companyId);

            if (result == null || result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron puntos de entrega cercanos");
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud");
        }
    }

    /**
     *
     * Endpoint para obtener el punto de entrega más lejano desde cada compañía.
     * Este endpoint devuelve una lista de puntos de entrega más lejanos desde cada compañía.
     *
     * */
    @GetMapping("/reports/farthest-deliveries")
    public ResponseEntity<List<Map<String, Object>>> getFarthestDeliveriesReport() {
        try {
            List<Map<String, Object>> report = service.getFarthestDeliveryPointReport();
            if (report.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Endpoint para obtener el ID de una compañía por su nombre
     * @param "name" Nombre de la compañía a buscar
     * @return ResponseEntity con el ID o código de error
     */
    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> getCompanyIdByName(@PathVariable String name) {
        try {
            Integer companyId = service.getCompanyIdByName(name);
            return ResponseEntity.ok(Collections.singletonMap("id", companyId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al buscar la compañía");
        }
    }

}