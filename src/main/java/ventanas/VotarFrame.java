package ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import datos.ControladorVotaciones;
import dominio.Votacion;
import dominio.Usuario;

public class VotarFrame extends JFrame {
    private ControladorVotaciones controladorVotaciones;
    private JPanel panelDeCartas;
    private CardLayout cardLayout;
    private Usuario usuario;

    public VotarFrame(ControladorVotaciones controlador, Usuario usuario) {
        this.controladorVotaciones = controlador;
        this.usuario = usuario;
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Votaciones Disponibles");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelDeCartas = new JPanel(cardLayout);
        configurarPanelListado();

        add(panelDeCartas);
        setVisible(true);
    }

    private void configurarPanelListado() {
        JPanel panelListado = new JPanel(new GridLayout(0, 1, 5, 5));
        for (Votacion votacion : controladorVotaciones.getVotaciones()) {
            JButton botonVotacion = new JButton(votacion.getNombre() + " (" + votacion.getId() + ")");
            botonVotacion.addActionListener(e -> configurarPanelDetalle(votacion));
            panelListado.add(botonVotacion);
        }
        panelDeCartas.add(panelListado, "Listado");
        cardLayout.show(panelDeCartas, "Listado");
    }

    private void configurarPanelDetalle(Votacion votacion) {
        JPanel panelDetalle = new JPanel();
        panelDetalle.setLayout(new BoxLayout(panelDetalle, BoxLayout.Y_AXIS));

        panelDetalle.add(new JLabel("Nombre: " + votacion.getNombre()));
        panelDetalle.add(new JLabel("Pregunta: " + votacion.getPregunta()));

        votacion.getOpciones().forEach(opcion -> {
            JButton opcionButton = new JButton(opcion);
            opcionButton.addActionListener(e -> {
                if (controladorVotaciones.getGestorVotos().usuarioHaVotado(votacion.getId(), usuario.getMatricula())) {
                    JOptionPane.showMessageDialog(this, "Ya has votado en esta votación.");
                } else {
                    confirmarVoto(votacion, opcion);
                }
            });
            panelDetalle.add(opcionButton);
        });

        JButton botonVolver = new JButton("Volver");
        botonVolver.addActionListener(e -> cardLayout.show(panelDeCartas, "Listado"));
        panelDetalle.add(botonVolver);

        panelDeCartas.add(panelDetalle, votacion.getId());
        cardLayout.show(panelDeCartas, votacion.getId());
    }

    private void confirmarVoto(Votacion votacion, String opcionSeleccionada) {
        int resultado = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres votar por " + opcionSeleccionada + "?",
                "Confirmación de voto",
                JOptionPane.YES_NO_OPTION);

        if (resultado == JOptionPane.YES_OPTION) {
            controladorVotaciones.getGestorVotos().registrarVoto(votacion.getId(), usuario.getMatricula(), opcionSeleccionada);
            JOptionPane.showMessageDialog(this, "Voto registrado con éxito para: " + opcionSeleccionada);
            cardLayout.show(panelDeCartas, "Listado");
        }
    }
}
