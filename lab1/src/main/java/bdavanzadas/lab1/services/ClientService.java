package bdavanzadas.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.repositories.ClientRepository;
import bdavanzadas.lab1.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Servicio de usuarios.
     * Este servicio se utiliza para interactuar con la base de datos de usuarios.
     */
    @Autowired
    private UserService userService;


    /**
     * Constructor del servicio de clientes.
     * @param clientRepository Repositorio de clientes utilizado para acceder a la base de datos.
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    /**
     * Patrón para validar el formato WKT (Well-Known Text) de una ubicación.
     * Este patrón verifica que la cadena comience con "POINT(" seguido de dos números (longitud y latitud)
     * separados por un espacio, y termine con un paréntesis de cierre.
     */
    // Patrón para validar WKT (ej: "POINT(-70.123 -33.456)")
    private static final Pattern WKT_PATTERN = Pattern.compile(
            "^POINT\\(-?\\d+\\.?\\d* -?\\d+\\.?\\d*\\)$"
    );

    // --- Métodos existentes (con validación para save y update) ---


    /**
     * Obtiene todos los clientes de la base de datos.
     * @return Una lista de entidades de clientes.
     */
    @Transactional(readOnly = true)
    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Obtiene un cliente por su ID.
     * @param id El ID del cliente a buscar.
     * @return La entidad de cliente correspondiente al ID, o null si no se encuentra.
     */
    @Transactional(readOnly = true)
    public ClientEntity getClientById(int id) {
        return clientRepository.findById(id);
    }

    /**
     * Guarda un nuevo cliente en la base de datos.
     * @param client La entidad de cliente a guardar.
     */
    @Transactional
    public void saveClient(ClientEntity client) {
        validateUbicacion(client.getUbication()); // Valida el WKT antes de guardar
        clientRepository.save(client);
    }

    /**
     * Actualiza un cliente existente en la base de datos.
     * @param client La entidad de cliente a actualizar.
     */
    @Transactional
    public void updateClient(ClientEntity client) {
        validateUbicacion(client.getUbication()); // Valida el WKT antes de actualizar
        clientRepository.update(client);
    }

    /**
     * Elimina un cliente de la base de datos por su ID.
     * @param id El ID del cliente a eliminar.
     */
    @Transactional
    public void deleteClient(int id) {
        clientRepository.delete(id);
    }

    /**
     * Busca un cliente por su RUT.
     * @param rut El RUT del cliente a buscar.
     * @return La entidad de cliente correspondiente al RUT, o null si no se encuentra.
     */
    @Transactional(readOnly = true)
    public String getNameByClientId(int id) {
        ClientEntity client = clientRepository.findById(id);
        return client != null ? client.getName() : null;
    }

    /**
     * Valida el formato de la ubicación en WKT (Well-Known Text).
     * Este método verifica que la ubicación tenga el formato correcto
     */
    // --- Método de validación adicional ---
    private void validateUbicacion(String ubicacion) {
        if (ubicacion == null || !WKT_PATTERN.matcher(ubicacion).matches()) {
            throw new IllegalArgumentException(
                    "Formato WKT inválido. Debe ser 'POINT(longitud latitud)'."
            );
        }
    }


    /**
     * Obtiene los datos del cliente autenticado.
     * Este método utiliza el servicio de usuarios para obtener el ID del usuario autenticado
     * y luego busca el cliente asociado a ese ID.
     * @return Un mapa con los datos del cliente, o lanza una excepción si no se encuentra.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getAuthenticatedClientData() {
        try {
            Long userIdLong = userService.getAuthenticatedUserId();
            if (userIdLong == null) {
                throw new RuntimeException("Usuario no autenticado");
            }

            int userId = userIdLong.intValue();
            ClientEntity client = clientRepository.findByUserId(userId);

            if (client == null) {
                throw new RuntimeException("No existe un cliente asociado a este usuario");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("id", client.getId());
            data.put("name", client.getName());
            data.put("rut", client.getRut());
            data.put("email", client.getEmail());
            data.put("phone", client.getPhone());
            data.put("address", client.getAddress());
            data.put("ubication", client.getUbication());

            return data;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener datos del cliente: " + e.getMessage());
        }
    }

    public List<ClientEntity> getClientsBeyond5KmFromCompanies() {
        return clientRepository.findClientsBeyond5KmFromCompanies();
    }
}