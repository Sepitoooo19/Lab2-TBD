package bdavanzadas.lab1.Controllers;


import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientEntity>> getAllClients() {
        List<ClientEntity> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable int id) {
        ClientEntity client = clientService.getClientById(id);
        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientEntity client) {
        try {
            clientService.saveClient(client);
            return new ResponseEntity<>(client, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    "Formato de ubicación inválido. Use 'POINT(longitud latitud)'",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable int id, @RequestBody ClientEntity client) {
        try {
            ClientEntity existingClient = clientService.getClientById(id);
            if (existingClient != null) {
                client.setId(id);
                clientService.updateClient(client);
                return new ResponseEntity<>(client, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    "Formato de ubicación inválido. Use 'POINT(longitud latitud)'",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable int id) {
        ClientEntity existingClient = clientService.getClientById(id);
        if (existingClient != null) {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getClientNameById(@PathVariable int id) {
        String clientName = clientService.getNameByClientId(id);
        return clientName != null
                ? new ResponseEntity<>(clientName, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Obtiene los datos del cliente autenticado
     * @return Datos del cliente o mensaje de error
     */
    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedClientProfile() {
        try {
            Map<String, Object> clientData = clientService.getAuthenticatedClientData();
            return ResponseEntity.ok(clientData);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No existe un cliente asociado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Cliente no encontrado", "details", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error del servidor", "details", e.getMessage()));
        }
    }
}
