package bdavanzadas.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.RatingEntity;
import bdavanzadas.lab1.repositories.RatingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import bdavanzadas.lab1.repositories.UserRepository;
import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.services.UserService;



import java.util.List;



/**
 *
 *
 * La clase RatingService representa el servicio de calificaciones en la aplicación.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar calificaciones en la base de datos.
 *
 * */
@Service
public class RatingService {


    /**
     * Repositorio de calificaciones.
     * Este repositorio se utiliza para interactuar con la base de datos de calificaciones.
     */
    @Autowired
    private final RatingRepository ratingRepository;

    /**
     * jdbc se usa para ejecutar consultas SQL directamente en la base de datos.
     * Este objeto se utiliza para ejecutar consultas SQL y obtener resultados.
     */
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    /**
     * Servicio de usuarios.
     *
     * Este servicio se utiliza para interactuar con la base de datos de usuarios.
     */
    @Autowired
    private UserService userService;


    /**
     * Constructor de la clase RatingService.
     * @param "ratingRepository" El repositorio de calificaciones a utilizar.
     * @param "jdbcTemplate" El objeto JdbcTemplate a utilizar.
     */
    public RatingService(RatingRepository ratingRepository, JdbcTemplate jdbcTemplate) {
        this.ratingRepository = ratingRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * Metodo para obtener todas las calificaciones de la base de datos.
     * @return Una lista de calificaciones.
     *
     */
    @Transactional(readOnly = true)
    public List<RatingEntity> getAllRatings() {
        return ratingRepository.findAll();
    }


    /**
     * Metodo para guardar una calificación en la base de datos.
     * @param "rating" La calificación a guardar.
     * @return void
     *
     */
    @Transactional
    public void saveRating(RatingEntity rating) {
        // Verifica que el pedido exista
        String orderCheckSql = "SELECT COUNT(*) FROM orders WHERE id = ?";
        Integer orderCount = jdbcTemplate.queryForObject(orderCheckSql, Integer.class, rating.getOrderId());
        if (orderCount == null || orderCount == 0) {
            throw new IllegalArgumentException("El pedido con ID " + rating.getOrderId() + " no existe.");
        }

        // Verifica que el repartidor exista
        String dealerCheckSql = "SELECT COUNT(*) FROM dealers WHERE id = ?";
        Integer dealerCount = jdbcTemplate.queryForObject(dealerCheckSql, Integer.class, rating.getDealerId());
        if (dealerCount == null || dealerCount == 0) {
            throw new IllegalArgumentException("El repartidor con ID " + rating.getDealerId() + " no existe.");
        }

        // Guarda la calificación
        ratingRepository.save(rating);
    }

    /**
     * Metodo para eliminar una calificación en la base de datos.
     * @param "rating" La calificación a eliminar.
     * @return void
     *
     */
    @Transactional
    public void deleteRating(int id) {
        ratingRepository.delete(id);
    }


    /**
     * Metodo para buscar una calificación por su id.
     * @param "id" El id de la calificación a buscar.
     * @return La calificación encontrada.
     *
     */
    @Transactional(readOnly = true)
    public RatingEntity getRatingById(int id) {
        return ratingRepository.findById(id);
    }


    /**
     * Metodo para buscar calificaciones por su clientId.
     * @param "clientId" El id de la orden a buscar.
     * @return Las calificaciones encontradas.
     *
     */
    @Transactional(readOnly = true)
    public List<RatingEntity> getRatingsByClientId(int clientId) {
        return ratingRepository.findByClientId(clientId);
    }


    /**
     * Metodo para buscar calificaciones por su dealerId.
     * @param "dealerId" El id del dealer a buscar.
     * @return Las calificaciones encontradas.
     *
     */
    @Transactional(readOnly = true)
    public List<RatingEntity> getRatingsByDealerId(int dealerId) {
        return ratingRepository.findByDealerId(dealerId);
    }
    //dealer ratings a partir de un dealer autenticado


    /**
     * Metodo para buscar calificaciones de un dealer autenticado.
     * @param "userId" El id del usuario autenticado.
     * @return Las calificaciones encontradas.
     *
     *
     */
    @Transactional(readOnly = true)
    public List<RatingEntity> getRatingsByDealerIdAuthenticated() {
        try {
            // Obtener el userId del usuario autenticado
            Long userId = userService.getAuthenticatedUserId();
            if (userId == null) {
                throw new IllegalArgumentException("El usuario no está autenticado.");
            }

            // Obtener el dealerId a partir del userId
            String sql = "SELECT id FROM dealers WHERE user_id = ?";
            Integer dealerId = jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
            if (dealerId == null) {
                throw new IllegalArgumentException("El dealer no existe para el usuario autenticado.");
            }

            // Obtener las calificaciones a partir del dealerId
            return ratingRepository.findByDealerId(dealerId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("No se encontró un dealer asociado al usuario autenticado.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las calificaciones del dealer autenticado.", e);
        }
    }
}
