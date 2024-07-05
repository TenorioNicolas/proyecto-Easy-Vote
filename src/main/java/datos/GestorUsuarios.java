package datos;

import dominio.Usuario;  // Importación de la clase Usuario para poder utilizar objetos Usuario.
import java.io.*;  // Importación de clases de entrada/salida para manejar la lectura de archivos.
import java.util.*;  // Importación de utilidades de Java, incluyendo Map y HashMap para gestionar los usuarios.

// Clase GestorUsuarios para manejar las operaciones relacionadas con los usuarios.
public class GestorUsuarios {
    // Mapa para almacenar usuarios utilizando su matrícula como clave.
    private Map<String, Usuario> usuarios = new HashMap<>();

    // Constructor que intenta cargar usuarios desde un archivo CSV al inicializar la instancia.
    public GestorUsuarios() {
        try {
            cargarUsuariosDesdeCSV("matriculas.csv");  // Intenta cargar usuarios desde el archivo especificado.
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            // Manejo de error en caso de que no se pueda cargar el archivo.
        }
    }

    // Método para validar un usuario usando matrícula y contraseña.
    public Usuario validarUsuario(String matricula, String contraseña) {
        Usuario usuario = usuarios.get(matricula);  // Obtiene el usuario del mapa por la matrícula.
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            return usuario;  // Retorna el usuario si la contraseña coincide.
        }
        return null;  // Retorna null si no encuentra el usuario o la contraseña no coincide.
    }

    // Método privado para cargar usuarios desde un archivo CSV.
    private void cargarUsuariosDesdeCSV(String nombreArchivo) throws IOException {
        File archivo = new File(nombreArchivo);  // Crea un objeto File para el archivo CSV.
        if (!archivo.exists()) {
            throw new FileNotFoundException("El archivo " + nombreArchivo + " no fue encontrado.");  // Lanza excepción si el archivo no existe.
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Salta la línea del encabezado del CSV.
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");  // Divide cada línea por comas para obtener los datos del usuario.
                if (datos.length >= 5) {
                    // Crea un nuevo usuario si la línea tiene todos los datos necesarios.
                    Usuario usuario = new Usuario(datos[0], datos[1], datos[2], datos[3], datos[4]);
                    usuarios.put(datos[0], usuario);  // Añade el usuario al mapa.
                } else {
                    // Log de error si la línea no contiene todos los datos esperados.
                    System.err.println("Línea incompleta o mal formada: " + linea);
                }
            }
        }
    }
}
