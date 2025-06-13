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


    /**
     * Verificación simple
     */
    @GetMapping("/check/{companyId}/{clientId}")
    public ResponseEntity<Map<String, Boolean>> checkCoverage(
            @PathVariable int companyId,
            @PathVariable int clientId) {

        boolean isCovered = coverageAreaService.checkClientCoverage(clientId, companyId);
        return ResponseEntity.ok(Collections.singletonMap("isCovered", isCovered));
    }

    /**
     * Verificación detallada
     */
    @GetMapping("/details/{companyId}/{clientId}")
    public ResponseEntity<CoverageCheckDTO> getCoverageDetails(
            @PathVariable int companyId,
            @PathVariable int clientId) {

        return ResponseEntity.ok(
                coverageAreaService.getClientCoverageDetails(clientId, companyId)
        );
    }
}