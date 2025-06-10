package bdavanzadas.lab1.repositories;

import java.util.List;
import java.util.Map;

import bdavanzadas.lab1.entities.CompanyEntity;


/**
 * La interfaz CompanyRepositoryInt define los métodos para interactuar con la base de datos de compañías.
 * Esta interfaz contiene métodos para guardar, actualizar, eliminar y buscar compañías en la base de datos.
 *
 */
public interface CompanyRepositoryInt {

    /**
     * Metodo para guardar una compañia en la base de datos.
     * @param "company" La compañia a guardar.
     *
     */
    void save(CompanyEntity company);

    /**
     * Metodo para actualizar una compañia en la base de datos.
     * @param "company" La compañia a actualizar.
     *
     */
    void update(CompanyEntity company);

    /**
     * Metodo para eliminar una compañia de la base de datos.
     * @param "id" El id de la compañia a eliminar.
     *
     */
    void delete(int id);

    /**
     * Metodo para buscar una compañia por su id.
     * @param "id" El id de la compañia a buscar.
     * @return La compañia encontrada.
     *
     */
    CompanyEntity findbyid(int id);

    /**
     * Metodo para encontrar todas las compañias en la base de datos.
     * @return Una lista de compañias.
     *
     *
     */
    List<CompanyEntity> findAll();

    /**
     * Metodo para buscar las empresas con más entregas fallidas.
     * Realiza una consulta SQL que cuenta el número de entregas fallidas por empresa y las ordena de mayor a menor.
     * @return Una lista de empresas ordenadas por el número de entregas fallidas.
     */
    List<CompanyEntity> getCompaniesWithMostFailedDeliveries();


    /**
     * Metodo para actualizar los metadatos de las empresas.
     * Este método actualiza el número de entregas, entregas fallidas y total de ventas de cada empresa en la base de datos.
     *
     * @return void
     */
    void updateCompanyMetrics();

    /**
     * Metodo para buscar las empresas con más volumen de comida entregada.
     * Realiza una consulta SQL que suma el volumen de comida entregada por empresa y las ordena de mayor a menor.
     * @return Una lista de empresas ordenadas por el volumen de comida entregada.
     */
    List<Map<String, Object>> getCompaniesByDeliveredFoodVolume();

}
