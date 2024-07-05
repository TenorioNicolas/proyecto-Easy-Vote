package datos;

import dominio.Usuario;
import ventanas.*;

import javax.swing.*;

public class ControladorFrame {
    private GestorUsuarios gestorUsuarios;
    private ControladorVotaciones controladorVotaciones;

    public ControladorFrame() {
        this.gestorUsuarios = new GestorUsuarios();
        GestorVotos gestorVotos = new GestorVotos();
        this.controladorVotaciones = new ControladorVotaciones(gestorVotos);
    }

    // Devuelve el controlador de votaciones asociado a este controlador de frames
    public ControladorVotaciones getControladorVotaciones() {
        return controladorVotaciones;
    }

    // Autentica un usuario con nombre de usuario y contraseña, y maneja la sesión del usuario
    public void autenticarUsuario(String username, String password, JFrame frame) {
        Usuario usuario = gestorUsuarios.validarUsuario(username, password);
        if (usuario != null) {
            JOptionPane.showMessageDialog(frame, "Bienvenido, " + usuario.getNombre() + "!");
            frame.dispose(); // Cierra la ventana de autenticación
            mostrarMainMenu(usuario); // Abre el menú principal para el usuario autenticado
        } else {
            JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Muestra el menú principal ajustado al rol del usuario
    private void mostrarMainMenu(Usuario usuario) {
        MainMenuFrame mainMenuFrame = new MainMenuFrame(this, usuario);
        mainMenuFrame.setVisible(true);
    }

    // Abre la ventana para crear nuevas votaciones, sólo si el usuario tiene el rol de "Creador"
    public void mostrarCrearVotacionFrame(Usuario usuario) {
        if (usuario.getRol().equals("Creador")) {
            CrearVotacionFrame crearVotacionFrame = new CrearVotacionFrame(this);
            crearVotacionFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No tienes permiso para crear votaciones", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Muestra la ventana para votar en votaciones disponibles
    public void mostrarVotarFrame(Usuario usuario) {
        VotarFrame votarFrame = new VotarFrame(getControladorVotaciones(), usuario);
        votarFrame.setVisible(true);
    }

    // Muestra los resultados de votaciones
    public void mostrarResultadosFrame(Usuario usuario) {
        VerResultadosFrame verResultadosFrame = new VerResultadosFrame(getControladorVotaciones());
        verResultadosFrame.setVisible(true);
    }

    // Muestra la interfaz para deshabilitar votaciones, nuevamente solo accesible para usuarios con rol "Creador"
    public void mostrarDeshabilitarVotacionFrame(Usuario usuario) {
        if (usuario.getRol().equals("Creador")) {
            DeshabilitarVotacionFrame disableVotingFrame = new DeshabilitarVotacionFrame(getControladorVotaciones());
            disableVotingFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No tienes permiso para deshabilitar votaciones", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
        }
    }
}