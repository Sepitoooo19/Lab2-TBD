package bdavanzadas.lab1.Controllers;


import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 *
 * La clase ClientController maneja las solicitudes relacionadas con los clientes.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar clientes en la base de datos.
 *
 * */
@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Obtiene todas las entidades de clientes.
     * @return Lista de entidades de clientes
     */

    @GetMapping
    public ResponseEntity<List<ClientEntity>> getAllClients() {
        List<ClientEntity> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    /**
     * Obtiene un cliente por su ID.
     * @param "id" ID del cliente
     * @return Cliente encontrado o NOT FOUND si no existe
     */

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable int id) {
        ClientEntity client = clientService.getClientById(id);
        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    /**
     * Crea un nuevo cliente.
     * @param "client" Entidad de cliente a crear
     * @return Cliente creado o BAD REQUEST si el formato es inválido
     */
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


    /**
     * Actualiza un cliente existente.
     * @param "id" ID del cliente a actualizar
     * @param "client" Entidad de cliente con los nuevos datos
     * @return Cliente actualizado o NOT FOUND si no existe
     */
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


    /**
     * Elimina un cliente por su ID.
     * @param "id" ID del cliente a eliminar
     * @return NO CONTENT si se eliminó correctamente o NOT FOUND si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable int id) {
        ClientEntity existingClient = clientService.getClientById(id);
        if (existingClient != null) {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    /**
     * Obtiene el nombre de un cliente por su ID.
     * @param "id" ID del cliente
     * @return Nombre del cliente o NOT FOUND si no existe
     */
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

    /**
     * Obtiene los clientes ubicados a mas de 5km de la empresa mas cercana
     * @return Lista de clientes que estan a mas de 5km
     */
    @GetMapping("/beyond-5km")
    public ResponseEntity<List<ClientEntity>> getClientsBeyond5Km() {
        List<ClientEntity> clients = clientService.getClientsBeyond5KmFromCompanies();
        return ResponseEntity.ok(clients);
    }
}
