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


/**
 * La clase CoverageAreaService representa el servicio de áreas de cobertura en la aplicación.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar áreas de cobertura en la base de datos.
 */
@Service
public class CoverageAreaService {

    /**
     * Repositorio de áreas de cobertura.
     * Este repositorio se utiliza para interactuar con la base de datos de áreas de cobertura.
     */
    @Autowired
    private CoverageAreaRepository coverageAreaRepository;

    /**
     * Método para obtener todas las áreas de cobertura de la base de datos.
     * @return Una lista de áreas de cobertura.
     */
    public List<CoverageAreaEntity> getAllCoverageAreas() {
        return coverageAreaRepository.findAll();
    }

    /**
     * Método para buscar un área de cobertura por su ID.
     * @param id El ID del área de cobertura a buscar.
     * @return El área de cobertura encontrada o null si no se encuentra.
     */
    public CoverageAreaEntity getCoverageAreaById(int id) {
        return coverageAreaRepository.findById(id);
    }

    /**
     * Método crear un área de cobertura.
     * @param coverageArea El área de cobertura a crear.
     */
    public void createCoverageArea(CoverageAreaEntity coverageArea) {
        coverageAreaRepository.save(coverageArea);
    }

    /**
     * Método para actualizar un área de cobertura.
     * @param coverageArea El área de cobertura a actualizar.
     */
    public void updateCoverageArea(CoverageAreaEntity coverageArea) {
        coverageAreaRepository.update(coverageArea);
    }

    /**
     * Método para eliminar un área de cobertura por su ID.
     * @param id El ID del área de cobertura a eliminar.
     */
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