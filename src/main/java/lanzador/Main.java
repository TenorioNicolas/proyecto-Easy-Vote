package lanzador;

import dominio.Usuario;
import dominio.Votacion;
import java.io.*;
import java.util.*;

public class Main {
    private static int contadorVotaciones = 1;
    private static Map<String, Votacion> votaciones = new HashMap<>();
    private static Map<String, Usuario> usuarios = new HashMap<>();

    public static void main(String[] args) {
        cargarVotacionesDesdeArchivo();
        usuarios = leerUsuariosDesdeCSV("matriculas.csv");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese su matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contraseña = scanner.nextLine();

        Usuario usuarioValidado = validarUsuario(matricula, contraseña);

        if (usuarioValidado != null) {
            System.out.println("Usuario y contraseña válidos. Bienvenido, " + usuarioValidado.getNombre() + "!");
            mostrarMenu(usuarioValidado, scanner);
        } else {
            System.out.println("Usuario o contraseña incorrectos. Inténtelo nuevamente.");
        }
        scanner.close();
    }

    public static Usuario validarUsuario(String matricula, String contraseña) {
        Usuario usuario = usuarios.get(matricula);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            return usuario;
        }
        return null;
    }

    public static void mostrarMenu(Usuario usuario, Scanner scanner) {
        System.out.println("Seleccione una opción:");
        if (usuario.getRol().equals("Creador") || usuario.getRol().equals("Creador y Votante")) {
            System.out.println("1. Crear una votación");
        }
        if (usuario.getRol().equals("Votante") || usuario.getRol().equals("Creador y Votante")) {
            if (votaciones.isEmpty()) {
                System.out.println("No hay votaciones disponibles por el momento.");
                return;
            }
            System.out.println("2. Votar en una votación existente");
        }

        int opcion = leerEntero(scanner, "Ingrese el número de la opción deseada: ");

        try {
            switch (opcion) {
                case 1:
                    if (usuario.getRol().contains("Creador")) {
                        System.out.println("Ha elegido crear una votación.");
                        crearVotacion(scanner);
                    } else {
                        System.out.println("No tiene permiso para crear votaciones.");
                    }
                    break;
                case 2:
                    if (usuario.getRol().contains("Votante")) {
                        System.out.println("Ha elegido votar en una votación existente.");
                        mostrarVotacionesDisponibles();
                        votarEnVotacionExistente(scanner);
                    } else {
                        System.out.println("No tiene permiso para votar.");
                    }
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, ingrese una opción correcta.");
                    mostrarMenu(usuario, scanner);
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }

    public static void crearVotacion(Scanner scanner) {
        System.out.println("Creación de votación:");
        System.out.print("Ingrese la pregunta de la votación: ");
        String pregunta = scanner.nextLine();
        int numOpciones = leerEntero(scanner, "Ingrese el número de opciones: ");
        List<String> opciones = new ArrayList<>();
        for (int i = 0; i < numOpciones; i++) {
            System.out.print("Ingrese la opción " + (i + 1) + ": ");
            opciones.add(scanner.nextLine());
        }
        Votacion nuevaVotacion = new Votacion(pregunta, opciones);
        String idVotacion = "V" + contadorVotaciones++;
        votaciones.put(idVotacion, nuevaVotacion);
        System.out.println("Votación creada con éxito. ID de votación: " + idVotacion);
        guardarVotacionesEnArchivo();
    }

    public static void mostrarVotacionesDisponibles() {
        if (votaciones.isEmpty()) {
            System.out.println("No hay votaciones disponibles en este momento.");
            return;
        }
        System.out.println("Votaciones disponibles:");
        for (Map.Entry<String, Votacion> entry : votaciones.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getPregunta());
        }
    }

    public static void votarEnVotacionExistente(Scanner scanner) {
        System.out.print("Ingrese el ID de la votación en la que desea votar: ");
        String idVotacion = scanner.nextLine();
        Votacion votacion = votaciones.get(idVotacion);
        if (votacion == null) {
            System.out.println("No se encontró la votación con ese ID. Inténtelo nuevamente.");
            return;
        }
        System.out.println("Votación seleccionada: " + votacion.getPregunta());
        List<String> opciones = votacion.getOpciones();
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
        int opcionVotada = leerEntero(scanner, "Ingrese el número de la opción que desea votar: ");
        if (opcionVotada < 1 || opcionVotada > opciones.size()) {
            System.out.println("Opción inválida. Por favor, ingrese un número válido.");
        } else {
            System.out.println("Voto registrado correctamente para la opción: " + opciones.get(opcionVotada - 1));
        }
    }

    public static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número entero. Intenta de nuevo.");
            }
        }
    }

    public static Map<String, Usuario> leerUsuariosDesdeCSV(String nombreArchivo) {
        Map<String, Usuario> usuarios = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            br.readLine(); // Saltar la línea del encabezado
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 5) {
                    Usuario usuario = new Usuario(datos[0], datos[1], datos[2], datos[3], datos[4]);
                    usuarios.put(datos[0], usuario);
                } else {
                    System.err.println("Línea incompleta o mal formada: " + linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public static void guardarVotacionesEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new File("votaciones.csv"))) {
            for (Map.Entry<String, Votacion> entry : votaciones.entrySet()) {
                Votacion votacion = entry.getValue();
                pw.println(entry.getKey() + "," + votacion.getPregunta() + "," + String.join(",", votacion.getOpciones()));
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo guardar las votaciones: " + e.getMessage());
        }
    }

    public static void cargarVotacionesDesdeArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader("votaciones.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                String idVotacion = partes[0];
                String pregunta = partes[1];
                List<String> opciones = Arrays.asList(partes).subList(2, partes.length);
                Votacion votacion = new Votacion(pregunta, opciones);
                votaciones.put(idVotacion, votacion);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de votaciones no encontrado. Se creará uno nuevo.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de votaciones: " + e.getMessage());
        }
    }
}
