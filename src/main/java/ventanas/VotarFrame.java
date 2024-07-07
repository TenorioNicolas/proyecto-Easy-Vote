package ventanas;

import javax.swing.*;
import java.awt.*;
import datos.ControladorVotaciones;
import dominio.Usuario;
import dominio.Votacion;
import java.util.List;

// Clase VotarFrame proporciona una interfaz para que los usuarios elijan y voten en votaciones activas.
public class VotarFrame extends JFrame {
    private ControladorVotaciones controladorVotaciones; // Controlador para interactuar con la lógica de votaciones.
    private JPanel panelDeCartas; // Panel que usa CardLayout para alternar entre listado de votaciones y detalle de votación.
    private CardLayout cardLayout; // Gestiona la navegación entre los paneles de listado y detalle.
    private Usuario usuario; // Usuario actual que está utilizando la interfaz.

    // Constructor que inicializa la ventana con el controlador de votaciones y el usuario.
    public VotarFrame(ControladorVotaciones controlador, Usuario usuario) {
        this.controladorVotaciones = controlador;
        this.usuario = usuario;
        inicializarUI();
    }

    // Método para configurar la interfaz de usuario inicial, incluyendo tamaño, disposición y visibilidad.
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

    // Configura el panel que lista las votaciones disponibles.
    private void configurarPanelListado() {
        List<Votacion> votacionesDisponibles = controladorVotaciones.getVotaciones();
        if (votacionesDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay votaciones disponibles.", "Información", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Termina la aplicación si no hay votaciones disponibles.
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
            botonVotacion.setPreferredSize(new Dimension(200, 30)); // Establece el tamaño preferido para los botones.
            botonVotacion.addActionListener(e -> configurarPanelDetalle(votacion));
            panelListado.add(botonVotacion, gbc);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton botonSalir = new JButton("Salir");
        botonSalir.addActionListener(e -> System.exit(0));
        bottomPanel.add(botonSalir);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelListado, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        panelDeCartas.add(mainPanel, "Listado");
        cardLayout.show(panelDeCartas, "Listado");
    }

    // Configura el panel de detalle para una votación específica.
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
            cardLayout.show(panelDeCartas, "Listado"); // Retorna al listado si la votación está inactiva.
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonVolver = new JButton("Volver");
        botonVolver.setPreferredSize(new Dimension(200, 30));
        botonVolver.addActionListener(e -> cardLayout.show(panelDeCartas, "Listado"));
        bottomPanel.add(botonVolver);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelDetalle, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        panelDeCartas.add(mainPanel, votacion.getId());
        cardLayout.show(panelDeCartas, votacion.getId());
    }

    // Maneja la confirmación y registro del voto del usuario para una opción específica.
    private void confirmarVoto(Votacion votacion, String opcionSeleccionada) {
        int resultado = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres votar por " + opcionSeleccionada + "?",
                "Confirmación de voto",
                JOptionPane.YES_NO_OPTION);

        if (resultado == JOptionPane.YES_OPTION) {
            controladorVotaciones.getGestorVotos().registrarVoto(votacion.getId(), usuario.getMatricula(), opcionSeleccionada);
            JOptionPane.showMessageDialog(this, "Voto registrado con éxito para: " + opcionSeleccionada);
            System.exit(0); // Termina la aplicación después de registrar el voto.
        }
    }
}
