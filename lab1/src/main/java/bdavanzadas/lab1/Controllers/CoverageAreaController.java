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

@RestController
@RequestMapping("/coverage-areas")
public class CoverageAreaController {

    @Autowired
    private CoverageAreaService coverageAreaService;

    @GetMapping
    public List<CoverageAreaEntity> getAllCoverageAreas() {
        return coverageAreaService.getAllCoverageAreas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoverageAreaEntity> getCoverageAreaById(@PathVariable int id) {
        CoverageAreaEntity coverageArea = coverageAreaService.getCoverageAreaById(id);
        return ResponseEntity.ok(coverageArea);
    }

    @PostMapping
    public ResponseEntity<CoverageAreaEntity> createCoverageArea(@RequestBody CoverageAreaEntity coverageArea) {
        coverageAreaService.createCoverageArea(coverageArea);
        return ResponseEntity.ok(coverageArea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoverageAreaEntity> updateCoverageArea(@PathVariable int id, @RequestBody CoverageAreaEntity coverageArea) {
        coverageArea.setId(id);
        coverageAreaService.updateCoverageArea(coverageArea);
        return ResponseEntity.ok(coverageArea);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoverageArea(@PathVariable int id) {
        coverageAreaService.deleteCoverageArea(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/check/{companyId}/{clientId}")
    public ResponseEntity<Map<String, Boolean>> checkCoverage(
            @PathVariable int companyId,
            @PathVariable int clientId) {

        boolean isCovered = coverageAreaService.checkClientCoverage(clientId, companyId);
        return ResponseEntity.ok(Collections.singletonMap("isCovered", isCovered));
    }

    /**
     * Verificación detallada con manejo de errores básico
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
     * @param clientId ID del cliente a consultar
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