package lanzador;

import datos.ControladorFrame;
import datos.ControladorVotaciones;
import datos.GestorUsuarios;
import ventanas.LoginFrame;

public class Main {
    public static void main(String[] args) {
        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        ControladorVotaciones controladorVotaciones = new ControladorVotaciones();

        ControladorFrame controladorFrame = new ControladorFrame(gestorUsuarios, controladorVotaciones);

        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(controladorFrame);
            loginFrame.setVisible(true);
        });
    }
}
