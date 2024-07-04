package ventanas;

import datos.ControladorFrame;
import dominio.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    private Usuario usuario;
    private ControladorFrame controladorFrame;

    public MainMenuFrame(ControladorFrame controladorFrame, Usuario usuario) {
        this.controladorFrame = controladorFrame;
        this.usuario = usuario;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Menú Principal - Easy Vote");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));  // 4 rows, 1 column, with padding

        JLabel welcomeLabel = new JLabel("BIENVENIDO, " + usuario.getNombre().toUpperCase() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton createVoteButton = new JButton("Crear Votación");
        JButton voteButton = new JButton("Votar");
        JButton resultsButton = new JButton("Ver resultados");

        // Añadir la acción correspondiente al botón con control de acceso según el rol
        createVoteButton.addActionListener(e -> controladorFrame.mostrarCrearVotacionFrame(usuario));
        voteButton.addActionListener(e -> controladorFrame.mostrarVotarFrame(usuario));  // Modificado para pasar el usuario
        resultsButton.addActionListener(e -> controladorFrame.mostrarResultadosFrame(usuario));  // Añadido usuario si necesario

        add(welcomeLabel);
        add(createVoteButton);
        add(voteButton);
        add(resultsButton);

        // Gestionar visibilidad de los botones según el rol del usuario
        manageButtonVisibility(usuario.getRol());

        setVisible(true);
    }

    private void manageButtonVisibility(String role) {
        if (role.equals("Votante")) {
            // Si el usuario es solo un votante, oculta la opción de crear votaciones
            getContentPane().getComponent(1).setVisible(false);
        }
    }
}
