package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.PaymentMethodEntity;
import java.util.List;



/**
 * La interfaz PaymentMethodRepositoryInt define los métodos para interactuar con la base de datos de payment methods.
 * Esta interfaz contiene métodos para guardar, actualizar, eliminar y buscar payment methods en la base de datos.
 *
 */
public interface PaymentMethodRepositoryInt {

    /**
     * Metodo para obtener todos los payment methods de la base de datos.
     * @return Una lista de payment methods.
     *
     */
    List<PaymentMethodEntity> findAll();

    /**
     * Metodo para buscar un payment method por su id.
     * @param "id" El id del payment method a buscar.
     * @return El payment method encontrado.
     *
     */
    PaymentMethodEntity findById(int id);

    /**
     * Metodo para buscar un payment method por su type.
     * @param "type" El type del payment method a buscar.
     * @return El payment method encontrado.
     *
     */
    PaymentMethodEntity findByType(String type);

    /**
     * Metodo para buscar guardar un payment method en la base de datos.
     * @param "p" El payment method a guardar.
     * @return void
     */

    void save(PaymentMethodEntity p);

    /**
     * Metodo para actualizar un payment method en la base de datos.
     * @param "p" El payment method a actualizar.
     * @return void
     *
     */
    void update(PaymentMethodEntity p);

    /**
     * Metodo para eliminar un payment method de la base de datos.
     * @param "id" El id del payment method a eliminar.
     * @return void
     *
     */
    void deleteById(int id);

    /**
     * Metodo para encontrar todos los payment methods asociados a un companyId.
     * @param "companyId" El id de la compañia.
     * @return Una lista de payment methods asociados a la compañia.
     *
     */
    List<PaymentMethodEntity> getPaymentMethodsByCompanyId(int companyId);
}
