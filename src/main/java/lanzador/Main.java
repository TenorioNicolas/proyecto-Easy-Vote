package lanzador;

import ventanas.LoginFrame;
import datos.ControladorFrame;

// Clase Main que funciona como punto de entrada para la aplicación.
public class Main {
    public static void main(String[] args) {
        // Crear y mostrar la ventana de login usando SwingUtilities para asegurar la correcta inicialización en el Event Dispatch Thread.
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame(new ControladorFrame()).setVisible(true);
        });
    }
}
