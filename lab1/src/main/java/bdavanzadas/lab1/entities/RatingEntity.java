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
 *  La clase RatingEntity representa la entidad de calificación en la base de datos.
 *  Esta clase contiene información básica sobre la calificación, como el ID, la calificación, el comentario, la fecha, el ID del cliente, el ID del distribuidor y el ID del pedido.
 *
 */
public class RatingEntity {
    private int id;
    private int rating;
    private String comment;
    private Date date;
    private int clientId;
    private int dealerId;
    private int orderId;
}
