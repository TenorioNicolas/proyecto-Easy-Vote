package dominio;

// Clase Usuario que representa a un usuario del sistema de votación.
public class Usuario {
    // Campos privados para almacenar los detalles del usuario.
    private String matricula;    // Identificador único para el usuario.
    private String contraseña;   // Contraseña para el acceso del usuario.
    private String nombre;       // Nombre del usuario.
    private String apellido;     // Apellido del usuario.
    private String rol;          // Rol del usuario dentro del sistema (por ejemplo, votante, administrador).

    // Constructor que inicializa un nuevo usuario con los detalles proporcionados.
    public Usuario(String matricula, String contraseña, String nombre, String apellido, String rol) {
        this.matricula = matricula;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
    }

    // Métodos getter para acceder a los campos privados del usuario.

    // Devuelve la matrícula del usuario.
    public String getMatricula() {
        return matricula;
    }

    // Devuelve la contraseña del usuario.
    public String getContraseña() {
        return contraseña;
    }

    // Devuelve el nombre del usuario.
    public String getNombre() {
        return nombre;
    }

    // Devuelve el apellido del usuario.
    public String getApellido() {
        return apellido;
    }

    // Devuelve el rol del usuario en el sistema.
    public String getRol() {
        return rol;
    }
}