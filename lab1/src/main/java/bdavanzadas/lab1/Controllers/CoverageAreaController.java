package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.dtos.CoverageCheckDTO;
import bdavanzadas.lab1.entities.CoverageAreaEntity;
import bdavanzadas.lab1.services.CoverageAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;




/**
 *
 * La clase CoverageAreaController maneja las solicitudes relacionadas con las áreas de cobertura.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar áreas de cobertura,
 *
 * */
@RestController
@RequestMapping("/coverage-areas")
public class CoverageAreaController {


    /**
     * Servicio de áreas de cobertura.
     * Este servicio se utiliza para interactuar con la base de datos de áreas de cobertura.
     */
    @Autowired
    private CoverageAreaService coverageAreaService;


    /**
     * Endpoint para obtener todas las áreas de cobertura.
     * Este endpoint devuelve una lista de todas las áreas de cobertura en la base de datos.
     *
     * @return Lista de entidades CoverageAreaEntity
     */
    @GetMapping
    public List<CoverageAreaEntity> getAllCoverageAreas() {
        return coverageAreaService.getAllCoverageAreas();
    }

    /**
     * Endpoint para obtener un área de cobertura por su ID.
     * Este endpoint devuelve un área de cobertura específica basada en su ID.
     *
     * @param "id" ID del área de cobertura
     * @return Entidad CoverageAreaEntity encontrada o NOT FOUND si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<CoverageAreaEntity> getCoverageAreaById(@PathVariable int id) {
        CoverageAreaEntity coverageArea = coverageAreaService.getCoverageAreaById(id);
        return ResponseEntity.ok(coverageArea);
    }


    /**
     * Endpoint para crear un nuevo área de cobertura.
     * Este endpoint recibe un objeto JSON con los datos del nuevo área de cobertura y lo guarda en la base de datos.
     *
     * @param "coverageArea" Entidad CoverageAreaEntity a crear
     * @return Entidad CoverageAreaEntity creada
     */
    @PostMapping
    public ResponseEntity<CoverageAreaEntity> createCoverageArea(@RequestBody CoverageAreaEntity coverageArea) {
        coverageAreaService.createCoverageArea(coverageArea);
        return ResponseEntity.ok(coverageArea);
    }


    /**
     * Endpoint para actualizar un área de cobertura existente.
     * Este endpoint recibe un objeto JSON con los datos del área de cobertura a actualizar y lo guarda en la base de datos.
     *
     * @param "id" ID del área de cobertura a actualizar
     * @param "coverageArea" Entidad CoverageAreaEntity con los nuevos datos
     * @return Entidad CoverageAreaEntity actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<CoverageAreaEntity> updateCoverageArea(@PathVariable int id, @RequestBody CoverageAreaEntity coverageArea) {
        coverageArea.setId(id);
        coverageAreaService.updateCoverageArea(coverageArea);
        return ResponseEntity.ok(coverageArea);
    }


    /**
     * Endpoint para eliminar un área de cobertura por su ID.
     * Este endpoint elimina un área de cobertura específica basada en su ID.
     *
     * @param "id" ID del área de cobertura a eliminar
     * @return Respuesta HTTP sin contenido (204 No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoverageArea(@PathVariable int id) {
        coverageAreaService.deleteCoverageArea(id);
        return ResponseEntity.noContent().build();
    }



    /**
     * Verificación simple de cobertura para un cliente en una compañía específica
     * @param "companyId" ID de la compañía
     * @param "clientId" ID del cliente
     * @return Mapa con el resultado de la verificación
     */
    @GetMapping("/check/{companyId}/{clientId}")
    public ResponseEntity<Map<String, Boolean>> checkCoverage(
            @PathVariable int companyId,
            @PathVariable int clientId) {

        boolean isCovered = coverageAreaService.checkClientCoverage(clientId, companyId);
        return ResponseEntity.ok(Collections.singletonMap("isCovered", isCovered));
    }

    /**
     * Obtiene los detalles de cobertura de un cliente en una compañía específica
     * @param "companyId" ID de la compañía
     * @param "clientId" ID del cliente
     * @return Detalles de cobertura del cliente
     */
    @GetMapping("/details/{companyId}/{clientId}")
    public ResponseEntity<?> getCoverageDetails(
            @PathVariable int companyId,
            @PathVariable int clientId) {

        // Validación básica de parámetros
        if (companyId <= 0 || clientId <= 0) {
            return ResponseEntity.badRequest().body("Los IDs deben ser mayores a cero");
        }

        try {
            CoverageCheckDTO details = coverageAreaService.getClientCoverageDetails(clientId, companyId);

            if (details == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener detalles de cobertura");
        }
    }

    /**
     * Obtiene todas las áreas de cobertura donde se encuentra un cliente
     * @param "clientId" ID del cliente a consultar
     * @return Lista de CoverageCheckDTO con las coberturas encontradas
     */
    @GetMapping("/client-coverage/{clientId}")
    public ResponseEntity<?> getClientCoverages(@PathVariable int clientId) {
        try {
            List<CoverageCheckDTO> coverages = coverageAreaService.getClientCoverages(clientId);

            if (coverages.isEmpty()) {
                return ResponseEntity.ok().body("El cliente no se encuentra en ninguna zona de cobertura");
            }

            return ResponseEntity.ok(coverages);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al consultar las coberturas del cliente");
        }
    }
}