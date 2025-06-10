package bdavanzadas.lab1.services;

import bdavanzadas.lab1.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.repositories.DealerRepository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * La clase DealerService representa el servicio de dealers en la aplicación.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar dealers en la base de datos.
 *
 */
@Service
public class DealerService {

    /**
     * Repositorio de dealers.
     * Este repositorio se utiliza para interactuar con la base de datos de dealers.
     */
    @Autowired
    private DealerRepository dealerRepository;




    /**
     * Servicio de usuarios.
     * Este servicio se utiliza para interactuar con la base de datos de usuarios.
     */
    @Autowired
    private UserService userService;


    /**
     * Metodo para obtener todos los dealers de la base de datos.
     * @return Una lista de dealers.
     */
    @Transactional(readOnly = true)
    public List<DealerEntity> getAllDealers() {
        return dealerRepository.findAll();
    }

    /**
     * Metodo para buscar un dealer por su id.
     * @param "id" El id del dealer a buscar.
     * @return El dealer encontrado.
     */
    @Transactional(readOnly = true)
    public DealerEntity getDealerById(int id) {
        return dealerRepository.findById(id);
    }

    /**
     * Metodo para guardar un dealer en la base de datos.
     * @param "dealer" El dealer a guardar.
     * @return void
     *
     *
     */
    @Transactional
    public void saveDealer(DealerEntity dealer) {
        dealerRepository.save(dealer);
    }

    /**
     * Metodo para actualizar un dealer en la base de datos.
     * @param "dealer" El dealer a actualizar.
     * @return void
     *
     */
    @Transactional
    public void updateDealer(DealerEntity dealer) {
        dealerRepository.update(dealer);
    }

    /**
     * Metodo para eliminar un dealer de la base de datos.
     * @param "id" El id del dealer a eliminar.
     * @return void
     *
     */
    @Transactional
    public void deleteDealer(int id) {
        dealerRepository.delete(id);
    }


    /**
     * Metodo para buscar el nombre de un dealer por su id.
     * @param "dealerId" El id del dealer a buscar.
     * @return El nombre del dealer encontrado.
     */
    public String getDealerNameById(Integer dealerId) {
        if (dealerId == null) {
            return "Sin asignar"; // Si el dealerId es null, devuelve "Sin asignar"
        }
        return dealerRepository.findDealerNameById(dealerId);
    }


    /**
     * Metodo para encontrar los 3 mejores repartidores
     * @return Una lista de los 3 mejores repartidores.
     */
    //RF 05 TOP 3 MEJORES REPARTIDORES
    public List<Map<String, Object>> getTopPerformingDealers() {
        return dealerRepository.getTopPerformingDealers();
    }


    /**
     * Metodo para encontrar el tiempo promedio de entrega por dealer
     * @return Una lista de dealers con su tiempo promedio de entrega.
     */
    //RF 04: TIEMPO PROMEDIO DE ESPERA
    public List<Map<String, Object>> getAverageDeliveryTimeByDealer() {
        return dealerRepository.getAverageDeliveryTimeByDealer();
    }


    /**
     * Metodo para encontrar el tiempo promedio de entrega de un dealer autenticado
     * @return El tiempo promedio de entrega del dealer autenticado.
     */
    @Transactional(readOnly = true)
    public Double getAverageDeliveryTimeByAuthenticatedDealer() {
        // Obtener el ID del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();
        return dealerRepository.getAverageDeliveryTimeByAuthenticatedDealer(userId);
    }

    /**
     * Metodo para encontrar el conteo de entregas de un dealer autenticado
     * @return El conteo de entregas del dealer autenticado.
     */
    @Transactional(readOnly = true)
    public Integer getDeliveryCountByAuthenticatedDealer() {
        // Obtener el ID del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();
        return dealerRepository.getDeliveryCountByAuthenticatedDealer(userId);
    }

    /**
     * Obtiene todos los datos del repartidor autenticado
     * @return Mapa con los datos consolidados del repartidor
     */
    public Map<String, Object> getAuthenticatedDealerData() {
        try {
            Long userIdLong = userService.getAuthenticatedUserId();
            int userId = Math.toIntExact(userIdLong); // Conversión segura

            DealerEntity dealer = dealerRepository.findByUserId(userId);

            if (dealer == null) {
                throw new RuntimeException("No existe un repartidor asociado a este usuario");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("id", dealer.getId());
            data.put("name", dealer.getName());

            // Manejo de valores nulos para las estadísticas
            Double avgWaitTime = dealerRepository.getAverageDeliveryTime(dealer.getId());
            data.put("avgWaitTime", avgWaitTime != null ? avgWaitTime : 0.0);

            Double rating = dealerRepository.getAverageRating(dealer.getId());
            data.put("rating", rating != null ? rating : "Sin calificaciones");

            Integer deliveryCount = dealerRepository.getDeliveryCount(dealer.getId());
            data.put("deliveryCount", deliveryCount != null ? deliveryCount : 0);

            return data;

        } catch (ArithmeticException e) {
            throw new RuntimeException("ID de usuario inválido", e);
        }
    }


}



