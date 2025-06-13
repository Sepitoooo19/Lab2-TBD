package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 *
 *  La clase OrdersEntity representa la entidad de pedidos en la base de datos.
 *  Esta clase contiene información básica sobre el pedido, como la fecha del pedido, la fecha de entrega, el estado, el ID del cliente, el ID del distribuidor y el precio total.
 *
 */
public class OrdersEntity {
    private int id;
    private Date orderDate;
    private Date deliveryDate;
    private String status;
    private int clientId;
    private Integer dealerId;
    private double totalPrice;
    private String estimatedRoute;
}
