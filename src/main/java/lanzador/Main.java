package lanzador;

import datos.ControladorFrame;
import datos.ControladorVotaciones;
import datos.GestorUsuarios;
import datos.GestorVotos; // Asegúrate de importar GestorVotos
import ventanas.LoginFrame;

public class Main {
    public static void main(String[] args) {
        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        GestorVotos gestorVotos = new GestorVotos(); // Crea una instancia de GestorVotos
        ControladorVotaciones controladorVotaciones = new ControladorVotaciones(gestorVotos); // Pasa gestorVotos al constructor

        ControladorFrame controladorFrame = new ControladorFrame(gestorUsuarios, controladorVotaciones);

        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(controladorFrame);
            loginFrame.setVisible(true);
        });
    }
}
