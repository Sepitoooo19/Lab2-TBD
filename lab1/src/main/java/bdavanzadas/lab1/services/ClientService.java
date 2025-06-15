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

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Patrón para validar WKT (ej: "POINT(-70.123 -33.456)")
    private static final Pattern WKT_PATTERN = Pattern.compile(
            "^POINT\\(-?\\d+\\.?\\d* -?\\d+\\.?\\d*\\)$"
    );

    // --- Métodos existentes (con validación para save y update) ---

    @Transactional(readOnly = true)
    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ClientEntity getClientById(int id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public void saveClient(ClientEntity client) {
        validateUbicacion(client.getUbication()); // Valida el WKT antes de guardar
        clientRepository.save(client);
    }

    @Transactional
    public void updateClient(ClientEntity client) {
        validateUbicacion(client.getUbication()); // Valida el WKT antes de actualizar
        clientRepository.update(client);
    }

    @Transactional
    public void deleteClient(int id) {
        clientRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public String getNameByClientId(int id) {
        ClientEntity client = clientRepository.findById(id);
        return client != null ? client.getName() : null;
    }

    // --- Método de validación adicional ---
    private void validateUbicacion(String ubicacion) {
        if (ubicacion == null || !WKT_PATTERN.matcher(ubicacion).matches()) {
            throw new IllegalArgumentException(
                    "Formato WKT inválido. Debe ser 'POINT(longitud latitud)'."
            );
        }
    }


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