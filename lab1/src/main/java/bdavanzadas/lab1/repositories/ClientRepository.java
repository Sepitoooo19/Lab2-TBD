package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 *  La clase ClientRepository representa el repositorio de clientes en la base de datos.
 *  Esta clase contiene métodos para guardar, actualizar, eliminar y buscar clientes en la base de datos.
 *
 */
@Repository
public class ClientRepository implements ClientRepositoryInt {

    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * Metodo para guardar un cliente en la base de datos.
     * @param "client" El cliente a guardar.
     *
     */
    public void save(ClientEntity client) {
        String sql = "INSERT INTO clients (user_id, name, rut, email, phone, address, ubication) " +
                "VALUES (?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 4326))";
        jdbcTemplate.update(
                sql,
                client.getUserId(),
                client.getName(),
                client.getRut(),
                client.getEmail(),
                client.getPhone(),
                client.getAddress(),
                client.getUbication() // Debe ser un String en formato WKT, ej: "POINT(-70.12345 -33.98765)"
        );
    }

    /**
     * Metodo para actualizar un cliente en la base de datos.
     * @param "client" El cliente a actualizar.
     *
     */
    public void update(ClientEntity client) {
        String sql = "UPDATE clients SET name = ?, rut = ?, email = ?, phone = ?, address = ?, " +
                "ubication = ST_GeomFromText(?, 4326) WHERE id = ?";
        jdbcTemplate.update(
                sql,
                client.getName(),
                client.getRut(),
                client.getEmail(),
                client.getPhone(),
                client.getAddress(),
                client.getUbication(), // WKT
                client.getId()
        );
    }


    /**
     * Metodo para eliminar un cliente de la base de datos.
     * @param "id" El id del cliente a eliminar.
     *
     */
    public void delete(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Metodo para buscar un cliente por su id.
     * @param "id" El id del cliente a buscar.
     * @return El cliente encontrado.
     *
     */
    public ClientEntity findById(int id) {
        String sql = "SELECT id, name, rut, email, phone, address, user_id, " +
                "ST_AsText(ubication) as ubication FROM clients WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            ClientEntity client = new ClientEntity();
            client.setId(rs.getInt("id"));
            client.setName(rs.getString("name"));
            client.setRut(rs.getString("rut"));
            client.setEmail(rs.getString("email"));
            client.setPhone(rs.getString("phone"));
            client.setAddress(rs.getString("address"));
            client.setUbication(rs.getString("ubication")); // Asigna el WKT directamente
            return client;
        });
    }

    public ClientEntity findByUserId(int userId) {
        String sql = "SELECT id, name, rut, email, phone, address, user_id, " +
                "ST_AsText(ubication) as ubication FROM clients WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> {
                ClientEntity client = new ClientEntity();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setRut(rs.getString("rut"));
                client.setEmail(rs.getString("email"));
                client.setPhone(rs.getString("phone"));
                client.setAddress(rs.getString("address"));
                client.setUserId(rs.getInt("user_id"));
                client.setUbication(rs.getString("ubication"));
                return client;
            });
        } catch (EmptyResultDataAccessException e) {
            return null; // Devuelve null si no se encuentra el cliente
        }
    }

    /**
     * Metodo para buscar todos los clientes.
     * @return Una lista de todos los clientes.
     *
     */
    public List<ClientEntity> findAll() {
        String sql = "SELECT id, name, rut, email, phone, address, user_id, " +
                "ST_AsText(ubication) as ubication FROM clients";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ClientEntity client = new ClientEntity();
            client.setId(rs.getInt("id"));
            client.setName(rs.getString("name"));
            client.setRut(rs.getString("rut"));
            client.setEmail(rs.getString("email"));
            client.setPhone(rs.getString("phone"));
            client.setAddress(rs.getString("address"));
            client.setUbication(rs.getString("ubication")); // WKT
            return client;
        });
    }

    /**
     * Metodo para buscar un cliente por su rut.
     * @param "rut" El rut del cliente a buscar.
     * @return El cliente encontrado.
     *
     */
    //getNameByClientId
    public String getNameByClientId(int clientId) {
        String sql = "SELECT name FROM clients WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{clientId}, String.class);
    }

    /**
     * Encuentra clientes que están a más de 5 km de todas las empresas registradas
     * @return Lista de ClientEntity que cumplen con el criterio de distancia
     */
    public List<ClientEntity> findClientsBeyond5KmFromCompanies() {
        String sql = """
        SELECT 
            c.id,
            c.name,
            c.rut,
            c.email,
            c.phone,
            c.address,
            c.user_id,
            ST_AsText(c.ubication) as ubication
        FROM clients c
        WHERE NOT EXISTS (
            SELECT 1 
            FROM companies co
            WHERE ST_DWithin(
                c.ubication::geography,
                co.ubication::geography,
                5000  -- 5000 metros = 5 km
            )
        )
        ORDER BY c.name
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ClientEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("rut"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("user_id"),
                        rs.getString("ubication")
                ));
    }

}