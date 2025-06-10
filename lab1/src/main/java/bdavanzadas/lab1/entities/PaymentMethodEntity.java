package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 *
 *  La clase PaymentMethodEntity representa la entidad de método de pago en la base de datos.
 *  Esta clase contiene información básica sobre el método de pago, como su tipo.
 *
 */
public class PaymentMethodEntity {
    int id;
    private String type;

}
