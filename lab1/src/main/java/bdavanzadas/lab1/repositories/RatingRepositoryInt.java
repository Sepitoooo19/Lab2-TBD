package bdavanzadas.lab1.repositories;


import java.util.List;
import bdavanzadas.lab1.entities.RatingEntity;


/**
 * La interfaz RatingRepositoryInt define los métodos para interactuar con la base de datos de ratings.
 * Esta interfaz contiene métodos para guardar, actualizar, eliminar y buscar ratings en la base de datos.
 *
 */
public interface RatingRepositoryInt {

    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    List<RatingEntity> findAll();
    /**
     * Metodo para guardar un rating en la base de datos.
     * @param "rating" El rating a guardar.
     * @return void
     *
     */
    void save(RatingEntity rating);

    /**
     * Metodo para actualizar un rating en la base de datos.
     * @param "rating" El rating que se actualizará
     * @return void
     *
     */
    void update(RatingEntity rating);

    /**
     * Metodo para eliminar un rating de la base de datos.
     * @param "id" El id del rating a eliminar.
     * @return void
     *
     */
    void delete(int id);

    /**
     * Metodo para buscar un rating por su id.
     * @param "id" El id del rating a buscar.
     * @return El rating encontrado.
     *
     */
    RatingEntity findById(int id);

    /**
     * Metodo para buscar un rating por su clientId.
     * @param "clientId" El order id del rating a buscar.
     * @return El rating encontrado.
     *
     */
    List<RatingEntity> findByClientId(int clientId);

    /**
     * Metodo para buscar un rating por su dealerId.
     * @param "dealerId" El order id del rating a buscar.
     * @return El rating encontrado.
     *
     */
    List<RatingEntity> findByDealerId(int dealerId);
}
