package bdavanzadas.lab1.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor


/**
 *
 * La clase CoverageCheckDTO representa un objeto de transferencia de datos (DTO) que contiene información sobre la cobertura de un cliente.
 * * Esta clase incluye detalles como el ID del cliente, su nombre, el ID de la empresa, el nombre de la empresa, el ID de la cobertura (que puede ser null si no hay cobertura),
 *
 * */
public class CoverageCheckDTO {
    private int clientId;
    private String clientName;
    private int companyId;
    private String companyName;
    private Integer coverageId;  // Puede ser null si no hay cobertura
    private String coverageName; // Puede ser null
    private boolean isCovered;
    private double distanceMeters;
}