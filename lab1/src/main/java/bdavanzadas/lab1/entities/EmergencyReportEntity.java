package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * La clase EmergencyReportEntity representa reportes de emergencias durante entregas.
 * Registra la ubicación exacta donde ocurrió la emergencia, relacionándola con un pedido
 * y el repartidor involucrado.
 */
public class EmergencyReportEntity {
    private int id;                 // ID único del reporte de emergencia
    private int orderId;           // ID del pedido relacionado
    private int dealerId;          // ID del repartidor involucrado
    private String ubication;      // Ubicación en formato WKT (Well-Known Text)
}