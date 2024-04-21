package org.example;

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
            mostrarMenu(usuarioValidado);
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

    public static void mostrarMenu(Usuario usuario) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione una opción:");
        if (usuario.getRol().equals("Creador") || usuario.getRol().equals("Creador y Votante")) {
            System.out.println("1. Crear una votación");
        }
        if (usuario.getRol().equals("Votante") || usuario.getRol().equals("Creador y Votante")) {
            if (votaciones.isEmpty()) {
                System.out.println("No hay votaciones disponibles por el momento.");
                return; // Aquí termina la ejecución si no hay votaciones disponibles.
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
                    mostrarMenu(usuario);
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static void crearVotacion(Scanner scanner) {
        System.out.println("Creación de votación:");

        // Leer la pregunta de la votación
        System.out.print("Ingrese la pregunta de la votación: ");
        String pregunta = scanner.nextLine();

        // Leer el número de opciones
        int numOpciones = leerEntero(scanner, "Ingrese el número de opciones: ");

        // Leer las opciones
        List<String> opciones = new ArrayList<>();
        for (int i = 0; i < numOpciones; i++) {
            System.out.print("Ingrese la opción " + (i + 1) + ": ");
            opciones.add(scanner.nextLine());
        }

        // Mostrar resumen de la votación
        System.out.println("Resumen de la votación:");
        System.out.println("Pregunta: " + pregunta);
        System.out.println("Opciones:");
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }

        // Crear la votación y agregarla al Map
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
            // Saltar la línea del encabezado
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 5) {  // Asegúrate de que hay al menos 5 elementos
                    Usuario usuario = new Usuario(
                            datos[0], // matricula
                            datos[1], // contraseña
                            datos[2], // nombre
                            datos[3], // apellido
                            datos[4]  // rol
                    );
                    usuarios.put(datos[0], usuario);
                } else {
                    // Manejar el caso de que no haya suficientes datos en la línea
                    System.err.println("Línea incompleta o mal formada: " + linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    private static final String NOMBRE_ARCHIVO_VOTACIONES = "votaciones.csv";

    public static void guardarVotacionesEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new File(NOMBRE_ARCHIVO_VOTACIONES))) {
            for (Map.Entry<String, Votacion> votacionEntry : votaciones.entrySet()) {
                Votacion votacion = votacionEntry.getValue();
                pw.println(votacionEntry.getKey() + "," + votacion.getPregunta() + "," + String.join(",", votacion.getOpciones()));
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo guardar las votaciones: " + e.getMessage());
        }
    }

    public static void cargarVotacionesDesdeArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_ARCHIVO_VOTACIONES))) {
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


    static class Usuario {
        private String matricula;
        private String contraseña;
        private String nombre;
        private String apellido;
        private String rol; // "Votante", "Creador" o "Creador y Votante"

        public Usuario(String matricula, String contraseña, String nombre, String apellido, String rol) {
            this.matricula = matricula;
            this.contraseña = contraseña;
            this.nombre = nombre;
            this.apellido = apellido;
            this.rol = rol;
        }

        public String getMatricula() {
            return matricula;
        }

        public String getContraseña() {
            return contraseña;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public String getRol() {
            return rol;
        }
    }

    static class Votacion {
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
}