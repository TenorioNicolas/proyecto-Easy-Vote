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

        createVoteButton.addActionListener(e -> controladorFrame.mostrarCrearVotacionFrame());
        voteButton.addActionListener(e -> controladorFrame.mostrarVotarFrame());
        resultsButton.addActionListener(e -> controladorFrame.mostrarResultadosFrame());

        add(welcomeLabel);
        add(createVoteButton);
        add(voteButton);
        add(resultsButton);

        setVisible(true);
    }
}
