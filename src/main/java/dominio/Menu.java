package dominio;


import datos.GestorUsuarios;
import datos.ControladorVotaciones;
import java.util.List;
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
            System.out.println("1. Crear una votación");
            System.out.println("2. Votar en una votación existente");
            System.out.println("3. Salir");


            int opcion = leerEntero("Ingrese el número de la opción deseada: ");


            switch (opcion) {
                case 1:
                    if (usuario.getRol().equals("Creador")) {
                        controladorVotaciones.crearVotacion(scanner);
                    } else {
                        System.out.println("No tiene permiso para crear votaciones.");
                    }
                    break;
                case 2:
                    controladorVotaciones.mostrarVotacionesDisponibles();
                    votarEnVotacionExistente(usuario);
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


    private void votarEnVotacionExistente(Usuario usuario) {
        System.out.println("Ingrese el ID de la votación en la que desea votar:");
        String idVotacion = scanner.nextLine();


        if (controladorVotaciones.usuarioHaVotado(idVotacion, usuario.getMatricula())) {
            System.out.println("Ya has votado en esta votación.");
            return;
        }


        Votacion votacion = controladorVotaciones.obtenerVotacionPorID(idVotacion);
        if (votacion == null) {
            System.out.println("No se encontró ninguna votación con el ID especificado.");
            return;
        }


        System.out.println("Votación: " + votacion.getPregunta());
        List<String> opciones = votacion.getOpciones();
        boolean confirmado = false;


        while (!confirmado) {
            for (int i = 0; i < opciones.size(); i++) {
                System.out.println((i + 1) + ". " + opciones.get(i));
            }


            System.out.println("Seleccione el número de la opción por la que desea votar:");
            int eleccion = leerEntero("Ingrese el número de la opción deseada: ") - 1;
            if (eleccion < 0 || eleccion >= opciones.size()) {
                System.out.println("Selección inválida. Por favor, elija una opción válida.");
                continue;
            }


            System.out.println("Ha seleccionado: " + opciones.get(eleccion));
            System.out.println("¿Está seguro de su elección? 1 para Sí, 2 para No:");
            int confirmacion = leerEntero("Ingrese el número de su elección: ");
            if (confirmacion == 1) {
                controladorVotaciones.registrarVoto(idVotacion, usuario.getMatricula(), opciones.get(eleccion));
                System.out.println("Su voto ha sido registrado.");
                confirmado = true;
            } else if (confirmacion == 2) {
                System.out.println("Votación no confirmada. Seleccione nuevamente una opción.");
            } else {
                System.out.println("Opción inválida. Por favor, elija 1 para Sí o 2 para No.");
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
