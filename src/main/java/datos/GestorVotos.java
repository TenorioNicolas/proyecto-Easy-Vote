package datos;

import java.io.*; // Importación de clases de IO para manejo de archivos.
import java.util.HashMap; // Importación de HashMap para almacenamiento en memoria de votos.
import java.util.Map; // Importación de la interfaz Map para el manejo de colecciones de pares clave-valor.

// Clase GestorVotos para gestionar las operaciones relacionadas con los votos de las votaciones.
public class GestorVotos {
    private final String VOTOS_FILE_PATH = "votos.csv"; // Ruta del archivo CSV donde se guardan los votos.
    private Map<String, Map<String, String>> votosRegistrados = new HashMap<>(); // Estructura para almacenar los votos.

    // Constructor que carga los votos desde un archivo al crear una instancia de GestorVotos.
    public GestorVotos() {
        cargarVotosDesdeArchivo();
    }

    // Método para cargar votos desde un archivo CSV.
    private void cargarVotosDesdeArchivo() {
        File file = new File(VOTOS_FILE_PATH); // Crea un objeto File para el archivo de votos.
        if (!file.exists()) {
            return; // Si el archivo no existe, sale del método.
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                String idVotacion = partes[0];
                String matriculaUsuario = partes[1];
                String opcionSeleccionada = partes[2];
                votosRegistrados.putIfAbsent(idVotacion, new HashMap<>());
                votosRegistrados.get(idVotacion).put(matriculaUsuario, opcionSeleccionada);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de votos: " + e.getMessage());
        }
    }

    // Método para registrar un nuevo voto en el sistema y actualizar el archivo de votos.
    public void registrarVoto(String idVotacion, String matriculaUsuario, String opcionSeleccionada) {
        votosRegistrados.putIfAbsent(idVotacion, new HashMap<>());
        votosRegistrados.get(idVotacion).put(matriculaUsuario, opcionSeleccionada);
        guardarVotosEnArchivo();
    }

    // Método para guardar todos los votos en un archivo CSV.
    private void guardarVotosEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VOTOS_FILE_PATH))) {
            for (Map.Entry<String, Map<String, String>> votacionEntry : votosRegistrados.entrySet()) {
                String idVotacion = votacionEntry.getKey();
                Map<String, String> votos = votacionEntry.getValue();
                for (Map.Entry<String, String> voto : votos.entrySet()) {
                    pw.println(idVotacion + "," + voto.getKey() + "," + voto.getValue());
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar el archivo de votos: " + e.getMessage());
        }
    }

    // Método para verificar si un usuario ha votado en una votación específica.
    public boolean usuarioHaVotado(String idVotacion, String matriculaUsuario) {
        return votosRegistrados.containsKey(idVotacion) && votosRegistrados.get(idVotacion).containsKey(matriculaUsuario);
    }

    // Método para contar los votos de una votación específica y retornar un mapa con los resultados.
    public Map<String, Integer> contarVotos(String idVotacion) {
        Map<String, Integer> resultados = new HashMap<>();
        if (votosRegistrados.containsKey(idVotacion)) {
            Map<String, String> votosParaVotacion = votosRegistrados.get(idVotacion);
            for (String voto : votosParaVotacion.values()) {
                resultados.put(voto, resultados.getOrDefault(voto, 0) + 1);
            }
        }
        return resultados;
    }
}