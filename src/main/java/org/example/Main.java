package org.example;

import java.io.*;
import java.util.*;

public class Main {
    // Variables estáticas para mantener el conteo de votaciones y almacenar los datos de votaciones y usuarios.
    private static int contadorVotaciones = 1;
    private static Map<String, Votacion> votaciones = new HashMap<>();
    private static Map<String, Usuario> usuarios = new HashMap<>();
    private static final String NOMBRE_ARCHIVO_VOTACIONES = "votaciones.csv";
    private static final String NOMBRE_ARCHIVO_VOTOS = "votos.csv";

    public static void main(String[] args) {
        // Carga inicial de datos de votaciones y usuarios.
        cargarVotacionesDesdeArchivo();
        usuarios = leerUsuariosDesdeCSV("matriculas.csv"); // Este debería devolver un Map<String, Usuario>
        Scanner scanner = new Scanner(System.in);

        // Autenticación del usuario.
        System.out.print("Ingrese su matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contraseña = scanner.nextLine();

        Usuario usuarioValidado = validarUsuario(matricula, contraseña, usuarios);

        if (usuarioValidado != null) {
            System.out.println("Usuario y contraseña válidos. Bienvenido, " + usuarioValidado.getNombre() + "!");
            mostrarMenu(usuarioValidado);
        } else {
            System.out.println("Usuario o contraseña incorrectos. Inténtelo nuevamente.");
        }
        scanner.close();
    }

    // Valida que el usuario exista y que la contraseña coincida.
    public static Usuario validarUsuario(String matricula, String contraseña, Map<String, Usuario> usuarios) {
        Usuario usuario = usuarios.get(matricula);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            return usuario;
        }
        return null;
    }


    // Muestra el menú de opciones basado en el rol del usuario.
// Muestra el menú de opciones basado en el rol del usuario.
    public static void mostrarMenu(Usuario usuario) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("Seleccione una opción:");
            if (usuario.getRol().equals("Creador") || usuario.getRol().equals("Creador y Votante")) {
                System.out.println("1. Crear una votación");
            }
            if (usuario.getRol().equals("Votante") || usuario.getRol().equals("Creador y Votante")) {
                System.out.println("2. Votar en una votación existente");
            }
            System.out.println("3. Salir");

            int opcion = leerEntero(scanner, "Ingrese el número de la opción deseada: ");

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
                        if (votaciones.isEmpty()) {
                            System.out.println("No hay votaciones disponibles en este momento.");
                            salir = true; // El usuario no puede hacer nada más si no hay votaciones.
                        } else {
                            System.out.println("Ha elegido votar en una votación existente.");
                            votarEnVotacionExistente(scanner, usuario); // Pasamos el usuario validado como argumento.
                        }
                    } else {
                        System.out.println("No tiene permiso para votar.");
                    }
                    break;
                case 3:
                    salir = true;
                    System.out.println("Gracias por usar el sistema de votaciones.");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intente de nuevo.");
                    break;
            }
        }
        scanner.close();
    }

    // Método para crear una nueva votación.
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
        System.out.println("Resumen de la votación:");
        System.out.println("Pregunta: " + pregunta);
        System.out.println("Opciones:");
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
        Votacion nuevaVotacion = new Votacion(pregunta, opciones);
        String idVotacion = "V" + contadorVotaciones++;
        votaciones.put(idVotacion, nuevaVotacion);
        System.out.println("Votación creada con éxito. ID de votación: " + idVotacion);
        guardarVotacionesEnArchivo();
    }

    // Muestra las votaciones disponibles.
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

    // Permite a un usuario votar en una votación existente.
    public static void votarEnVotacionExistente(Scanner scanner, Usuario usuario) {
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
            guardarVotoEnArchivo(usuario.getMatricula(), idVotacion, opcionVotada);
        }
    }


    // Método utilitario para leer un número entero desde la consola.
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

    // Carga los usuarios desde un archivo CSV.
    public static Map<String, Usuario> leerUsuariosDesdeCSV(String nombreArchivo) {
        Map<String, Usuario> usuarios = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 5) {
                    Usuario usuario = new Usuario(
                            datos[0], // matricula
                            datos[1], // contraseña
                            datos[2], // nombre
                            datos[3], // apellido
                            datos[4]  // rol
                    );
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

    // Guarda las votaciones actuales en un archivo.
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

    // Carga las votaciones desde un archivo.
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

    public static void guardarVotoEnArchivo(String matricula, String idVotacion, int opcionVotada) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(NOMBRE_ARCHIVO_VOTOS, true))) {
            pw.println(matricula + "," + idVotacion + "," + opcionVotada);
        } catch (IOException e) {
            System.err.println("No se pudo guardar el voto: " + e.getMessage());
        }
    }

    static class Usuario {
        private String matricula;
        private String contraseña;
        private String nombre;
        private String apellido;
        private String rol; // Puede ser "Votante", "Creador" o "Creador y Votante"

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