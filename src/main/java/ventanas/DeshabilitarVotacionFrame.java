package ventanas;

import javax.swing.*;
import java.awt.*;
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
        List<Votacion> votacionesDisponibles = controladorVotaciones.getVotaciones();
        if (votacionesDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay votaciones disponibles.", "Información", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cierra la ventana
            return;
        }

        JPanel panelListado = new JPanel(new GridLayout(0, 1, 5, 5));
        for (Votacion votacion : votacionesDisponibles) {
            JButton botonVotacion = new JButton(votacion.getNombre() + " (" + votacion.getId() + ")");
            botonVotacion.addActionListener(e -> mostrarDialogoConfirmacion(votacion));
            panelListado.add(botonVotacion);
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
            configurarPanelListado(); // Actualiza la lista de votaciones
        }
    }
}