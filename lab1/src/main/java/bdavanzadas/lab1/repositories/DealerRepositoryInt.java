package bdavanzadas.lab1.repositories;



import java.util.List;
import java.util.Map;

import bdavanzadas.lab1.entities.DealerEntity;



/**
 * La interfaz DealerRepositoryInt define los métodos para interactuar con la base de datos de dealers.
 * Esta interfaz contiene métodos para guardar, actualizar, eliminar y buscar dealers en la base de datos.
 *
 */
public interface DealerRepositoryInt {

    /**
     * Metodo para buscar todos los dealers.
     * @return Una lista de todos los dealers.
     *
     */
    List<DealerEntity> findAll();

    /**
     * Metodo para guardar un dealer en la base de datos.
     * @param "dealer" El dealer a guardar.
     *
     */
    void save(DealerEntity dealer);

    /**
     * Metodo para actualizar un dealer en la base de datos.
     * @param "dealer" El dealer a actualizar.
     * @return void
     *
     */
    void update(DealerEntity dealer);


    /**
     * Metodo para eliminar un dealer de la base de datos.
     * @param "id" El id del dealer a eliminar.
     * @return void
     *
     */
    void delete(int id);

    /**
     * Metodo para buscar un dealer por su id.
     * @param "id" El id del dealer a buscar.
     * @return El dealer encontrado.
     *
     */
    DealerEntity findById(Integer id);

    /**
     * Metodo para obtener el tiempo promedio de entrega por repartidor.
     * Este método realiza una consulta SQL que calcula el tiempo promedio entre la fecha de pedido y la fecha de entrega para cada repartidor.
     * El resultado se agrupa por el ID y nombre del repartidor, y se ordena de menor a mayor tiempo promedio.
     * @return Una lista de mapas, donde cada mapa contiene el ID y nombre del repartidor, y el tiempo promedio de entrega en horas.
     *
     */
    List<Map<String, Object>> getAverageDeliveryTimeByDealer();

    /**
     * Metodo para buscar el nombre de un dealer por su id.
     * @param "dealerId" El id del dealer a buscar.
     * @return El nombre del dealer encontrado o "Sin asignar" si no se encuentra.
     *
     */
    String findDealerNameById(int dealerId);

    /**
     * Metodo para buscar los 3 mejores repartidores.
     * Este método realiza una consulta SQL que calcula el puntaje de rendimiento de cada repartidor basado en el número de entregas y la calificación promedio.
     * El puntaje se calcula como 70% del número de entregas y 30% de la calificación promedio.
     * El resultado se agrupa por el ID y nombre del repartidor, y se ordena de mayor a menor puntaje.
     * @return Una lista de mapas, donde cada mapa contiene el ID y nombre del repartidor, el total de entregas, la calificación promedio y el puntaje de rendimiento.
     *
     */
    //RF 05: tres mejores repartidores
    List<Map<String, Object>> getTopPerformingDealers();

    /**
     * Metodo para obtener el tiempo promedio de entrega por repartidor autenticado.
     * Este método realiza una consulta SQL que calcula el tiempo promedio entre la fecha de pedido y la fecha de entrega para el repartidor autenticado.
     * El resultado se agrupa por el ID y nombre del repartidor, y se ordena de menor a mayor tiempo promedio.
     * @param "userId" El id del usuario autenticado.
     * @return El tiempo promedio de entrega en horas.
     *
     */
    Double getAverageDeliveryTimeByAuthenticatedDealer(Long userId);

    /**
     * Metodo para contar el número de entregas realizadas por el repartidor autenticado.
     * Este método realiza una consulta SQL que cuenta el número de entregas realizadas por el repartidor autenticado.
     * @param "userId" El id del usuario autenticado.
     * @return El número de entregas realizadas.
     *
     */
    Integer getDeliveryCountByAuthenticatedDealer(Long userId);


}
