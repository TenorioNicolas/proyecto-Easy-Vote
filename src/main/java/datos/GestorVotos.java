package datos;

import dominio.Votacion;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GestorVotos {
    private final String VOTOS_FILE_PATH = "votos.csv";
    private Map<String, Map<String, String>> votosRegistrados = new HashMap<>();

    public GestorVotos() {
        cargarVotosDesdeArchivo();
    }

    private void cargarVotosDesdeArchivo() {
        File file = new File(VOTOS_FILE_PATH);
        if (!file.exists()) {
            return;
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

    public void registrarVoto(String idVotacion, String matriculaUsuario, String opcionSeleccionada) {
        votosRegistrados.putIfAbsent(idVotacion, new HashMap<>());
        votosRegistrados.get(idVotacion).put(matriculaUsuario, opcionSeleccionada);
        guardarVotosEnArchivo();
    }

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

    public boolean usuarioHaVotado(String idVotacion, String matriculaUsuario) {
        return votosRegistrados.containsKey(idVotacion) && votosRegistrados.get(idVotacion).containsKey(matriculaUsuario);
    }

    public Map<String, Integer> contarVotos(String idVotacion) {
        Map<String, Integer> resultados = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(VOTOS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue; // Ignora líneas mal formadas

                String votoIdVotacion = parts[0];
                String voto = parts[2];

                if (votoIdVotacion.equals(idVotacion)) {
                    resultados.put(voto, resultados.getOrDefault(voto, 0) + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de votos: " + e.getMessage());
        }
        return resultados;
    }
}
