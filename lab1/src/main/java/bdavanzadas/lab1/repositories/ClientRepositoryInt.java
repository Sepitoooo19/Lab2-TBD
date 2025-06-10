package bdavanzadas.lab1.repositories;



import java.util.List;

import bdavanzadas.lab1.entities.ClientEntity;


/**
 *
 *  La interfaz ClientRepositoryInt define los métodos para interactuar con la base de datos de clientes.
 *  Esta interfaz contiene métodos para guardar, actualizar, eliminar y buscar clientes en la base de datos.
 *
 */
public interface ClientRepositoryInt {

    /**
     * Metodo para guardar un cliente en la base de datos.
     * @param "client" El cliente a guardar.
     *
     */
    List<ClientEntity> findAll();

    /**
     * Metodo para guardar un cliente en la base de datos.
     * @param "client" El cliente a guardar.
     *
     */
    void save(ClientEntity client);

    /**
     * Metodo para actualizar un cliente en la base de datos.
     * @param "client" El cliente a actualizar.
     *
     */
    void update(ClientEntity client);

    /**
     * Metodo para eliminar un cliente de la base de datos.
     * @param "id" El id del cliente a eliminar.
     *
     */
    void delete(int id);

    /**
     * Metodo para buscar un cliente por su id.
     * @param "id" El id del cliente a buscar.
     * @return El cliente encontrado.
     *
     */
    ClientEntity findById(int id);


}
