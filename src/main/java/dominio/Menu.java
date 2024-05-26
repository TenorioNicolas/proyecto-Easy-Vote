package dominio;

import datos.GestorUsuarios;
import datos.ControladorVotaciones;
import java.util.Scanner;

public class Menu {
    private GestorUsuarios gestorUsuarios;
    private ControladorVotaciones controladorVotaciones;
    private Scanner scanner;

    public Menu(GestorUsuarios gestorUsuarios, ControladorVotaciones controladorVotaciones) {
        this.gestorUsuarios = gestorUsuarios;
        this.controladorVotaciones = controladorVotaciones;
        this.scanner = new Scanner(System.in);
    }

    public void iniciarSesionYMostrarMenu() {
        System.out.print("Ingrese su matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contraseña = scanner.nextLine();

        Usuario usuarioValidado = gestorUsuarios.validarUsuario(matricula, contraseña);

        if (usuarioValidado != null) {
            System.out.println("Usuario y contraseña válidos. Bienvenido, " + usuarioValidado.getNombre() + "!");
            mostrarMenu(usuarioValidado);
        } else {
            System.out.println("Usuario o contraseña incorrectos. Inténtelo nuevamente.");
        }
    }

    private void mostrarMenu(Usuario usuario) {
        boolean sesionActiva = true;
        while (sesionActiva) {
            System.out.println("Seleccione una opción:");
            if (usuario.getRol().equals("Creador") || usuario.getRol().equals("Creador y Votante")) {
                System.out.println("1. Crear una votación");
            }
            if (usuario.getRol().equals("Votante") || usuario.getRol().equals("Creador y Votante")) {
                System.out.println("2. Votar en una votación existente");
            }
            System.out.println("3. Salir");

            int opcion = leerEntero("Ingrese el número de la opción deseada: ");

            switch (opcion) {
                case 1:
                    if (usuario.getRol().contains("Creador")) {
                        controladorVotaciones.crearVotacion(scanner);
                    } else {
                        System.out.println("No tiene permiso para crear votaciones.");
                    }
                    break;
                case 2:
                    if (usuario.getRol().contains("Votante")) {
                        controladorVotaciones.mostrarVotacionesDisponibles();
                        controladorVotaciones.votarEnVotacionExistente(scanner);
                    } else {
                        System.out.println("No tiene permiso para votar.");
                    }
                    break;
                case 3:
                    sesionActiva = false;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, ingrese una opción correcta.");
            }
        }
    }




    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número entero. Intenta de nuevo.");
            }
        }
    }
}
