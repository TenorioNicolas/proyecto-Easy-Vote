package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Main {
    private static final String NOMBRE_ARCHIVO = "votaciones.txt";
    private static final String NOMBRE_ARCHIVO_VOTOS = "votos.txt";
    private static int contadorVotaciones = 1;
    private static Map<String, Votacion> votaciones = new HashMap<>();


    public static void main(String[] args) {
        verificarVotaciones();

        Map<String, String> usuariosContraseñas = new HashMap<>();
        usuariosContraseñas.put("123456789", "contraseña1");
        usuariosContraseñas.put("987654321", "contraseña2");

        String usuarioValidado = ingresarUsuario(usuariosContraseñas);

        if (usuarioValidado != null) {
            System.out.println("Usuario y contraseña válidos. Bienvenido, " + usuarioValidado + "!");
            mostrarMenu();
        } else {
            System.out.println("Usuario o contraseña incorrectos. Inténtelo nuevamente.");
        }
    }

    public static String ingresarUsuario(Map<String, String> usuariosContraseñas) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Ingrese el usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Ingrese la contraseña: ");
            String contraseña = scanner.nextLine();

            if (validarUsuario(usuario, contraseña, usuariosContraseñas)) {
                return usuario;
            } else {
                System.out.println("Usuario o contraseña incorrectos. Inténtelo nuevamente.");
            }
        }
    }
    public static boolean validarUsuario(String usuario, String contraseña, Map<String, String> usuariosContraseñas) {
        String contraseñaAlmacenada = usuariosContraseñas.get(usuario);
        return contraseñaAlmacenada != null && contraseñaAlmacenada.equals(contraseña);
    }
    public static void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione una opción:");
        System.out.println("1. Crear una votación");
        System.out.println("2. Votar en una votación existente");
        int opcion = leerEntero(scanner, "Ingrese el número de la opción deseada: ");

        switch (opcion) {
            case 1:
                System.out.println("Ha elegido crear una votación.");
                crearVotacion(scanner);
                break;
            case 2:
                System.out.println("Ha elegido votar en una votación existente.");
                mostrarVotacionesDisponibles();
                votarEnVotacionExistente(scanner);
                break;
            default:
                System.out.println("Opción inválida. Por favor, ingrese 1 o 2.");
        }
    }
    private static void cargarVotacionesDesdeArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Parsear la línea para obtener los detalles de la votación
                String[] partes = linea.split(",");
                String id = partes[0];
                String pregunta = partes[1];
                List<String> opciones = Arrays.asList(partes).subList(2, partes.length);

                // Agregar la votación al mapa de votaciones si no existe previamente
                if (!votaciones.containsKey(id)) {
                    votaciones.put(id, new Votacion(pregunta, opciones));
                }
            }
        } catch (IOException e) {
            // Manejar errores de lectura del archivo
            e.printStackTrace();
        }
    }// IMPLEMENTAR PARA LEER LAS VOTACIONES Y VOTAR
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

        // Guardar votacion, de momento sin csv
        // Crear la votación y agregarla al Map
        Votacion nuevaVotacion = new Votacion(pregunta, opciones);
        String idVotacion = "V" + contadorVotaciones++;
        votaciones.put(idVotacion, nuevaVotacion);

        System.out.println("Votación creada con éxito. ID de votación: " + idVotacion);
        guardarVotacionesEnArchivo();
        // Mostrar el menú nuevamente
        mostrarMenu();
    }
    private static void guardarVotacionesEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(NOMBRE_ARCHIVO))) {
            for (Map.Entry<String, Votacion> entry : votaciones.entrySet()) {
                String id = entry.getKey();
                Votacion votacion = entry.getValue();
                String linea = id + "," + votacion.getPregunta() + "," + String.join(",", votacion.getOpciones());
                pw.println(linea);
            }
        } catch (IOException e) {
            // Manejar errores de escritura en el archivo
            e.printStackTrace();
        }
    }

    public static void mostrarVotacionesDisponibles() {
        System.out.println("Votaciones disponibles:");
        for (String id : votaciones.keySet()) {
            Votacion votacion = votaciones.get(id);
            System.out.println(id + ": " + votacion.getPregunta());
        }
    }
    public static void votarEnVotacionExistente(Scanner scanner) {
        while (true) {
            System.out.print("Ingrese el ID de la votación en la que desea votar: ");
            String idVotacion = scanner.nextLine();

            Votacion votacion = votaciones.get(idVotacion);
            if (votacion == null) {
                System.out.println("No se encontró la votación con ese ID. Inténtelo nuevamente.");
            } else {
                System.out.println("Votación seleccionada: " + votacion.getPregunta());
                System.out.println("Opciones:");
                List<String> opciones = votacion.getOpciones();
                for (int i = 0; i < opciones.size(); i++) {
                    System.out.println((i + 1) + ". " + opciones.get(i));
                }

                int opcionVotada = leerEntero(scanner, "Ingrese el número de la opción que desea votar: ");
                if (opcionVotada < 1 || opcionVotada > opciones.size()) {
                    System.out.println("Opción inválida. Por favor, ingrese un número válido.");
                } else {
                    // Aquí puedes registrar el voto del usuario en la opción seleccionada
                    registrarVoto(idVotacion, opcionVotada);
                    System.out.println("Voto registrado correctamente.");
                    break; // Salir del bucle una vez que se registra el voto correctamente
                }
            }
        }
    }
    private static void registrarVoto(String idVotacion, int opcionVotada) {
        try {
            // Verificar si el archivo de votos existe, y crearlo si no existe
            if (!Files.exists(Paths.get(NOMBRE_ARCHIVO_VOTOS))) {
                Files.createFile(Paths.get(NOMBRE_ARCHIVO_VOTOS));
            }

            // Escribir el voto en el archivo de votos
            try (PrintWriter pw = new PrintWriter(new FileWriter(NOMBRE_ARCHIVO_VOTOS, true))) {
                pw.println(idVotacion + "," + opcionVotada);
            }
        } catch (IOException e) {
            System.err.println("Error al registrar el voto: " + e.getMessage());
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

    public static void verificarVotaciones() {
        if (!Files.exists(Paths.get(NOMBRE_ARCHIVO))) {
            try {
                Files.createFile(Paths.get(NOMBRE_ARCHIVO));
                System.out.println("Archivo de votaciones creado correctamente.");
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de votaciones: " + e.getMessage());
            }
        }
    }

    public static int leerEntero(Scanner scanner, String mensaje) {
        int numero = 0;
        boolean entradaValida = false;

        while (!entradaValida) {
            try {
                System.out.print(mensaje);
                numero = scanner.nextInt();
                entradaValida = true;
            } catch (InputMismatchException e) {
                System.out.println("Error: Debes ingresar un número entero.");
                scanner.next(); // Limpiar el búfer del scanner para evitar un bucle infinito
            }
            scanner.nextLine(); // Consumir la nueva línea en blanco, para que no consuma dos scanner
        }
        return numero;
    }

}


