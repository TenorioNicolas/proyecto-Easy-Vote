package datos;

import dominio.Votacion;  // Importa la clase Votacion para usarla en la gestión de votaciones.
import java.io.*;  // Importa clases de I/O para manejar la lectura y escritura de archivos.
import java.util.*;  // Importa clases de utilidad para usar colecciones como Map y List.
import java.util.stream.Collectors;  // Importa Collectors para facilitar operaciones de filtrado y colección.

public class ControladorVotaciones {
    private Map<String, Votacion> votaciones = new HashMap<>();  // Almacena todas las votaciones por su ID.
    private final String VOTACIONES_FILE_PATH = "votaciones.csv";  // Ruta del archivo CSV donde se guardan las votaciones.
    private int contadorVotaciones = 0;  // Contador para asignar IDs únicos a nuevas votaciones.

    private GestorVotos gestorVotos;  // Gestor de votos para manejar las operaciones relacionadas con los votos.

    // Constructor que inicializa el gestor de votos y carga votaciones desde un archivo.
    public ControladorVotaciones(GestorVotos gestorVotos) {
        this.gestorVotos = gestorVotos;
        cargarVotacionesDesdeArchivo();
        ajustarContadorVotaciones(); // Ajusta el contador basándose en los IDs máximos encontrados.
    }

    // Carga las votaciones desde un archivo CSV.
    private void cargarVotacionesDesdeArchivo() {
        File file = new File(VOTACIONES_FILE_PATH);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length < 5) continue; // Ignora líneas mal formadas.
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

    // Ajusta el contador de votaciones basándose en el ID más alto existente.
    private void ajustarContadorVotaciones() {
        if (!votaciones.isEmpty()) {
            contadorVotaciones = votaciones.keySet().stream()
                    .mapToInt(id -> Integer.parseInt(id.substring(1))) // Extrae el número del ID.
                    .max().orElse(0); // Obtiene el máximo o cero si no hay votaciones.
        }
    }

    // Devuelve una lista de todas las votaciones.
    public List<Votacion> getVotaciones() {
        return new ArrayList<>(votaciones.values());
    }

    // Devuelve una lista de todas las votaciones activas.
    public List<Votacion> getVotacionesActivas() {
        return votaciones.values().stream()
                .filter(Votacion::isActiva)
                .collect(Collectors.toList());
    }

    // Crea una nueva votación y la guarda en el archivo.
    public void crearVotacion(String nombre, String pregunta, List<String> opciones) {
        String idVotacion = "V" + (++contadorVotaciones); // Asigna un nuevo ID.
        Votacion nuevaVotacion = new Votacion(idVotacion, nombre, pregunta, opciones, true);
        votaciones.put(idVotacion, nuevaVotacion);
        guardarVotacionesEnArchivo();
    }

    // Guarda todas las votaciones en el archivo CSV.
    private void guardarVotacionesEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VOTACIONES_FILE_PATH))) {
            for (Map.Entry<String, Votacion> entry : votaciones.entrySet()) {
                Votacion votacion = entry.getValue();
                pw.println(votacion.getId() + "," + votacion.getNombre() + "," + votacion.getPregunta() +
                        "," + String.join(",", votacion.getOpciones()) + "," + (votacion.isActiva() ? "activa" : "inactiva"));
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar las votaciones: " + e.getMessage());
        }
    }

    // Cambia el estado de una votación y actualiza el archivo.
    public void cambiarEstadoVotacion(String idVotacion, boolean nuevoEstado) {
        Votacion votacion = votaciones.get(idVotacion);
        if (votacion != null) {
            votacion.setActiva(nuevoEstado);
            guardarVotacionesEnArchivo();
        }
    }

    // Obtiene los resultados de votación para una votación específica.
    public Map<String, Integer> obtenerResultadosVotacion(String idVotacion) {
        return gestorVotos.contarVotos(idVotacion);
    }

    // Devuelve una lista de votaciones inactivas.
    public List<Votacion> getVotacionesInactivas() {
        return votaciones.values().stream()
                .filter(v -> !v.isActiva())
                .collect(Collectors.toList());
    }

    // Devuelve el gestor de votos asociado.
    public GestorVotos getGestorVotos() {
        return gestorVotos;
    }
}