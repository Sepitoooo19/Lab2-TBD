package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 *
 *  La clase CompanyEntity representa la entidad de empresa en la base de datos.
 *  Esta clase contiene información básica sobre la empresa, como su nombre, correo electrónico, teléfono y dirección.
 *  También incluye información adicional como el RUT, tipo de empresa, número de entregas, entregas fallidas y total de ventas.
 *
 */
public class CompanyEntity {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String rut;
    private String type;
    private int deliveries;
    private int failedDeliveries;
    private int totalSales;
    private String ubication;

}
