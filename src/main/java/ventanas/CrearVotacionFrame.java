package ventanas;

import javax.swing.*;  // Importa todos los componentes Swing para la interfaz gráfica de usuario.
import java.awt.*;     // Importa herramientas del Abstract Window Toolkit para manejar elementos gráficos básicos.
import java.awt.event.ActionEvent;  // Importa la clase para manejar eventos de acción.
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;  // Utilizado para crear secuencias de números.
import datos.ControladorFrame;  // Importa la clase que maneja la lógica de interacción con otras partes del sistema.

// Clase que proporciona la interfaz gráfica para crear una nueva votación.
public class CrearVotacionFrame extends JFrame {
    private ControladorFrame controladorFrame;  // Maneja la lógica del negocio relacionada con las votaciones.
    private JPanel tarjetas;  // Panel que contiene las diferentes "tarjetas" o pantallas.
    private static final String PANEL_NOMBRE = "Tarjeta con nombre de votación";
    private static final String PANEL_PREGUNTA = "Tarjeta con pregunta de votación";
    private static final String PANEL_CANTIDAD_OPCIONES = "Tarjeta con cantidad de opciones";
    private static final String PANEL_AGREGAR_OPCIONES = "Tarjeta para agregar opciones";
    private static final String PANEL_PREVIA = "Tarjeta con vista previa";

    private JTextField campoNombreVotacion;  // Campo de texto para el nombre de la votación.
    private JTextField campoPregunta;  // Campo de texto para la pregunta de la votación.
    private JComboBox<Integer> comboBoxCantidadOpciones;  // ComboBox para seleccionar la cantidad de opciones.
    private List<JTextField> camposOpciones;  // Lista de campos de texto para las opciones de la votación.

    // Constructor que inicializa la interfaz gráfica y la lógica de negocio.
    public CrearVotacionFrame(ControladorFrame controladorFrame) {
        this.controladorFrame = controladorFrame;
        inicializarInterfaz();
    }

    // Método para configurar la ventana y los componentes iniciales.
    private void inicializarInterfaz() {
        setTitle("Crear Nueva Votación");  // Título de la ventana.
        setSize(500, 400);  // Tamaño de la ventana.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Operación de cierre (cerrar ventana sin salir de la aplicación).
        setLocationRelativeTo(null);  // Centrar ventana en la pantalla.
        setLayout(new BorderLayout());  // Establecer el gestor de diseño BorderLayout.

        tarjetas = new JPanel(new CardLayout());  // Panel para contener los diferentes paneles de la interfaz.
        configurarPanelNombre();  // Configurar el panel para introducir el nombre de la votación.
        configurarPanelPregunta();  // Configurar el panel para introducir la pregunta.
        configurarPanelCantidadOpciones();  // Configurar el panel para seleccionar la cantidad de opciones.
        add(tarjetas, BorderLayout.CENTER);  // Añadir el panel de tarjetas al centro de la ventana.
        setVisible(true);  // Hacer visible la ventana.
    }

    // Método para configurar el panel donde se introduce el nombre de la votación.
    private void configurarPanelNombre() {
        JPanel panelNombre = new JPanel();  // Crear un panel para el nombre.
        campoNombreVotacion = new JTextField(20);  // Campo de texto para el nombre.
        panelNombre.add(new JLabel("Nombre de la Votación:"));  // Etiqueta para el campo.
        panelNombre.add(campoNombreVotacion);  // Añadir el campo de texto al panel.
        JButton botonSiguiente = new JButton("Siguiente");  // Botón para continuar al siguiente paso.
        botonSiguiente.addActionListener(e -> {
            if (campoNombreVotacion.getText().trim().isEmpty()) {
                mostrarDialogoError("El nombre de la votación no puede estar vacío.");  // Validar que el campo no esté vacío.
            } else {
                mostrarTarjeta(PANEL_PREGUNTA);  // Mostrar el siguiente panel si el campo no está vacío.
            }
        });
        panelNombre.add(botonSiguiente);  // Añadir el botón al panel.
        tarjetas.add(panelNombre, PANEL_NOMBRE);  // Añadir el panel al panel de tarjetas.
    }

    // Método para configurar el panel donde se introduce la pregunta de la votación.
    private void configurarPanelPregunta() {
        JPanel panelPregunta = new JPanel();  // Crear un panel para la pregunta.
        campoPregunta = new JTextField(20);  // Campo de texto para la pregunta.
        panelPregunta.add(new JLabel("Pregunta:"));  // Etiqueta para el campo.
        panelPregunta.add(campoPregunta);  // Añadir el campo de texto al panel.
        JButton botonSiguiente = new JButton("Siguiente");  // Botón para continuar al siguiente paso.
        botonSiguiente.addActionListener(e -> {
            if (campoPregunta.getText().trim().isEmpty()) {
                mostrarDialogoError("La pregunta de la votación no puede estar vacía.");  // Validar que el campo no esté vacío.
            } else {
                mostrarTarjeta(PANEL_CANTIDAD_OPCIONES);  // Mostrar el siguiente panel si el campo no está vacío.
            }
        });
        panelPregunta.add(botonSiguiente);  // Añadir el botón al panel.
        tarjetas.add(panelPregunta, PANEL_PREGUNTA);  // Añadir el panel al panel de tarjetas.
    }

    // Método para configurar el panel donde se selecciona la cantidad de opciones para la votación.
    private void configurarPanelCantidadOpciones() {
        JPanel panelCantidadOpciones = new JPanel(new FlowLayout(FlowLayout.LEFT));  // Crear un panel con FlowLayout para la alineación.
        panelCantidadOpciones.add(new JLabel("Seleccione la cantidad de opciones:"));  // Etiqueta para el comboBox.
        Integer[] opcionesCantidad = IntStream.rangeClosed(2, 10).boxed().toArray(Integer[]::new);  // Rango de opciones para el comboBox.
        comboBoxCantidadOpciones = new JComboBox<>(opcionesCantidad);  // ComboBox para seleccionar la cantidad de opciones.
        panelCantidadOpciones.add(comboBoxCantidadOpciones);  // Añadir el comboBox al panel.
        JButton botonSiguiente = new JButton("Siguiente");  // Botón para continuar al siguiente paso.
        botonSiguiente.addActionListener(e -> configurarYMostrarPanelAgregarOpciones());  // Mostrar el panel para agregar opciones.
        panelCantidadOpciones.add(botonSiguiente);  // Añadir el botón al panel.
        tarjetas.add(panelCantidadOpciones, PANEL_CANTIDAD_OPCIONES);  // Añadir el panel al panel de tarjetas.
    }

    // Método para configurar y mostrar el panel donde se agregan las opciones de la votación.
    private void configurarYMostrarPanelAgregarOpciones() {
        int cantidadOpciones = (Integer) comboBoxCantidadOpciones.getSelectedItem();  // Obtener la cantidad seleccionada de opciones.
        JPanel panelAgregarOpciones = new JPanel();  // Crear un panel para agregar las opciones.
        panelAgregarOpciones.setLayout(new BoxLayout(panelAgregarOpciones, BoxLayout.Y_AXIS));  // Usar BoxLayout para alinear los componentes verticalmente.
        panelAgregarOpciones.add(new JLabel("Ingrese el contenido de las opciones:"));  // Etiqueta para los campos de texto.
        camposOpciones = new ArrayList<>();  // Lista para almacenar los campos de texto de las opciones.
        for (int i = 0; i < cantidadOpciones; i++) {
            JTextField campoOpcion = new JTextField(20);  // Campo de texto para cada opción.
            panelAgregarOpciones.add(new JLabel("Opción " + (i + 1) + ":"));  // Etiqueta para cada campo de texto.
            panelAgregarOpciones.add(campoOpcion);  // Añadir el campo de texto al panel.
            camposOpciones.add(campoOpcion);  // Añadir el campo de texto a la lista.
        }
        JButton botonVistaPrevia = new JButton("Vista Previa");  // Botón para mostrar la vista previa de la votación.
        botonVistaPrevia.addActionListener(this::mostrarVistaPrevia);  // Acción para mostrar la vista previa.
        panelAgregarOpciones.add(botonVistaPrevia);  // Añadir el botón al panel.
        tarjetas.add(panelAgregarOpciones, PANEL_AGREGAR_OPCIONES);  // Añadir el panel al panel de tarjetas.
        mostrarTarjeta(PANEL_AGREGAR_OPCIONES);  // Mostrar el panel de agregar opciones.
    }

    // Método para mostrar la vista previa de la votación y validar los campos.
    private void mostrarVistaPrevia(ActionEvent e) {
        boolean todosCamposValidos = true;  // Variable para verificar si todos los campos son válidos.
        // Validaciones de campos vacíos
        if (campoNombreVotacion.getText().trim().isEmpty()) {
            mostrarDialogoError("El nombre de la votación no puede estar vacío.");  // Mostrar error si el nombre está vacío.
            todosCamposValidos = false;  // Indicar que hay un campo inválido.
        }
        if (campoPregunta.getText().trim().isEmpty()) {
            mostrarDialogoError("La pregunta de la votación no puede estar vacía.");  // Mostrar error si la pregunta está vacía.
            todosCamposValidos = false;  // Indicar que hay un campo inválido.
        }
        for (JTextField campoOpcion : camposOpciones) {
            if (campoOpcion.getText().trim().isEmpty()) {
                mostrarDialogoError("Todas las opciones deben contener texto.");  // Mostrar error si alguna opción está vacía.
                todosCamposValidos = false;  // Indicar que hay un campo inválido.
                break;  // Salir del bucle si se encuentra un campo vacío.
            }
        }

        // Si todos los campos son válidos, se muestra la vista previa
        if (todosCamposValidos) {
            JPanel panelPrevia = new JPanel();  // Crear un panel para la vista previa.
            panelPrevia.setLayout(new BoxLayout(panelPrevia, BoxLayout.Y_AXIS));  // Usar BoxLayout para alinear los componentes verticalmente.
            panelPrevia.add(new JLabel("Vista previa de la Votación:"));  // Etiqueta para la vista previa.
            panelPrevia.add(new JLabel("Nombre: " + campoNombreVotacion.getText()));  // Mostrar el nombre de la votación.
            panelPrevia.add(new JLabel("Pregunta: " + campoPregunta.getText()));  // Mostrar la pregunta de la votación.
            camposOpciones.forEach(campo -> panelPrevia.add(new JLabel("Opción: " + campo.getText())));  // Mostrar cada opción de la votación.
            JButton botonCrear = new JButton("Crear");  // Botón para crear la votación.
            botonCrear.addActionListener(this::manejarCrearVotacion);  // Acción para crear la votación.
            JButton botonCancelar = new JButton("Cancelar");  // Botón para cancelar la creación de la votación.
            botonCancelar.addActionListener(ev -> {
                JOptionPane.showMessageDialog(this, "Creación de votación cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0); // Termina la aplicación
            });
            panelPrevia.add(botonCrear);  // Añadir el botón de crear al panel.
            panelPrevia.add(botonCancelar);  // Añadir el botón de cancelar al panel.
            tarjetas.add(panelPrevia, PANEL_PREVIA);  // Añadir el panel de vista previa al panel de tarjetas.
            mostrarTarjeta(PANEL_PREVIA);  // Mostrar el panel de vista previa.
        }
    }

    // Método para manejar la creación de la votación.
    private void manejarCrearVotacion(ActionEvent e) {
        List<String> opciones = new ArrayList<>();  // Lista para almacenar las opciones de la votación.
        camposOpciones.forEach(campo -> opciones.add(campo.getText().trim()));  // Añadir cada opción a la lista.
        controladorFrame.getControladorVotaciones().crearVotacion(campoNombreVotacion.getText().trim(), campoPregunta.getText().trim(), opciones);  // Llamar al controlador para crear la votación.
        JOptionPane.showMessageDialog(this, "Votación '" + campoNombreVotacion.getText() + "' creada con éxito!");  // Mostrar mensaje de éxito.
        System.exit(0);  // Termina completamente la aplicación después de mostrar el mensaje
    }

    // Método para mostrar un diálogo de error.
    private void mostrarDialogoError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);  // Mostrar el mensaje de error en un diálogo.
    }

    // Método para mostrar una tarjeta específica en el panel de tarjetas.
    private void mostrarTarjeta(String nombreTarjeta) {
        CardLayout cl = (CardLayout) (tarjetas.getLayout());  // Obtener el gestor de diseño CardLayout.
        cl.show(tarjetas, nombreTarjeta);  // Mostrar la tarjeta especificada.
    }
}