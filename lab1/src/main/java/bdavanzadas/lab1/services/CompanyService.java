package bdavanzadas.lab1.services;

import bdavanzadas.lab1.dtos.NearestDeliveryPointDTO;
import bdavanzadas.lab1.entities.CompanyEntity;
import bdavanzadas.lab1.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class CompanyService {

    /**
     * Repositorio de compañías.
     * Este repositorio se utiliza para interactuar con la base de datos de compañías.
     */
    @Autowired
    private CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CompanyEntity findbyid(int id) {
        return companyRepository.findbyid(id);
    }

    @Transactional
    public void saveCompany(CompanyEntity company) {
        // Validar el formato de la ubicación en WKT (Well-Known Text)
        validateUbicacion(company.getUbication());
        companyRepository.save(company);
    }

    @Transactional
    public void updateCompany(CompanyEntity company) {
        // Validar el formato de la ubicación en WKT (Well-Known Text)
        validateUbicacion(company.getUbication());
        companyRepository.update(company);
    }

    @Transactional
    public void deleteCompany(int id) {
        companyRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<CompanyEntity> getCompaniesWithMostFailedDeliveries() {
        return companyRepository.getCompaniesWithMostFailedDeliveries();
    }

    @Transactional
    public void updateCompanyMetrics() {
        companyRepository.updateCompanyMetrics();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCompaniesByDeliveredFoodVolume() {
        return companyRepository.getCompaniesByDeliveredFoodVolume();
    }

    // Patrón para validar WKT (ej: "POINT(-70.123 -33.456)")
    private static final Pattern WKT_PATTERN = Pattern.compile(
            "^POINT\\(-?\\d+\\.?\\d* -?\\d+\\.?\\d*\\)$"
    );

    // --- Método de validación adicional ---
    private void validateUbicacion(String ubicacion) {
        if (ubicacion == null || !WKT_PATTERN.matcher(ubicacion).matches()) {
            throw new IllegalArgumentException(
                    "Formato WKT inválido. Debe ser 'POINT(longitud latitud)'."
            );
        }
    }

    public List<NearestDeliveryPointDTO> getTop5NearestDeliveryPoints(int companyId) {
        // Validación básica del ID
        if (companyId <= 0) {
            throw new IllegalArgumentException("El ID de compañía debe ser positivo");
        }

        return companyRepository.findNearestDeliveryPoints(companyId, 5);
    }

    // rf4-identificar el punto de entrega más lejano desde cada compañía
    /**
     * Genera un reporte que muestra el punto de entrega más lejano para cada empresa
     * basado en pedidos urgentes o pendientes.
     * @return Una lista con la información del reporte.
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getFarthestDeliveryPointReport() {
        return companyRepository.findFarthestDeliveryPointForEachCompany();
    }

    /**
     * Obtiene el ID de una compañía por su nombre
     * @param name Nombre de la compañía a buscar
     * @return ID de la compañía
     */
    public Integer getCompanyIdByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        Integer id = companyRepository.findIdByName(name);


        return id;
    }


}