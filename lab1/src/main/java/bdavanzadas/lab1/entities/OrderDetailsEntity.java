package bdavanzadas.lab1.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 *
 *  La clase OrderDetailsEntity representa la entidad de detalles de pedido en la base de datos.
 *  Esta clase contiene información básica sobre el pedido, como el ID del pedido, el método de pago, el número total de productos y el precio total.
 *
 */
public class OrderDetailsEntity {
    private int id;
    private int orderId;
    private String paymentMethod;
    private int totalProducts;
    private double price;

}
