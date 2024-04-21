package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Map<String, String> usuariosContraseñas = new HashMap<>();
        usuariosContraseñas.put("123456789", "contraseña1");
        usuariosContraseñas.put("987654321", "contraseña2");

        ingresarUsuario(usuariosContraseñas);

    }
    public static void ingresarUsuario(Map<String, String> usuariosContraseñas) {
        Scanner scanner = new Scanner(System.in);
        boolean usuarioValido = false;

        while (!usuarioValido) {
            System.out.print("Ingrese el usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Ingrese la contraseña: ");
            String contraseña = scanner.nextLine();

            if (validarUsuario(usuario, contraseña, usuariosContraseñas)) {
                System.out.println("Usuario y contraseña válidos. Bienvenido.");
                usuarioValido = true;
            } else {
                System.out.println("Usuario o contraseña incorrectos. Inténtelo nuevamente.");
            }
        }
    }
    public static boolean validarUsuario(String usuario, String contraseña, Map<String, String> usuariosContraseñas) {
        String contraseñaAlmacenada = usuariosContraseñas.get(usuario);
        return contraseñaAlmacenada != null && contraseñaAlmacenada.equals(contraseña);
    }
}

