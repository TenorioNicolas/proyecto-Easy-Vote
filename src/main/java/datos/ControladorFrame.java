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
            mostrarMainMenu(usuario); // Abre el menú principal según el rol del usuario
        } else {
            JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarMainMenu(Usuario usuario) {
        MainMenuFrame mainMenuFrame = new MainMenuFrame(this, usuario);
        mainMenuFrame.setVisible(true);
    }

    public void mostrarCrearVotacionFrame(Usuario usuario) {
        if (usuario.getRol().equals("Creador")) {
            CrearVotacionFrame crearVotacionFrame = new CrearVotacionFrame(this);
            crearVotacionFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No tienes permiso para crear votaciones", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Añadir el método mostrarVotarFrame ajustado en ControladorFrame
    public void mostrarVotarFrame(Usuario usuario) {
        VotarFrame votarFrame = new VotarFrame(getControladorVotaciones(), usuario);
        votarFrame.setVisible(true);
    }

    // Y ajusta mostrarResultadosFrame si es necesario
    public void mostrarResultadosFrame(Usuario usuario) {
        // Implementar si necesario, de acuerdo con las funcionalidades específicas para mostrar resultados
    }
}