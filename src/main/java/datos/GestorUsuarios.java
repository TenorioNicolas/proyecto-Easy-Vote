package datos;

import dominio.Usuario;
import java.io.*;
import java.util.*;

public class GestorUsuarios {
    private Map<String, Usuario> usuarios = new HashMap<>();

    public GestorUsuarios() {
        try {
            cargarUsuariosDesdeCSV("matriculas.csv");
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            // Puedes decidir cargar datos predeterminados o simplemente loguear el error
        }
    }

    public Usuario validarUsuario(String matricula, String contraseña) {
        Usuario usuario = usuarios.get(matricula);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            return usuario;
        }
        return null;
    }

    private void cargarUsuariosDesdeCSV(String nombreArchivo) throws IOException {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            throw new FileNotFoundException("El archivo " + nombreArchivo + " no fue encontrado.");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
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
        }
    }
}
