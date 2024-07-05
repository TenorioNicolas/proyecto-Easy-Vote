package ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import datos.ControladorVotaciones;
import dominio.Votacion;
import dominio.Usuario;
import java.util.List;

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
        List<Votacion> votacionesDisponibles = controladorVotaciones.getVotaciones();
        if (votacionesDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay votaciones disponibles.", "Información", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Termina completamente la aplicación
            return;
        }

        JPanel panelListado = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titulo = new JLabel("Votaciones Disponibles", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelListado.add(titulo, gbc);

        for (Votacion votacion : votacionesDisponibles) {
            JButton botonVotacion = new JButton(votacion.getNombre() + " (" + votacion.getId() + ")");
            botonVotacion.setPreferredSize(new Dimension(200, 30)); // Establecer el tamaño preferido para todos los botones
            botonVotacion.addActionListener(e -> configurarPanelDetalle(votacion));
            panelListado.add(botonVotacion, gbc);
        }

        panelDeCartas.add(panelListado, "Listado");
        cardLayout.show(panelDeCartas, "Listado");
    }

    private void configurarPanelDetalle(Votacion votacion) {
        JPanel panelDetalle = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nombreLabel = new JLabel("Nombre: " + votacion.getNombre());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel preguntaLabel = new JLabel("Pregunta: " + votacion.getPregunta());
        preguntaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panelDetalle.add(nombreLabel, gbc);
        panelDetalle.add(preguntaLabel, gbc);

        if (!votacion.isActiva()) {
            JOptionPane.showMessageDialog(this, "Esta votación está inactiva.", "Votación Inactiva", JOptionPane.WARNING_MESSAGE);
            cardLayout.show(panelDeCartas, "Listado"); // Retorna al listado después de mostrar el mensaje
            return;
        }

        votacion.getOpciones().forEach(opcion -> {
            JButton opcionButton = new JButton(opcion);
            opcionButton.setPreferredSize(new Dimension(200, 30));
            opcionButton.addActionListener(e -> {
                if (controladorVotaciones.getGestorVotos().usuarioHaVotado(votacion.getId(), usuario.getMatricula())) {
                    JOptionPane.showMessageDialog(this, "Ya has votado en esta votación.");
                } else {
                    confirmarVoto(votacion, opcion);
                }
            });
            panelDetalle.add(opcionButton, gbc);
        });

        JButton botonVolver = new JButton("Volver");
        botonVolver.setPreferredSize(new Dimension(200, 30));
        botonVolver.addActionListener(e -> cardLayout.show(panelDeCartas, "Listado"));
        panelDetalle.add(botonVolver, gbc);

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
            System.exit(0); // Termina completamente la aplicación
        }
    }
}