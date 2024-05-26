package datos;

import dominio.Votacion;
import java.io.*;
import java.util.*;


public class ControladorVotaciones {
    private Map<String, Votacion> votaciones = new HashMap<>();
    private int contadorVotaciones = 1;
    private final String FILE_PATH = "votaciones.csv";

    public ControladorVotaciones() {
        cargarVotacionesDesdeArchivo();
    }

    public void mostrarVotacionesDisponibles() {
        if (votaciones.isEmpty()) {
            System.out.println("No hay votaciones disponibles en este momento.");
            return;
        }
        System.out.println("Votaciones disponibles:");
        for (Map.Entry<String, Votacion> entry : votaciones.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getPregunta() + " con opciones: " + entry.getValue().getOpciones());
        }
    }

    public void votarEnVotacionExistente(Scanner scanner) {
        System.out.println("Ingrese el ID de la votación en la que desea votar:");
        String idVotacion = scanner.nextLine();
        Votacion votacion = votaciones.get(idVotacion);
        if (votacion == null) {
            System.out.println("No se encontró ninguna votación con el ID especificado.");
            return;
        }
        System.out.println("Votación: " + votacion.getPregunta());
        List<String> opciones = votacion.getOpciones();
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
        System.out.println("Seleccione el número de la opción por la que desea votar:");
        int eleccion = Integer.parseInt(scanner.nextLine()) - 1;
        if (eleccion < 0 || eleccion >= opciones.size()) {
            System.out.println("Selección inválida.");
        } else {
            System.out.println("Ha votado por: " + opciones.get(eleccion));
        }
    }

    public void crearVotacion(Scanner scanner) {
        System.out.println("Ingrese la pregunta de la votación:");
        String pregunta = scanner.nextLine();
        System.out.println("Cuántas opciones tendrá la votación?");
        int cantidadOpciones = Integer.parseInt(scanner.nextLine());
        List<String> opciones = new ArrayList<>();
        for (int i = 1; i <= cantidadOpciones; i++) {
            System.out.println("Ingrese la opción " + i + ":");
            opciones.add(scanner.nextLine());
        }
        Votacion nuevaVotacion = new Votacion(pregunta, opciones);
        String idVotacion = "V" + contadorVotaciones++;
        votaciones.put(idVotacion, nuevaVotacion);
        System.out.println("Votación creada con éxito. ID de votación: " + idVotacion);
        guardarVotacionesEnArchivo();
    }

    private void guardarVotacionesEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, Votacion> entry : votaciones.entrySet()) {
                Votacion votacion = entry.getValue();
                pw.println(entry.getKey() + "," + votacion.getPregunta() + "," + String.join(",", votacion.getOpciones()));
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar las votaciones: " + e.getMessage());
        }
    }

    private void cargarVotacionesDesdeArchivo() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Archivo de votaciones no encontrado.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 3) {
                    String idVotacion = partes[0];
                    String pregunta = partes[1];
                    List<String> opciones = Arrays.asList(partes).subList(2, partes.length);
                    votaciones.put(idVotacion, new Votacion(pregunta, opciones));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de votaciones: " + e.getMessage());
        }
    }
}
