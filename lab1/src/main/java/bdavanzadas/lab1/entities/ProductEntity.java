package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 *
 *  La clase ProductEntity representa la entidad de producto en la base de datos.
 *  Esta clase contiene información básica sobre el producto, como su nombre, stock, precio, categoría y ID de la empresa.
 *
 */
public class ProductEntity {
    private int id;
    private String name;
    private int stock;
    private float price;
    private String category;
    private int companyId;
}
