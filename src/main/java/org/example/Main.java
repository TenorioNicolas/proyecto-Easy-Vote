package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String[] usuarios = {"123456789", "987654321"};
        String[] contrasenas = {"contraseña1", "contraseña2"};

        ingresarUsuario(usuarios, contrasenas);

    }
    public static void ingresarUsuario(String[] usuarios, String[] contrasenas) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Ingrese la contraseña: ");
        String contrasena = scanner.nextLine();

        if (validarUsuario(usuario, contrasena, usuarios, contrasenas)) {
            System.out.println("Usuario y contraseña válidos. Bienvenido.");
        } else {
            System.out.println("Usuario o contraseña incorrectos.");
        }
    }
    public static boolean validarUsuario(String usuario, String contrasena, String[] usuarios, String[] contrasenas) {
        for (int i = 0; i < usuarios.length; i++) {
            if (usuarios[i].equals(usuario) && contrasenas[i].equals(contrasena)) {
                return true;
            }
        }
        return false;
    }
}

