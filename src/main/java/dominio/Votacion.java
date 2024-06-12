
package dominio;

import java.util.List;

public class Votacion {
    private String pregunta;
    private List<String> opciones;

    public Votacion(String pregunta, List<String> opciones) {
        this.pregunta = pregunta;
        this.opciones = opciones;
    }

    public String getPregunta() {
        return pregunta;
    }

    public List<String> getOpciones() {
        return opciones;
    }
}

