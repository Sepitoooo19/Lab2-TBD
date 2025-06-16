package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.EmergencyReportEntity;
import bdavanzadas.lab1.services.EmergencyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar operaciones relacionadas con reportes de emergencia
 */
@RestController
@RequestMapping("/emergencies")
public class EmergencyReportController {


    /**
     * Servicio de reportes de emergencia.
     * Este servicio se utiliza para interactuar con la base de datos de reportes de emergencia.
     */
    private final EmergencyReportService emergencyReportService;

    /**
     * Constructor del controlador que inyecta el servicio de reportes de emergencia
     * @param emergencyReportService Servicio de reportes de emergencia
     */
    @Autowired
    public EmergencyReportController(EmergencyReportService emergencyReportService) {
        this.emergencyReportService = emergencyReportService;
    }

    /**
     * Obtiene todos los reportes de emergencia
     * @return Lista de emergencias y código 200 OK
     */
    @GetMapping
    public ResponseEntity<List<EmergencyReportEntity>> getAllEmergencyReports() {
        List<EmergencyReportEntity> reports = emergencyReportService.getAllEmergencyReports();
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    /**
     * Crea un nuevo reporte de emergencia
     * @param emergencyReport Datos del reporte
     * @return Reporte creado y código 201 Created, o error 400 Bad Request
     */
    @PostMapping
    public ResponseEntity<?> createEmergencyReport(@RequestBody EmergencyReportEntity emergencyReport) {
        try {
            emergencyReportService.reportEmergency(emergencyReport);
            return new ResponseEntity<>(emergencyReport, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene un reporte de emergencia por ID
     * @param id ID del reporte
     * @return Reporte y código 200 OK, o 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyReportEntity> getEmergencyReportById(@PathVariable int id) {
        try {
            EmergencyReportEntity report = emergencyReportService.getEmergencyReportById(id);
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene reportes de emergencia por ID de pedido
     * @param orderId ID del pedido
     * @return Lista de reportes y código 200 OK
     */
    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<List<EmergencyReportEntity>> getEmergencyReportsByOrder(@PathVariable int orderId) {
        List<EmergencyReportEntity> reports = emergencyReportService.getEmergencyReportsByOrder(orderId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    /**
     * Obtiene reportes de emergencia por ID de repartidor
     * @param dealerId ID del repartidor
     * @return Lista de reportes y código 200 OK
     */
    @GetMapping("/by-dealer/{dealerId}")
    public ResponseEntity<List<EmergencyReportEntity>> getEmergencyReportsByDealer(@PathVariable int dealerId) {
        List<EmergencyReportEntity> reports = emergencyReportService.getEmergencyReportsByDealer(dealerId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    /**
     * Elimina un reporte de emergencia
     * @param id ID del reporte a eliminar
     * @return Código 204 No Content si se eliminó, o 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergencyReport(@PathVariable int id) {
        try {
            emergencyReportService.deleteEmergencyReport(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}