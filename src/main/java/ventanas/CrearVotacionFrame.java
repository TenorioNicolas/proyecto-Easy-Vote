package ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import datos.ControladorFrame;

public class CrearVotacionFrame extends JFrame {
    private ControladorFrame controladorFrame;
    private JPanel tarjetas;
    private final static String PANEL_NOMBRE = "Tarjeta con nombre de votación";
    private final static String PANEL_PREGUNTA = "Tarjeta con pregunta de votación";
    private final static String PANEL_CANTIDAD_OPCIONES = "Tarjeta con cantidad de opciones";
    private final static String PANEL_AGREGAR_OPCIONES = "Tarjeta para agregar opciones";
    private final static String PANEL_PREVIA = "Tarjeta con vista previa";

    private JTextField campoNombreVotacion;
    private JTextField campoPregunta;
    private JComboBox<Integer> comboBoxCantidadOpciones;
    private List<JTextField> camposOpciones;

    public CrearVotacionFrame(ControladorFrame controladorFrame) {
        this.controladorFrame = controladorFrame;
        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        setTitle("Crear Nueva Votación");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tarjetas = new JPanel(new CardLayout());
        configurarPanelNombre();
        configurarPanelPregunta();
        configurarPanelCantidadOpciones();
        add(tarjetas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void configurarPanelNombre() {
        JPanel panelNombre = new JPanel();
        campoNombreVotacion = new JTextField(20);
        panelNombre.add(new JLabel("Nombre de la Votación:"));
        panelNombre.add(campoNombreVotacion);
        JButton botonSiguiente = new JButton("Siguiente");
        botonSiguiente.addActionListener(e -> {
            if (campoNombreVotacion.getText().trim().isEmpty()) {
                mostrarDialogoError("El nombre de la votación no puede estar vacío.");
            } else {
                mostrarTarjeta(PANEL_PREGUNTA);
            }
        });
        panelNombre.add(botonSiguiente);
        tarjetas.add(panelNombre, PANEL_NOMBRE);
    }

    private void configurarPanelPregunta() {
        JPanel panelPregunta = new JPanel();
        campoPregunta = new JTextField(20);
        panelPregunta.add(new JLabel("Pregunta:"));
        panelPregunta.add(campoPregunta);
        JButton botonSiguiente = new JButton("Siguiente");
        botonSiguiente.addActionListener(e -> {
            if (campoPregunta.getText().trim().isEmpty()) {
                mostrarDialogoError("La pregunta de la votación no puede estar vacía.");
            } else {
                mostrarTarjeta(PANEL_CANTIDAD_OPCIONES);
            }
        });
        panelPregunta.add(botonSiguiente);
        tarjetas.add(panelPregunta, PANEL_PREGUNTA);
    }

    private void configurarPanelCantidadOpciones() {
        JPanel panelCantidadOpciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCantidadOpciones.add(new JLabel("Seleccione la cantidad de opciones:"));
        Integer[] opcionesCantidad = IntStream.rangeClosed(2, 10).boxed().toArray(Integer[]::new);
        comboBoxCantidadOpciones = new JComboBox<>(opcionesCantidad);
        panelCantidadOpciones.add(comboBoxCantidadOpciones);
        JButton botonSiguiente = new JButton("Siguiente");
        botonSiguiente.addActionListener(e -> configurarYMostrarPanelAgregarOpciones());
        panelCantidadOpciones.add(botonSiguiente);
        tarjetas.add(panelCantidadOpciones, PANEL_CANTIDAD_OPCIONES);
    }

    private void configurarYMostrarPanelAgregarOpciones() {
        int cantidadOpciones = (Integer) comboBoxCantidadOpciones.getSelectedItem();
        JPanel panelAgregarOpciones = new JPanel();
        panelAgregarOpciones.setLayout(new BoxLayout(panelAgregarOpciones, BoxLayout.Y_AXIS));
        panelAgregarOpciones.add(new JLabel("Ingrese el contenido de las opciones:"));
        camposOpciones = new ArrayList<>();
        for (int i = 0; i < cantidadOpciones; i++) {
            JTextField campoOpcion = new JTextField(20);
            panelAgregarOpciones.add(new JLabel("Opción " + (i + 1) + ":"));
            panelAgregarOpciones.add(campoOpcion);
            camposOpciones.add(campoOpcion);
        }
        JButton botonVistaPrevia = new JButton("Vista Previa");
        botonVistaPrevia.addActionListener(this::mostrarVistaPrevia);
        panelAgregarOpciones.add(botonVistaPrevia);
        tarjetas.add(panelAgregarOpciones, PANEL_AGREGAR_OPCIONES);
        mostrarTarjeta(PANEL_AGREGAR_OPCIONES);
    }

    private void mostrarVistaPrevia(ActionEvent e) {
        boolean todosCamposValidos = true;
        // Validaciones de campos vacíos
        if (campoNombreVotacion.getText().trim().isEmpty()) {
            mostrarDialogoError("El nombre de la votación no puede estar vacío.");
            todosCamposValidos = false;
        }
        if (campoPregunta.getText().trim().isEmpty()) {
            mostrarDialogoError("La pregunta de la votación no puede estar vacía.");
            todosCamposValidos = false;
        }
        for (JTextField campoOpcion : camposOpciones) {
            if (campoOpcion.getText().trim().isEmpty()) {
                mostrarDialogoError("Todas las opciones deben contener texto.");
                todosCamposValidos = false;
                break;
            }
        }

        // Si todos los campos son válidos, se muestra la vista previa
        if (todosCamposValidos) {
            JPanel panelPrevia = new JPanel();
            panelPrevia.setLayout(new BoxLayout(panelPrevia, BoxLayout.Y_AXIS));
            panelPrevia.add(new JLabel("Vista previa de la Votación:"));
            panelPrevia.add(new JLabel("Nombre: " + campoNombreVotacion.getText()));
            panelPrevia.add(new JLabel("Pregunta: " + campoPregunta.getText()));
            camposOpciones.forEach(campo -> panelPrevia.add(new JLabel("Opción: " + campo.getText())));
            JButton botonCrear = new JButton("Crear");
            botonCrear.addActionListener(this::manejarCrearVotacion);
            JButton botonCancelar = new JButton("Cancelar");
            botonCancelar.addActionListener(ev -> {
                JOptionPane.showMessageDialog(this, "Creación de votación cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0); // Termina la aplicación
            });
            panelPrevia.add(botonCrear);
            panelPrevia.add(botonCancelar);
            tarjetas.add(panelPrevia, PANEL_PREVIA);
            mostrarTarjeta(PANEL_PREVIA);
        }
    }

    private void manejarCrearVotacion(ActionEvent e) {
        List<String> opciones = new ArrayList<>();
        camposOpciones.forEach(campo -> opciones.add(campo.getText().trim()));
        controladorFrame.getControladorVotaciones().crearVotacion(campoNombreVotacion.getText().trim(), campoPregunta.getText().trim(), opciones);
        JOptionPane.showMessageDialog(this, "Votación '" + campoNombreVotacion.getText() + "' creada con éxito!");
        System.exit(0);  // Termina completamente la aplicación después de mostrar el mensaje
    }


    private void mostrarDialogoError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarTarjeta(String nombreTarjeta) {
        CardLayout cl = (CardLayout) (tarjetas.getLayout());
        cl.show(tarjetas, nombreTarjeta);
    }
}