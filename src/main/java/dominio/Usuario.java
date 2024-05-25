package dominio;

public class Usuario {
    private String matricula;
    private String contraseña;
    private String nombre;
    private String apellido;
    private String rol;

    public Usuario(String matricula, String contraseña, String nombre, String apellido, String rol) {
        this.matricula = matricula;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getRol() {
        return rol;
    }
}
