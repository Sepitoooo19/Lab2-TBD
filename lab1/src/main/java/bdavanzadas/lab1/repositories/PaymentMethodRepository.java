package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.PaymentMethodEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 *  La clase PaymentMethodRepository representa el repositorio de métodos de pago en la base de datos.
 *  Esta clase contiene métodos para guardar, actualizar, eliminar y buscar métodos de pago en la base de datos.
 *
 */
@Repository
public class PaymentMethodRepository implements PaymentMethodRepositoryInt {


    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Metodo para obtener todos los payment methods de la base de datos.
     * @return Una lista de payment methods.
     *
     */
    public List<PaymentMethodEntity> findAll() {
        String sql = "SELECT * FROM payment_methods";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new PaymentMethodEntity(
                        rs.getInt("id"),
                        rs.getString("type")));
    }


    /**
     * Metodo para buscar un payment method por su id.
     * @param "id" El id del payment method a buscar.
     * @return El payment method encontrado.
     *
     */
    public PaymentMethodEntity findById(int id) {
        String sql = "SELECT * FROM payment_methods WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            PaymentMethodEntity p = new PaymentMethodEntity();
            p.setId(rs.getInt("id"));
            p.setType(rs.getString("type"));
            return p;
        });
    }


    /**
     * Metodo para buscar un payment method por su type.
     * @param "type" El type del payment method a buscar.
     * @return El payment method encontrado.
     *
     */
    public PaymentMethodEntity findByType(String type) {
        String sql = "SELECT * FROM payment_methods WHERE type = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{type}, (rs, rowNum) -> {
            PaymentMethodEntity p = new PaymentMethodEntity();
            p.setId(rs.getInt("id"));
            p.setType(rs.getString("type"));
            return p;
        });
    }



    /**
     * Metodo para buscar guardar un payment method en la base de datos.
     * @param "p" El payment method a guardar.
     * @return void
     */

    public void save(PaymentMethodEntity p) {
        String sql = "INSERT INTO payment_methods (type) VALUES (?)";
        jdbcTemplate.update(sql, p.getType());
    }



    /**
     * Metodo para actualizar un payment method en la base de datos.
     * @param "p" El payment method a actualizar.
     * @return void
     *
     */
    public void update(PaymentMethodEntity p) {
        String sql = "UPDATE payment_methods SET type = ? WHERE id = ?";
        jdbcTemplate.update(sql, p.getType(), p.getId());
    }




    /**
     * Metodo para eliminar un payment method de la base de datos.
     * @param "id" El id del payment method a eliminar.
     * @return void
     *
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM payment_method WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    /**
     * Metodo para encontrar todos los payment methods asociados a un companyId.
     * @param "companyId" El id de la compañia.
     * @return Una lista de payment methods asociados a la compañia.
     *
     */
    public List<PaymentMethodEntity> getPaymentMethodsByCompanyId(int companyId) {
        String sql = """
        SELECT pm.id, pm.type
        FROM payment_methods pm
        INNER JOIN company_payment_methods cpm ON pm.id = cpm.payment_method_id
        WHERE cpm.company_id = ?
    """;

        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) -> {
            PaymentMethodEntity paymentMethod = new PaymentMethodEntity();
            paymentMethod.setId(rs.getInt("id"));
            paymentMethod.setType(rs.getString("type"));
            return paymentMethod;
        });
    }


}

