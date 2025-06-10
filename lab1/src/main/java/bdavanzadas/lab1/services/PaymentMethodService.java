package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.PaymentMethodEntity;
import bdavanzadas.lab1.repositories.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 * La clase PaymentMethodService representa el servicio de métodos de pago en la aplicación.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar métodos de pago en la base de datos.
 *
 */
@Service
public class PaymentMethodService {

    /**
     * Repositorio de métodos de pago.
     * Este repositorio se utiliza para interactuar con la base de datos de métodos de pago.
     */
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    /**
     * Metodo para obtener todos los métodos de pago de la base de datos.
     * @return Una lista de métodos de pago.
     *
     * */
    @Transactional(readOnly = true)
    public List<PaymentMethodEntity> getallPaymentMethods() {
        return paymentMethodRepository.findAll();
    }


    /**
     * Metodo para buscar un método de pago por su id.
     * @param "id" El id del método de pago a buscar.
     * @return El método de pago encontrado.
     *
     */
    @Transactional(readOnly = true)
    public PaymentMethodEntity getPaymentMethodById(int id) {
        return paymentMethodRepository.findById(id);
    }

    /**
     * Metodo para buscar un método de pago por su tipo.
     * @param "type" El tipo del método de pago a buscar.
     * @return El método de pago encontrado.
     *
     */
    @Transactional(readOnly = true)
    public PaymentMethodEntity getPaymentMethodByType(String type) {
        return paymentMethodRepository.findByType(type);
    }



    /**
     * Metodo para guardar un método de pago en la base de datos.
     * @param "paymentMethodEntity" El método de pago a guardar.
     * @return void
     *
     */
    @Transactional
    public void savePaymentMethod(PaymentMethodEntity paymentMethodEntity) {
        paymentMethodRepository.save(paymentMethodEntity);
    }

    /**
     * Metodo para eliminar un método de pago de la base de datos en base a su id.
     * @param "id" El id del método de pago a eliminar.
     * @return void
     *
     */
    @Transactional
    public void deletePaymentMethod(int id) {
        paymentMethodRepository.deleteById(id);
    }

    /**
     * Metodo para actualizar un método de pago en la base de datos.
     * @param "paymentMethodEntity" El método de pago a actualizar.
     * @return void
     *
     */
    @Transactional
    public void updatePaymentMethod(PaymentMethodEntity paymentMethodEntity) {
        paymentMethodRepository.update(paymentMethodEntity);
    }


    /**
     * Metodo para buscar los métodos de pago por su id de empresa.
     * @param "companyId" El id de la empresa a buscar.
     * @return Una lista de métodos de pago encontrados.
     *
     */
    //getPaymentMethodsByCompanyId
    public List<PaymentMethodEntity> getPaymentMethodsByCompanyId(int companyId) {
        return paymentMethodRepository.getPaymentMethodsByCompanyId(companyId);
    }


}
