package bdavanzadas.lab1.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NearestDeliveryPointDTO {
    private int clientId;
    private String clientName;
    private String clientAddress;
    private String clientLocation; // WKT del punto
    private String companyName;
    private double distanceMeters; // Distancia en metros

}