package dominio;

import java.util.List;

// Clase Votacion que representa una votación dentro del sistema de votaciones.
public class Votacion {
    private String id; // Identificador único de la votación.
    private String nombre; // Nombre de la votación.
    private String pregunta; // Pregunta que se plantea en la votación.
    private List<String> opciones; // Lista de opciones disponibles para esta votación.
    private boolean activa; // Estado de la votación, indica si está activa o no.

    // Constructor que inicializa una votación con todos los detalles necesarios.
    public Votacion(String id, String nombre, String pregunta, List<String> opciones, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.pregunta = pregunta;
        this.opciones = opciones;
        this.activa = activa;
    }

    // Métodos getter que proporcionan acceso seguro a los atributos de la votación.

    // Devuelve el identificador único de la votación.
    public String getId() {
        return id;
    }

    // Devuelve el nombre de la votación.
    public String getNombre() {
        return nombre;
    }

    // Devuelve la pregunta asociada a la votación.
    public String getPregunta() {
        return pregunta;
    }

    // Devuelve la lista de opciones disponibles para votar.
    public List<String> getOpciones() {
        return opciones;
    }

    // Indica si la votación está activa.
    public boolean isActiva() {
        return activa;
    }

    // Métodos setter para modificar los atributos de la votación después de su creación, si es necesario.

    // Establece un nuevo ID para la votación.
    public void setId(String id) {
        this.id = id;
    }

    // Establece un nuevo nombre para la votación.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Establece una nueva pregunta para la votación.
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    // Establece nuevas opciones para la votación.
    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    // Activa o desactiva la votación.
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}