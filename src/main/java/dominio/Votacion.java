package dominio;

import java.util.List;

public class Votacion {
    private String id; // Identificador único de la votación
    private String nombre; // Nombre de la votación
    private String pregunta; // Pregunta de la votación
    private List<String> opciones; // Lista de opciones de la votación
    private boolean activa; // Estado de la votación, activa o no

    // Constructor completo
    public Votacion(String id, String nombre, String pregunta, List<String> opciones, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.pregunta = pregunta;
        this.opciones = opciones;
        this.activa = activa;
    }

    // Getters para acceder a los atributos
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPregunta() {
        return pregunta;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public boolean isActiva() {
        return activa;
    }

    // Setters si es necesario modificar los atributos después de la creación
    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
