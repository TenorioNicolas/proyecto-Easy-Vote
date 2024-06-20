package dominio;

import java.util.List;

public class Votacion {
    private String pregunta;
    private List<String> opciones;
    private boolean activa; // Verdadero si la votación está activa, falso si está inactiva

    public Votacion(String pregunta, List<String> opciones, boolean activa) {
        this.pregunta = pregunta;
        this.opciones = opciones;
        this.activa = activa;
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

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
