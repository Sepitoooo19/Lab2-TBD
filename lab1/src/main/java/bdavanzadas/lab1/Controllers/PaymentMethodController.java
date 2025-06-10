package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.PaymentMethodEntity;
import bdavanzadas.lab1.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * La clase PaymentMethodController maneja las solicitudes relacionadas con los métodos de pago.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar métodos de pago en la base de datos.
 *
 *
 * */
@RestController
@RequestMapping("/paymentmethod")
@CrossOrigin(origins = "*")
public class PaymentMethodController {

    /**
     *
     * Servicio de métodos de pago.
     * Este servicio se utiliza para interactuar con la base de datos de métodos de pago.
     *
     * */
    @Autowired
    private PaymentMethodService service;

    // -----------------------------------------------------------------
    // ENDPOINTS CRUD
    // -----------------------------------------------------------------





    /**
     * Endpoint para obtener todos los métodos de pago.
     * Este endpoint devuelve una lista de todos los métodos de pago en la base de datos.
     * */
    @GetMapping("/obtenertodos")
    public List<PaymentMethodEntity> getAll() {
        return service.getallPaymentMethods();
    }


    /**
     * Endpoint para obtener un método de pago por su ID.
     * Este endpoint devuelve un método de pago específico basado en su ID.
     * */
    @GetMapping("/obtenerbyid/{id}")
    public PaymentMethodEntity getById(@PathVariable int id) {
        return service.getPaymentMethodById(id);
    }


    /**
     * Endpoint para obtener un método de pago por su tipo.
     * Este endpoint devuelve un método de pago específico basado en su tipo.
     * */
    @GetMapping("/obtenerbytype/{type}")
    public PaymentMethodEntity getByType(@PathVariable String type) {
        return service.getPaymentMethodByType(type);
    }


    /**
     * Endpoint para crear un nuevo método de pago.
     * Este endpoint guarda un nuevo método de pago en la base de datos.
     * */
    @PostMapping("/crear")
    public ResponseEntity<Void> create(@RequestBody PaymentMethodEntity p) {
        service.savePaymentMethod(p);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * Endpoint para actualizar un método de pago existente.
     * Este endpoint actualiza un método de pago existente en la base de datos.
     * */
    @PostMapping("/update")
    public void update(@RequestBody PaymentMethodEntity p) {
        service.updatePaymentMethod(p);
    }


    /**
     * Endpoint para eliminar un método de pago.
     * Este endpoint elimina un método de pago específico basado en su ID.
     * */
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.deletePaymentMethod(id);
    }

    // -----------------------------------------------------------------
    // CONSULTAS ESPECÍFICAS
    // -----------------------------------------------------------------


    /**
     * Endpoint para obtener los métodos de pago por ID de compañía.
     * Este endpoint devuelve una lista de métodos de pago específicos basados en el ID de la compañía.
     * */
    @GetMapping("/company/{companyId}")
    public List<PaymentMethodEntity> getByCompanyId(@PathVariable int companyId) {
        return service.getPaymentMethodsByCompanyId(companyId);
    }
}
