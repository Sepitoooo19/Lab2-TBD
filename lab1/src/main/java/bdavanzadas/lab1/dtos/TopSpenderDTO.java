package bdavanzadas.lab1.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

/**
 *
 *  La clase TopSpenderDTO representa un objeto de transferencia de datos (DTO) que contiene información sobre el cliente que más ha gastado.
 *  Esta clase incluye detalles como el ID del cliente, su nombre, RUT, correo electrónico, teléfono, dirección y el total gastado.
 *
 */
public class TopSpenderDTO {
    private int id;
    private String name;
    private String rut;
    private String email;
    private String phone;
    private String address;
    private double totalSpent;
}
