package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.EmergencyReportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * La clase EmergencyReportRepository maneja las operaciones CRUD para reportes de emergencia.
 * Proporciona métodos para interactuar con la tabla emergency_reports en la base de datos.
 */
@Repository
public class EmergencyReportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Obtiene todos los reportes de emergencia de la base de datos.
     * @return Lista de EmergencyReportEntity
     */
    public List<EmergencyReportEntity> findAll() {
        String sql = "SELECT * FROM emergency_report";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new EmergencyReportEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("dealer_id"),
                        rs.getString("ubication")
                )
        );
    }

    /**
     * Guarda un nuevo reporte de emergencia en la base de datos.
     * @param emergencyReport El reporte a guardar
     */
    public void save(EmergencyReportEntity emergencyReport) {
        String sql = "INSERT INTO emergency_report (order_id, dealer_id, ubication) VALUES (?, ?, ST_GeomFromText(?))";
        jdbcTemplate.update(sql,
                emergencyReport.getOrderId(),
                emergencyReport.getDealerId(),
                emergencyReport.getUbication());
    }

    /**
     * Actualiza un reporte de emergencia existente.
     * @param emergencyReport El reporte con datos actualizados
     */
    public void update(EmergencyReportEntity emergencyReport) {
        String sql = "UPDATE emergency_report SET order_id = ?, dealer_id = ?, ubication = ST_GeomFromText(?) WHERE id = ?";
        jdbcTemplate.update(sql,
                emergencyReport.getOrderId(),
                emergencyReport.getDealerId(),
                emergencyReport.getUbication(),
                emergencyReport.getId());
    }

    /**
     * Elimina un reporte de emergencia por su ID.
     * @param id ID del reporte a eliminar
     */
    public void delete(int id) {
        String sql = "DELETE FROM emergency_report WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Busca un reporte de emergencia por su ID.
     * @param id ID del reporte a buscar
     * @return EmergencyReportEntity encontrado
     */
    public EmergencyReportEntity findById(int id) {
        String sql = "SELECT id, order_id, dealer_id, ST_AsText(ubication) as ubication FROM emergency_report WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new EmergencyReportEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("dealer_id"),
                        rs.getString("ubication")
                ));
    }

    /**
     * Busca reportes de emergencia por ID de pedido.
     * @param orderId ID del pedido asociado
     * @return Lista de EmergencyReportEntity
     */
    public List<EmergencyReportEntity> findByOrderId(int orderId) {
        String sql = "SELECT id, order_id, dealer_id, ST_AsText(ubication) as ubication FROM emergency_report WHERE order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) ->
                new EmergencyReportEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("dealer_id"),
                        rs.getString("ubication")
                ));
    }

    /**
     * Busca reportes de emergencia por ID de repartidor.
     * @param dealerId ID del repartidor asociado
     * @return Lista de EmergencyReportEntity
     */
    public List<EmergencyReportEntity> findByDealerId(int dealerId) {
        String sql = "SELECT id, order_id, dealer_id, ST_AsText(ubication) as ubication FROM emergency_report WHERE dealer_id = ?";
        return jdbcTemplate.query(sql, new Object[]{dealerId}, (rs, rowNum) ->
                new EmergencyReportEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("dealer_id"),
                        rs.getString("ubication")
                ));
    }
}