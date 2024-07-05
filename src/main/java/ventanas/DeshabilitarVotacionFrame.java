package ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import datos.ControladorVotaciones;
import dominio.Votacion;
import java.util.List;

public class DeshabilitarVotacionFrame extends JFrame {
    private ControladorVotaciones controladorVotaciones;
    private JPanel panelDeCartas;
    private CardLayout cardLayout;

    public DeshabilitarVotacionFrame(ControladorVotaciones controlador) {
        this.controladorVotaciones = controlador;
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Deshabilitar Votaciones");
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
        List<Votacion> votacionesDisponibles = controladorVotaciones.getVotacionesActivas();
        if (votacionesDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay votaciones disponibles.", "Información", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Termina completamente la aplicación si no hay votaciones disponibles
            return;
        }

        JPanel panelListado = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (Votacion votacion : votacionesDisponibles) {
            JButton botonVotacion = new JButton(votacion.getNombre() + " (" + votacion.getId() + ")");
            botonVotacion.setPreferredSize(new Dimension(200, 30)); // Establecer el tamaño preferido para todos los botones
            botonVotacion.addActionListener(e -> mostrarDialogoConfirmacion(votacion));
            panelListado.add(botonVotacion, gbc);
        }

        panelDeCartas.add(panelListado, "Listado");
        cardLayout.show(panelDeCartas, "Listado");
    }

    private void mostrarDialogoConfirmacion(Votacion votacion) {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres deshabilitar la votación '" + votacion.getNombre() + "'?",
                "Confirmar deshabilitación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            controladorVotaciones.cambiarEstadoVotacion(votacion.getId(), false);
            JOptionPane.showMessageDialog(this, "La votación ha sido deshabilitada.", "Votación deshabilitada", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);  // Termina el programa completamente
        }
    }
}