package bdavanzadas.lab1.services;

import bdavanzadas.lab1.dtos.CoverageCheckDTO;
import bdavanzadas.lab1.entities.CoverageAreaEntity;
import bdavanzadas.lab1.repositories.CoverageAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoverageAreaService {

    @Autowired
    private CoverageAreaRepository coverageAreaRepository;

    public List<CoverageAreaEntity> getAllCoverageAreas() {
        return coverageAreaRepository.findAll();
    }

    public CoverageAreaEntity getCoverageAreaById(int id) {
        return coverageAreaRepository.findById(id);
    }

    public void createCoverageArea(CoverageAreaEntity coverageArea) {
        coverageAreaRepository.save(coverageArea);
    }

    public void updateCoverageArea(CoverageAreaEntity coverageArea) {
        coverageAreaRepository.update(coverageArea);
    }

    public void deleteCoverageArea(int id) {
        coverageAreaRepository.delete(id);
    }

    /**
     * Verificación simple de cobertura
     */
    public boolean checkClientCoverage(int clientId, int companyId) {
        return coverageAreaRepository.isClientInCoverageArea(clientId, companyId);
    }

    /**
     * Verificación detallada con información adicional
     */
    public CoverageCheckDTO getClientCoverageDetails(int clientId, int companyId) {
        return coverageAreaRepository.getClientCoverageDetails(clientId, companyId);
    }
}