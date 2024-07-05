package lanzador;

import ventanas.LoginFrame;
import datos.GestorUsuarios;
import datos.GestorVotos;
import datos.ControladorVotaciones;
import datos.ControladorFrame;

public class Main {
    public static void main(String[] args) {
        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        GestorVotos gestorVotos = new GestorVotos();
        ControladorVotaciones controladorVotaciones = new ControladorVotaciones(gestorVotos);
        ControladorFrame controladorFrame = new ControladorFrame(gestorUsuarios, controladorVotaciones);

        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(controladorFrame);
            loginFrame.setVisible(true);
        });
    }
}
