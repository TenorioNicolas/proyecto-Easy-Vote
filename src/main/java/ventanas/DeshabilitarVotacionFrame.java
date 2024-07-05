package ventanas;

import javax.swing.*;  // Importación de componentes de Swing para la interfaz gráfica.
import java.awt.*;     // Herramientas de AWT para gestionar componentes y eventos.
import java.awt.event.ActionEvent;  // Importación para manejar eventos de acción como clics de botón.
import datos.ControladorVotaciones;  // Importa la clase que controla la lógica de negocio para las votaciones.
import dominio.Votacion;  // Importa la clase del dominio que representa una votación.
import java.util.List;  // Utilizado para manejar listas de objetos, en este caso, listas de votaciones.

// Clase que extiende JFrame para crear la ventana de deshabilitación de votaciones.
public class DeshabilitarVotacionFrame extends JFrame {
    private ControladorVotaciones controladorVotaciones;  // Controlador para interactuar con la lógica de las votaciones.
    private JPanel panelDeCartas;  // Panel que utiliza CardLayout para mostrar diferentes paneles de forma dinámica.
    private CardLayout cardLayout;  // Gestor de diseño para cambiar entre diferentes paneles.

    // Constructor que inicializa el controlador y la interfaz gráfica.
    public DeshabilitarVotacionFrame(ControladorVotaciones controlador) {
        this.controladorVotaciones = controlador;
        inicializarUI();
    }

    // Método para configurar la interfaz gráfica inicial del marco.
    private void inicializarUI() {
        setTitle("Deshabilitar Votaciones");  // Título de la ventana.
        setSize(500, 400);  // Dimensiones de la ventana.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Operación al cerrar la ventana.
        setLocationRelativeTo(null);  // Centra la ventana en la pantalla.

        cardLayout = new CardLayout();
        panelDeCartas = new JPanel(cardLayout);  // Inicialización del panel con CardLayout.
        configurarPanelListado();  // Configura el panel con la lista de votaciones.

        add(panelDeCartas);  // Añade el panel de cartas al frame.
        setVisible(true);  // Hace visible el frame.
    }

    // Configura el panel que lista las votaciones que se pueden deshabilitar.
    private void configurarPanelListado() {
        List<Votacion> votacionesDisponibles = controladorVotaciones.getVotacionesActivas();  // Obtiene las votaciones activas.
        if (votacionesDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay votaciones disponibles para deshabilitar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Si no hay votaciones, termina la aplicación.
            return;
        }

        JPanel panelListado = new JPanel(new GridBagLayout());  // Usa GridBagLayout para un diseño flexible.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 5, 5, 5);  // Márgenes para los componentes.

        // Añade botones para cada votación disponible.
        for (Votacion votacion : votacionesDisponibles) {
            JButton botonVotacion = new JButton(votacion.getNombre() + " (" + votacion.getId() + ")");
            botonVotacion.setPreferredSize(new Dimension(200, 30));
            botonVotacion.addActionListener(e -> mostrarDialogoConfirmacion(votacion));
            panelListado.add(botonVotacion, gbc);
        }

        // Panel inferior con un botón de salida.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonSalir = new JButton("Salir");
        botonSalir.addActionListener(e -> System.exit(0));  // Acción para cerrar la aplicación.
        bottomPanel.add(botonSalir);

        // Añade los paneles al panel principal usando BorderLayout para la estructura.
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelListado, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        panelDeCartas.add(mainPanel, "Listado");
        cardLayout.show(panelDeCartas, "Listado");  // Muestra el panel listado inicialmente.
    }

    // Muestra un diálogo de confirmación para deshabilitar una votación.
    private void mostrarDialogoConfirmacion(Votacion votacion) {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres deshabilitar la votación '" + votacion.getNombre() + "'?",
                "Confirmar deshabilitación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            controladorVotaciones.cambiarEstadoVotacion(votacion.getId(), false);  // Cambia el estado de la votación.
            JOptionPane.showMessageDialog(this, "La votación ha sido deshabilitada.", "Votación deshabilitada", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);  // Termina el programa completamente tras deshabilitar la votación.
        }
    }
}