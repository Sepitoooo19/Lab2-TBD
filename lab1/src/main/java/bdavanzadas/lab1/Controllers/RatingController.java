package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.RatingEntity;
import bdavanzadas.lab1.services.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * La clase RatingController maneja las solicitudes relacionadas con las calificaciones.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar calificaciones en la base de datos.
 *
 *
 * */
@RestController
@RequestMapping("/ratings")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class RatingController {


    /**
     *
     * Servicio de calificaciones.
     * Este servicio se utiliza para interactuar con la base de datos de calificaciones.
     *
     * */
    private final RatingService ratingService;


    /**
     *
     * Constructor de la clase RatingController.
     * @param "ratingService" El servicio de calificaciones a utilizar.
     *
     * */
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // -----------------------------------------------------------------
    // CRUD BÁSICO
    // -----------------------------------------------------------------



    /**
     * Endpoint para obtener todas las calificaciones.
     * Este endpoint devuelve una lista de todas las calificaciones en la base de datos.
     * */
    @GetMapping
    public ResponseEntity<List<RatingEntity>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatings());
    }



    /**
     * Endpoint para obtener una calificación por su ID.
     * Este endpoint devuelve una calificación específica basada en su ID.
     * */
    @GetMapping("/{id}")
    public ResponseEntity<RatingEntity> getRatingById(@PathVariable int id) {
        RatingEntity rating = ratingService.getRatingById(id);
        return (rating != null) ? ResponseEntity.ok(rating)
                : ResponseEntity.notFound().build();
    }



    /**
     * Crea una nueva calificación.
     * <p>Usa POST para crear un nuevo recurso.</p>
     */
    @PostMapping
    public ResponseEntity<Void> addRating(@RequestBody RatingEntity rating) {
        ratingService.saveRating(rating);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Actualiza una calificación existente.
     * <p>Usa PUT para mantener la semántica REST.</p>
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRating(@PathVariable int id,
                                             @RequestBody RatingEntity rating) {
        rating.setId(id);
        ratingService.saveRating(rating);
        return ResponseEntity.noContent().build();
    }



    /**
     * Elimina una calificación por su ID.
     * Este endpoint elimina una calificación específica basada en su ID.
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable int id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    // -----------------------------------------------------------------
    // CONSULTAS POR CLIENTE Y DEALER
    // -----------------------------------------------------------------



    /**
     * Calificaciones recibidas por un cliente dado su ID.
     * Este endpoint devuelve una lista de calificaciones asociadas a un cliente específico.
     * */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RatingEntity>> getRatingsByClientId(@PathVariable int clientId) {
        List<RatingEntity> ratings = ratingService.getRatingsByClientId(clientId);
        return (ratings != null && !ratings.isEmpty())
                ? ResponseEntity.ok(ratings)
                : ResponseEntity.notFound().build();
    }



    /**
     * Calificaciones recibidas por un dealer dado su ID.
     * Este endpoint devuelve una lista de calificaciones asociadas a un dealer específico.
     * */
    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<List<RatingEntity>> getRatingsByDealerId(@PathVariable int dealerId) {
        List<RatingEntity> ratings = ratingService.getRatingsByDealerId(dealerId);
        return (ratings != null && !ratings.isEmpty())
                ? ResponseEntity.ok(ratings)
                : ResponseEntity.notFound().build();
    }

    // -----------------------------------------------------------------
    // DEALER AUTENTICADO
    // -----------------------------------------------------------------

    /**
     * Calificaciones asociadas al dealer actualmente autenticado.
     *
     * @return 200 OK con lista de ratings,
     *         404 si no hay registros,
     *         400 en error de validación,
     *         500 en error inesperado.
     */
    @GetMapping("/dealer/ratings")
    public ResponseEntity<?> getRatingsByAuthenticatedDealer() {
        try {
            List<RatingEntity> ratings = ratingService.getRatingsByDealerIdAuthenticated();
            if (ratings != null && !ratings.isEmpty()) {
                return ResponseEntity.ok(ratings);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron calificaciones para el dealer autenticado.");
            }
        } catch (IllegalArgumentException e) { // Error de validación (p.ej., usuario sin dealer)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {                // Error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al obtener las calificaciones.");
        }
    }

}
