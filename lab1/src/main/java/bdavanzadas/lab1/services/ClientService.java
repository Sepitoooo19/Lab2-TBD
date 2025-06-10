package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.repositories.ClientRepository;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

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
}