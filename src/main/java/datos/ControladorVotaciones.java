package datos;

import dominio.Votacion;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ControladorVotaciones {
    private Map<String, Votacion> votaciones = new HashMap<>();
    private final String VOTACIONES_FILE_PATH = "votaciones.csv";
    private int contadorVotaciones = 0; // Iniciar desde 0 para ajustarlo según el archivo

    private GestorVotos gestorVotos;

    public ControladorVotaciones(GestorVotos gestorVotos) {
        this.gestorVotos = gestorVotos;
        cargarVotacionesDesdeArchivo();
        ajustarContadorVotaciones(); // Ajustar contador después de cargar las votaciones
    }

    private void cargarVotacionesDesdeArchivo() {
        File file = new File(VOTACIONES_FILE_PATH);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length < 5) continue; // Saltar líneas mal formadas

                String idVotacion = partes[0];
                String nombre = partes[1];
                String pregunta = partes[2];
                List<String> opciones = Arrays.asList(partes).subList(3, partes.length - 1);
                boolean activa = partes[partes.length - 1].equals("activa");
                votaciones.put(idVotacion, new Votacion(idVotacion, nombre, pregunta, opciones, activa));
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de votaciones: " + e.getMessage());
        }
    }

    private void ajustarContadorVotaciones() {
        if (!votaciones.isEmpty()) {
            contadorVotaciones = votaciones.keySet().stream()
                    .mapToInt(id -> Integer.parseInt(id.substring(1))) // Extraer parte numérica del ID
                    .max()
                    .getAsInt(); // Obtener el máximo ID
        }
    }

    public List<Votacion> getVotaciones() {
        return new ArrayList<>(votaciones.values());
    }

    public List<Votacion> getVotacionesActivas() {
        return votaciones.values().stream()
                .filter(Votacion::isActiva)
                .collect(Collectors.toList());
    }

    public void crearVotacion(String nombre, String pregunta, List<String> opciones) {
        String idVotacion = "V" + (++contadorVotaciones); // Incrementar primero para asegurar un nuevo ID
        Votacion nuevaVotacion = new Votacion(idVotacion, nombre, pregunta, opciones, true);
        votaciones.put(idVotacion, nuevaVotacion);
        guardarVotacionesEnArchivo();
    }

    private void guardarVotacionesEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VOTACIONES_FILE_PATH))) {
            for (Map.Entry<String, Votacion> entry : votaciones.entrySet()) {
                Votacion votacion = entry.getValue();
                pw.println(votacion.getId() + "," + votacion.getNombre() + "," + votacion.getPregunta() + "," +
                        String.join(",", votacion.getOpciones()) + "," + (votacion.isActiva() ? "activa" : "inactiva"));
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar las votaciones: " + e.getMessage());
        }
    }

    public void cambiarEstadoVotacion(String idVotacion, boolean nuevoEstado) {
        Votacion votacion = votaciones.get(idVotacion);
        if (votacion != null) {
            votacion.setActiva(nuevoEstado);
            guardarVotacionesEnArchivo();
        }
    }

    public GestorVotos getGestorVotos() {
        return gestorVotos;
    }

    public Map<String, Integer> obtenerResultadosVotacion(String idVotacion) {
        return gestorVotos.contarVotos(idVotacion);
    }
}