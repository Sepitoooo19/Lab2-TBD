package bdavanzadas.lab1.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerWithDistanceDTO {

    private int id;
    private String name;
    private Double distanceMeters;

}
