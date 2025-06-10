package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 *
 *  La clase ClientEntity representa la entidad de cliente en la base de datos.
 *  Esta clase contiene información básica sobre el cliente, como su nombre, RUT, correo electrónico, teléfono y dirección.
 *  También incluye una relación con la entidad UserEntity para gestionar la autenticación y autorización del cliente.
 *
 */
public class ClientEntity {
    private int id;
    private String name;
    private String rut;
    private String email;
    private String phone;
    private String address;
    private int userId; // Relación con UserEntity
    private String ubication; //WKT
}