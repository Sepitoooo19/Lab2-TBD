package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.RatingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;




/**
 *
 *
 * La clase RatingRepository representa el repositorio de ratings en la base de datos.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar ratings en la base de datos.
 * */
@Repository
public class RatingRepository implements RatingRepositoryInt {


    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RatingEntity> findAll() {
        String sql = "SELECT * FROM ratings";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RatingEntity(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("date"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getInt("order_id")

                )
        );
    }


    /**
     * Metodo para guardar un rating en la base de datos.
     * @param "rating" El rating a guardar.
     * @return void
     *
     */
    public void save(RatingEntity rating) {
        String sql = "INSERT INTO ratings (rating, comment, date, client_id, dealer_id, order_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, rating.getRating(), rating.getComment(), rating.getDate(), rating.getClientId(), rating.getDealerId(), rating.getOrderId());
    }


    /**
     * Metodo para actualizar un rating en la base de datos.
     * @param "rating" El rating que se actualizará.
     * @return void
     *
     */
    public void update(RatingEntity rating) {
        String sql = "UPDATE ratings SET rating = ?, comment = ?, date = ?, client_id = ?, dealer_id = ?, order_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, rating.getRating(), rating.getComment(), rating.getDate(), rating.getClientId(), rating.getDealerId(), rating.getId(), rating.getOrderId());
    }


    /**
     * Metodo para eliminar un rating de la base de datos.
     * @param "id" El id del rating a eliminar.
     * @return void
     *
     */
    public void delete(int id) {
        String sql = "DELETE FROM ratings WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Metodo para buscar un rating por su id.
     * @param "id" El id del rating a buscar.
     * @return El rating encontrado.
     *
     */

    public RatingEntity findById(int id) {
        String sql = "SELECT * FROM ratings WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new RatingEntity(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("date"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getInt("order_id")
                )
        );
    }


    /**
     * Metodo para buscar un rating por su clientId.
     * @param "clientId" El order id del rating a buscar.
     * @return El rating encontrado.
     *
     */
    public List<RatingEntity> findByClientId(int clientId) {
        String sql = "SELECT * FROM ratings WHERE client_id = ?";
        return jdbcTemplate.query(sql, new Object[]{clientId}, (rs, rowNum) ->
                new RatingEntity(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("date"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getInt("order_id")
                )
        );
    }


    /**
     * Metodo para buscar un rating por su dealerId.
     * @param "dealerId" El order id del rating a buscar.
     * @return El rating encontrado.
     *
     */
    public List<RatingEntity> findByDealerId(int dealerId) {
        String sql = "SELECT * FROM ratings WHERE dealer_id = ?";
        return jdbcTemplate.query(sql, new Object[]{dealerId}, (rs, rowNum) ->
                new RatingEntity(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("date"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getInt("order_id")
                )
        );
    }
}
