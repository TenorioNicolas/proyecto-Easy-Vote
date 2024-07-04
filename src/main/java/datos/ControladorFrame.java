package datos;

import dominio.Usuario;
import ventanas.*;
import javax.swing.*;

public class ControladorFrame {
    private GestorUsuarios gestorUsuarios;
    private ControladorVotaciones controladorVotaciones;

    public ControladorFrame(GestorUsuarios gestorUsuarios, ControladorVotaciones controladorVotaciones) {
        this.gestorUsuarios = gestorUsuarios;
        this.controladorVotaciones = controladorVotaciones;
    }

    public ControladorVotaciones getControladorVotaciones() {
        return controladorVotaciones;
    }

    public void autenticarUsuario(String username, String password, JFrame frame) {
        Usuario usuario = gestorUsuarios.validarUsuario(username, password);
        if (usuario != null) {
            JOptionPane.showMessageDialog(frame, "Bienvenido, " + usuario.getNombre() + "!");
            frame.dispose(); // Cierra la ventana de login
            mostrarMainMenu(usuario); // Abre el menú principal
        } else {
            JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarMainMenu(Usuario usuario) {
        MainMenuFrame mainMenuFrame = new MainMenuFrame(this, usuario);
        mainMenuFrame.setVisible(true);
    }

    public void mostrarCrearVotacionFrame() {
        CrearVotacionFrame crearVotacionFrame = new CrearVotacionFrame(this);
        crearVotacionFrame.setVisible(true);
    }

    public void mostrarVotarFrame() {
        VotarFrame votarFrame = new VotarFrame(getControladorVotaciones());
        votarFrame.setVisible(true);
    }

    public void mostrarResultadosFrame() {
    }
}
