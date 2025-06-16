package bdavanzadas.lab1.services;

import bdavanzadas.lab1.dtos.CoverageCheckDTO;
import bdavanzadas.lab1.entities.CoverageAreaEntity;
import bdavanzadas.lab1.repositories.CoverageAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * Verificación básica de cobertura sin validaciones
     * @param clientId ID del cliente
     * @param companyId ID de la empresa
     * @return resultado directo del repository
     */
    public boolean checkClientCoverage(int clientId, int companyId) {
        return coverageAreaRepository.isClientInCoverageArea(clientId, companyId);
    }

    /**
     * Verificación detallada
     * @return DTO o null si hay algún problema
     */
    @Transactional(readOnly = true)
    public CoverageCheckDTO getClientCoverageDetails(int clientId, int companyId) {
        if (clientId <= 0 || companyId <= 0) {
            return null;
        }


        return coverageAreaRepository.getClientCoverageDetails(clientId, companyId);
    }

    /**
     * Obtiene todas las coberturas donde se encuentra un cliente
     * @param clientId ID del cliente a verificar
     * @return Lista de CoverageCheckDTO con las coberturas encontradas
     */
    public List<CoverageCheckDTO> getClientCoverages(int clientId) {
        // Validación básica del ID
        if (clientId <= 0) {
            throw new IllegalArgumentException("El ID de cliente debe ser mayor a cero");
        }

        return coverageAreaRepository.getClientCoverages(clientId);
    }
}