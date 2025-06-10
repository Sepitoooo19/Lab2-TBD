package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 *
 *  La clase DealerEntity representa la entidad de concesionario en la base de datos.
 *  Esta clase contiene información básica sobre el concesionario, como su nombre, RUT, correo electrónico, teléfono y vehículo.
 *  También incluye información adicional como la placa del vehículo y una relación con la entidad UserEntity para gestionar la autenticación y autorización del concesionario.
 *
 */
public class DealerEntity {
    private int id;
    private String rut;
    private String name;
    private String phone;
    private String email;
    private String vehicle;
    private String plate;
    private String ubication; //WKT
    private int userId; // Relación con UserEntity
}