package bdavanzadas.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.EmergencyReportEntity;
import bdavanzadas.lab1.repositories.EmergencyReportRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

/**
 * La clase EmergencyReportService maneja la lógica de negocio para reportes de emergencia.
 * Proporciona métodos para gestionar emergencias reportadas durante las entregas.
 */
@Service
public class EmergencyReportService {

    private final EmergencyReportRepository emergencyReportRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmergencyReportService(EmergencyReportRepository emergencyReportRepository,
                                  JdbcTemplate jdbcTemplate) {
        this.emergencyReportRepository = emergencyReportRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Obtiene todos los reportes de emergencia
     * @return Lista de EmergencyReportEntity
     */
    @Transactional(readOnly = true)
    public List<EmergencyReportEntity> getAllEmergencyReports() {
        return emergencyReportRepository.findAll();
    }

    /**
     * Registra un nuevo reporte de emergencia
     * @param emergencyReport Datos del reporte
     * @throws IllegalArgumentException Si el pedido o repartidor no existen
     */
    @Transactional
    public void reportEmergency(EmergencyReportEntity emergencyReport) {
        // Validar que el pedido exista
        String orderCheckSql = "SELECT COUNT(*) FROM orders WHERE id = ?";
        Integer orderCount = jdbcTemplate.queryForObject(orderCheckSql, Integer.class, emergencyReport.getOrderId());
        if (orderCount == null || orderCount == 0) {
            throw new IllegalArgumentException("El pedido con ID " + emergencyReport.getOrderId() + " no existe.");
        }

        // Validar que el repartidor exista
        String dealerCheckSql = "SELECT COUNT(*) FROM dealers WHERE id = ?";
        Integer dealerCount = jdbcTemplate.queryForObject(dealerCheckSql, Integer.class, emergencyReport.getDealerId());
        if (dealerCount == null || dealerCount == 0) {
            throw new IllegalArgumentException("El repartidor con ID " + emergencyReport.getDealerId() + " no existe.");
        }

        // Validar formato WKT de la ubicación
        if (!isValidWktPoint(emergencyReport.getUbication())) {
            throw new IllegalArgumentException("Formato de ubicación inválido. Debe ser POINT(longitud latitud)");
        }

        emergencyReportRepository.save(emergencyReport);
    }

    /**
     * Obtiene un reporte de emergencia por su ID
     * @param id ID del reporte
     * @return EmergencyReportEntity encontrado
     * @throws EmptyResultDataAccessException Si no se encuentra el reporte
     */
    @Transactional(readOnly = true)
    public EmergencyReportEntity getEmergencyReportById(int id) {
        return emergencyReportRepository.findById(id);
    }

    /**
     * Obtiene reportes de emergencia por ID de pedido
     * @param orderId ID del pedido
     * @return Lista de EmergencyReportEntity
     */
    @Transactional(readOnly = true)
    public List<EmergencyReportEntity> getEmergencyReportsByOrder(int orderId) {
        return emergencyReportRepository.findByOrderId(orderId);
    }

    /**
     * Obtiene reportes de emergencia por ID de repartidor
     * @param dealerId ID del repartidor
     * @return Lista de EmergencyReportEntity
     */
    @Transactional(readOnly = true)
    public List<EmergencyReportEntity> getEmergencyReportsByDealer(int dealerId) {
        return emergencyReportRepository.findByDealerId(dealerId);
    }

    /**
     * Elimina un reporte de emergencia
     * @param id ID del reporte a eliminar
     */
    @Transactional
    public void deleteEmergencyReport(int id) {
        emergencyReportRepository.delete(id);
    }

    /**
     * Valida el formato WKT para un punto
     * @param wkt Cadena en formato WKT
     * @return true si es válido, false en caso contrario
     */
    private boolean isValidWktPoint(String wkt) {
        if (wkt == null || wkt.isEmpty()) {
            return false;
        }
        // Validación simple del formato POINT
        return wkt.matches("^POINT\\([-+]?\\d+\\.?\\d*\\s[-+]?\\d+\\.?\\d*\\)$");
    }
}