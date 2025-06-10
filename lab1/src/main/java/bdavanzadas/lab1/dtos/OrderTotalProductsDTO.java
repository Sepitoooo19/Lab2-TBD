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
 *  La clase OrderTotalProductsDTO representa un objeto de transferencia de datos (DTO) que contiene información sobre un pedido y el total de productos en él.
 *  Esta clase se utiliza para transferir datos entre la capa de servicio y la capa de presentación.
 *
 */
public class OrderTotalProductsDTO {
    private int id;
    private Date orderDate;
    private Date deliveryDate;
    private String status;
    private double totalPrice;
    private int totalProducts;
}
