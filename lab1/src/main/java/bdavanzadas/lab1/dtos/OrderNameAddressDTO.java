package bdavanzadas.lab1.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

/**
 *
 *  La clase OrderNameAddressDTO representa un objeto de transferencia de datos (DTO) que contiene información sobre un pedido y la dirección del cliente.
 *  Esta clase se utiliza para transferir datos entre la capa de servicio y la capa de presentación.
 *
 */
public class OrderNameAddressDTO {
    private int id;
    private Date orderDate;
    private Date deliveryDate;
    private String status;
    private double totalPrice;
    private int clientId;
    private String nameClient; // Nombre del cliente
    private String address;
}
