package bdavanzadas.lab1.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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