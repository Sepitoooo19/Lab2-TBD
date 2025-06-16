package bdavanzadas.lab1.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

/**
 * La clase DealerWithDistanceDTO representa un objeto de transferencia de datos (DTO) que contiene información sobre un dealer y la distancia desde un punto de referencia.
 * Esta clase se utiliza para transferir datos entre la capa de servicio y la capa de presentación.
 */
public class DealerWithDistanceDTO {

    private int id;
    private String name;
    private Double distanceMeters;

}
