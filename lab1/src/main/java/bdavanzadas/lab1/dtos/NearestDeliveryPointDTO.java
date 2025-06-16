package bdavanzadas.lab1.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

/**
 *
 *  La clase NearestDeliveryPointDTO representa un objeto de transferencia de datos (DTO) que contiene información sobre el punto de entrega más cercano a un cliente.
 *  Esta clase incluye detalles como el ID del cliente, su nombre, dirección, ubicación (en formato WKT), nombre de la empresa y la distancia al punto de entrega en metros.
 *
 */
public class NearestDeliveryPointDTO {
    private int clientId;
    private String clientName;
    private String clientAddress;
    private String clientLocation; // WKT del punto
    private String companyName;
    private double distanceMeters; // Distancia en metros

}