package bdavanzadas.lab1.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


/**
 * La clase CoverageAreaEntity representa la entidad de área de cobertura en la base de datos.
 * Esta clase contiene información sobre el nombre del área de cobertura y su representación geográfica.
 */
public class CoverageAreaEntity {

    private int id;
    private String name;
    private String coverageArea;



}
