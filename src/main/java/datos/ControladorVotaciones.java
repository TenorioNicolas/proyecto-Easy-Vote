package datos;


import dominio.Votacion;
import java.io.*;
import java.util.*;


public class ControladorVotaciones {
    private Map<String, Votacion> votaciones = new HashMap<>();
    private final String VOTACIONES_FILE_PATH = "votaciones.csv";
    private final String VOTOS_FILE_PATH = "votos.csv";
    private int contadorVotaciones = 1;
    private Scanner scanner;


    public ControladorVotaciones() {
        cargarVotacionesDesdeArchivo();
        this.scanner = new Scanner(System.in);
    }

    public boolean mostrarVotacionesDisponibles() {
        if (votaciones.isEmpty()) {
            System.out.println("No hay votaciones disponibles en este momento.");
            return false;
        }
        System.out.println("Votaciones disponibles:");
        for (Map.Entry<String, Votacion> entry : votaciones.entrySet()) {
            Votacion votacion = entry.getValue();
            System.out.println(entry.getKey() + ": " + votacion.getPregunta() + " - " + (votacion.isActiva() ? "Activa" : "Inactiva"));
        }
        return true;
    }


    public void votarEnVotacionExistente(Scanner scanner) {
        System.out.println("Ingrese el ID de la votación en la que desea votar:");
        String idVotacion = scanner.nextLine();
        Votacion votacion = votaciones.get(idVotacion);
        if (votacion == null) {
            System.out.println("No se encontró ninguna votación con el ID especificado.");
            return;
        }
        if (!votacion.isActiva()) {
            System.out.println("Votación ya vencida.");
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
    public Votacion obtenerVotacionPorID(String idVotacion) {
        return votaciones.get(idVotacion);
    }

    public void crearVotacion(Scanner scanner) {
        System.out.println("Ingrese la pregunta de la votación:");
        String pregunta = scanner.nextLine().trim();
        while (pregunta.isEmpty()) {
            System.out.println("La pregunta no puede estar vacía. Por favor, ingrese una pregunta válida:");
            pregunta = scanner.nextLine().trim();
        }

        System.out.println("Cuántas opciones tendrá la votación?");
        int cantidadOpciones = leerNumeroEntero(scanner);
        List<String> opciones = new ArrayList<>();
        for (int i = 1; i <= cantidadOpciones; i++) {
            System.out.println("Ingrese la opción " + i + ":");
            String opcion = scanner.nextLine().trim();
            while (opcion.isEmpty()) {
                System.out.println("La opción no puede estar vacía. Por favor, ingrese una opción válida:");
                opcion = scanner.nextLine().trim();
            }
            opciones.add(opcion);
        }

        // Asumimos que las nuevas votaciones son creadas activas
        Votacion nuevaVotacion = new Votacion(pregunta, opciones, true);
        String idVotacion = "V" + contadorVotaciones++; // Formato V1, V2, V3, ...
        votaciones.put(idVotacion, nuevaVotacion);
        System.out.println("Votación creada con éxito. ID de votación: " + idVotacion);
        guardarVotacionesEnArchivo();
    }

    // Método auxiliar para leer un número entero de forma segura
    private int leerNumeroEntero(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número entero. Intenta de nuevo.");
            }
        }
    }

    private void guardarVotacionesEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VOTACIONES_FILE_PATH))) {
            for (Map.Entry<String, Votacion> entry : votaciones.entrySet()) {
                Votacion votacion = entry.getValue();
                pw.println(entry.getKey() + "," + votacion.getPregunta() + "," +
                        String.join(",", votacion.getOpciones()) + "," +
                        (votacion.isActiva() ? "activa" : "inactiva"));
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar las votaciones: " + e.getMessage());
        }
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
                String idVotacion = partes[0];
                String pregunta = partes[1];
                List<String> opciones = Arrays.asList(partes).subList(2, partes.length - 1);
                boolean activa = partes[partes.length - 1].equals("activa");
                votaciones.put(idVotacion, new Votacion(pregunta, opciones, activa));

                // Actualizar el contador de votaciones basado en el ID más alto
                int currentIdNumber = Integer.parseInt(idVotacion.substring(1)); // Extrae el número después de 'V'
                contadorVotaciones = Math.max(contadorVotaciones, currentIdNumber + 1);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de votaciones: " + e.getMessage());
        }
    }


    // Método para registrar un voto en el archivo de votos
    public void registrarVoto(String idVotacion, String matriculaUsuario, String opcionSeleccionada) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VOTOS_FILE_PATH, true))) {
            pw.println(idVotacion + "," + matriculaUsuario + "," + opcionSeleccionada);
        } catch (IOException e) {
            System.err.println("No se pudo guardar el voto: " + e.getMessage());
        }
    }

    // Método para verificar si un usuario ya ha votado en una votación
    public boolean usuarioHaVotado(String idVotacion, String matriculaUsuario) {
        File file = new File(VOTOS_FILE_PATH);
        if (!file.exists()) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3 && partes[0].equals(idVotacion) && partes[1].equals(matriculaUsuario)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de votos: " + e.getMessage());
        }
        return false;
    }

    public void cambiarEstadoVotacion(String idVotacion, boolean nuevoEstado) {
        Votacion votacion = votaciones.get(idVotacion);
        if (votacion != null) {
            votacion.setActiva(nuevoEstado);
            guardarVotacionesEnArchivo(); // Asegúrate de guardar los cambios en el archivo CSV
            System.out.println("El estado de la votación ha sido cambiado a " + (nuevoEstado ? "activa" : "inactiva"));
        } else {
            System.out.println("No se encontró ninguna votación con el ID especificado.");
        }
    }
}