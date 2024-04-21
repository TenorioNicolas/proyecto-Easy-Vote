package org.example;

import java.util.*;

public class Main {

    public static void main(String[] args) {

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
                // Aquí puedes llamar a un método para votar en una votación existente
                break;
            default:
                System.out.println("Opción inválida. Por favor, ingrese 1 o 2.");
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

        // Guardar votacion, de momento sin csv
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


