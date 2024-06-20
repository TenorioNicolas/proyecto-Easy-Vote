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
            System.out.println("3. Desactivar una votación");
            System.out.println("4. Salir");

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
                    if (controladorVotaciones.mostrarVotacionesDisponibles()) {
                        votarEnVotacionExistente(usuario);
                    } else {
                        System.out.println("Regresando al menú principal...");
                    }
                    break;
                case 3:
                    if (usuario.getRol().equals("Creador")) {
                        desactivarVotacion(usuario);
                    } else {
                        System.out.println("No tiene permisos para desactivar una votación.");
                    }
                    break;
                case 4:
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

        // Primero verificamos si el usuario ya ha votado en esta votación.
        if (!controladorVotaciones.usuarioHaVotado(idVotacion, usuario.getMatricula())) {
            Votacion votacion = controladorVotaciones.obtenerVotacionPorID(idVotacion);

            // Verificar si la votación existe y está activa antes de permitir votar.
            if (votacion != null) {
                if (!votacion.isActiva()) {
                    System.out.println("Votación ya vencida.");
                    return;
                }

                List<String> opciones = votacion.getOpciones();
                boolean confirmado = false;
                while (!confirmado) {
                    System.out.println("Votación: " + votacion.getPregunta());
                    for (int i = 0; i < opciones.size(); i++) {
                        System.out.println((i + 1) + ". " + opciones.get(i));
                    }
                    System.out.println("Seleccione el número de la opción por la que desea votar:");
                    int eleccion = leerEntero("Ingrese el número de la opción deseada: ") - 1;
                    if (eleccion >= 0 && eleccion < opciones.size()) {
                        System.out.println("Ha seleccionado: " + opciones.get(eleccion));
                        System.out.println("¿Está seguro de su elección? 1 para Sí, 2 para No");
                        int confirmacion = leerEntero("Ingrese su elección: ");
                        if (confirmacion == 1) {
                            controladorVotaciones.registrarVoto(idVotacion, usuario.getMatricula(), opciones.get(eleccion));
                            System.out.println("Has votado por: " + opciones.get(eleccion));
                            confirmado = true;
                        } else if (confirmacion == 2) {
                            System.out.println("Por favor, elija otra opción.");
                        } else {
                            System.out.println("Selección inválida. Intente de nuevo.");
                        }
                    } else {
                        System.out.println("Selección inválida. Intente de nuevo.");
                    }
                }
            } else {
                System.out.println("No se encontró ninguna votación con el ID especificado.");
            }
        } else {
            System.out.println("Ya has votado en esta votación.");
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

    private void desactivarVotacion(Usuario usuario) {
        // Primero mostramos todas las votaciones disponibles
        if (!controladorVotaciones.mostrarVotacionesDisponibles()) {
            System.out.println("No hay votaciones disponibles para desactivar.");
            return;
        }

        System.out.println("Ingrese el ID de la votación que desea desactivar:");
        String idVotacion = scanner.nextLine();
        Votacion votacion = controladorVotaciones.obtenerVotacionPorID(idVotacion);
        if (votacion != null) {
            if (!votacion.isActiva()) {
                System.out.println("Esta votación ya está inactiva.");
                return;
            }
            controladorVotaciones.cambiarEstadoVotacion(idVotacion, false);
            System.out.println("La votación ha sido desactivada exitosamente.");
        } else {
            System.out.println("No se encontró ninguna votación con el ID especificado.");
        }
    }

}