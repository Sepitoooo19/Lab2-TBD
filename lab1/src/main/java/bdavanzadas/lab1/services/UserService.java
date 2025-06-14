package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.repositories.ClientRepository;
import bdavanzadas.lab1.repositories.DealerRepository;
import bdavanzadas.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 *
 * La clase UserService representa el servicio de usuarios en la aplicación.
 * Esta clase contiene métodos para registrar usuarios, validar credenciales y obtener el ID del usuario autenticado.
 *
 *
 * */
@Service
public class UserService {

    /**
     * Repositorio de usuarios.
     * Este repositorio se utiliza para interactuar con la base de datos de usuarios.
     */
    @Autowired
    private UserRepository userRepository;


    /**
     * Codificador de contraseñas.
     * Este codificador se utiliza para codificar las contraseñas de los usuarios antes de guardarlas en la base de datos.
     */
    @Autowired
    private PasswordEncoder encoder;


    /**
     * Repositorio de clientes.
     * Este repositorio se utiliza para interactuar con la base de datos de clientes.
     */
    @Autowired
    private ClientRepository clientRepository;


    /**
     * Repositorio de dealers.
     * Este repositorio se utiliza para interactuar con la base de datos de dealers.
     */
    @Autowired
    private DealerRepository dealerRepository;


    /**
     * Metodo para registrar un nuevo usuario administrador.
     * @param "username" El nombre de usuario del nuevo administrador.
     * @param "password" La contraseña del nuevo administrador.
     *
     * Este metodo codifica la contraseña del nuevo administrador y la guarda en la base de datos.
     * Si el nombre de usuario ya está en uso, se lanza una excepción.
     * Si el ID del usuario no se genera correctamente, se lanza una excepción.
     *
     */
    public void registerAdmin(String username, String password) {
        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("ADMIN");
        userRepository.save(user);
    }


    /**
     * Metodo para registrar un nuevo usuario cliente.
     * @param "username" El nombre de usuario del nuevo cliente.
     * @param "password" La contraseña del nuevo cliente.
     * @param "name" El nombre del nuevo cliente.
     * @param "rut" El rut del nuevo cliente.
     * @param "email" El email del nuevo cliente.
     * @param "phone" El teléfono del nuevo cliente.
     * @param "address" La dirección del nuevo cliente.
     *
     * Este metodo codifica la contraseña del nuevo cliente y la guarda en la base de datos.
     * Si el nombre de usuario ya está en uso, se lanza una excepción.
     * Si el ID del usuario no se genera correctamente, se lanza una excepción.
     */
    public void registerClient(String username, String password, String name, String rut, String email, String phone, String address, String ubication) {
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso: " + username);
        }

        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("CLIENT");
        userRepository.save(user);

        if (user.getId() == 0) {
            throw new IllegalStateException("No se generó un ID para el usuario");
        }

        ClientEntity client = new ClientEntity();
        client.setName(name);
        client.setRut(rut);
        client.setEmail(email);
        client.setPhone(phone);
        client.setAddress(address);
        client.setUserId(user.getId());
        client.setUbication(ubication != null ? ubication : "POINT(0 0)");  // Usar el parámetro ubication, no client.getUbication()
        clientRepository.save(client);
    }


    /**
     * Metodo para registrar un nuevo usuario dealer.
     * @param "username" El nombre de usuario del nuevo dealer.
     * @param "password" La contraseña del nuevo dealer.
     * @param "name" El nombre del nuevo dealer.
     * @param "rut" El rut del nuevo dealer.
     * @param "email" El email del nuevo dealer.
     * @param "phone" El teléfono del nuevo dealer.
     * @param "vehicle" El vehículo del nuevo dealer.
     * @param "plate" La placa del nuevo dealer.
     *
     * Este metodo codifica la contraseña del nuevo dealer y la guarda en la base de datos.
     * Si el nombre de usuario ya está en uso, se lanza una excepción.
     * Si el ID del usuario no se genera correctamente, se lanza una excepción.
     */
    public void registerDealer(String username, String password, String name, String rut, String email, String phone, String vehicle, String plate, String ubication) {
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso: " + username);
        }

        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("DEALER");
        userRepository.save(user);

        if (user.getId() == 0) {
            throw new IllegalStateException("No se generó un ID para el usuario");
        }

        DealerEntity dealer = new DealerEntity();
        dealer.setName(name);
        dealer.setRut(rut);
        dealer.setEmail(email);
        dealer.setPhone(phone);
        dealer.setVehicle(vehicle);
        dealer.setPlate(plate);
        dealer.setUserId(user.getId());
        dealer.setUbication(ubication != null ? ubication : "POINT(0 0)"); // Asignar una ubicación por defecto, puede ser modificada posteriormente

        dealerRepository.save(dealer);
    }

    /**
     * Metodo para validar las credenciales de un usuario.
     * @param "username" El nombre de usuario a validar.
     * @param "password" La contraseña a validar.
     * @return El usuario encontrado si las credenciales son válidas, null en caso contrario.
     *
     * Este metodo busca un usuario por su nombre de usuario y valida la contraseña.
     */

    public UserEntity validateCredentials(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }


    /**
     * Metodo para obtener el ID del usuario autenticado.
     * @return El ID del usuario autenticado.
     *
     * Este metodo obtiene el ID del usuario autenticado a partir del contexto de seguridad.
     * Si no hay un usuario autenticado, lanza una excepción.
     */
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal(); // Retorna el ID del usuario autenticado
        }
        throw new RuntimeException("Usuario no autenticado");
    }
}