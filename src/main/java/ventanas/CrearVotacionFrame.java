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
    private JPanel cards; // Panel que contiene las "tarjetas"
    private final static String NAME_PANEL = "Card with voting name";
    private final static String QUESTION_PANEL = "Card with voting question";
    private final static String OPTIONS_COUNT_PANEL = "Card with options count";
    private final static String ADD_OPTIONS_PANEL = "Card with add options";
    private final static String PREVIEW_PANEL = "Card with preview";

    private JTextField nombreVotacionField;
    private JTextField questionField;
    private JComboBox<Integer> optionsCountComboBox;
    private List<JTextField> optionFields; // List to keep references to option fields

    public CrearVotacionFrame(ControladorFrame controladorFrame) {
        this.controladorFrame = controladorFrame;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Crear Nueva Votación");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cards = new JPanel(new CardLayout());

        setupNamePanel();
        setupQuestionPanel();
        setupOptionsCountPanel();

        add(cards, BorderLayout.CENTER);
        setVisible(true);
    }

    private void setupNamePanel() {
        JPanel namePanel = new JPanel();
        nombreVotacionField = new JTextField(20);
        namePanel.add(new JLabel("Nombre de la Votación:"));
        namePanel.add(nombreVotacionField);
        JButton nextToQuestionButton = new JButton("Siguiente");
        nextToQuestionButton.addActionListener(e -> showCard(QUESTION_PANEL));
        namePanel.add(nextToQuestionButton);

        cards.add(namePanel, NAME_PANEL);
    }

    private void setupQuestionPanel() {
        JPanel questionPanel = new JPanel();
        questionPanel.add(new JLabel("Pregunta:"));
        questionField = new JTextField(20);
        questionPanel.add(questionField);
        JButton nextToOptionsCountButton = new JButton("Siguiente");
        nextToOptionsCountButton.addActionListener(e -> showCard(OPTIONS_COUNT_PANEL));
        questionPanel.add(nextToOptionsCountButton);

        cards.add(questionPanel, QUESTION_PANEL);
    }

    private void setupOptionsCountPanel() {
        JPanel optionsCountPanel = new JPanel();
        optionsCountPanel.add(new JLabel("Seleccione la cantidad de opciones:"));

        Integer[] optionsCounts = IntStream.rangeClosed(2, 10).boxed().toArray(Integer[]::new);
        optionsCountComboBox = new JComboBox<>(optionsCounts);
        optionsCountPanel.add(optionsCountComboBox);

        JButton nextToAddOptionsButton = new JButton("Siguiente");
        nextToAddOptionsButton.addActionListener(e -> setupAndShowAddOptionsPanel());
        optionsCountPanel.add(nextToAddOptionsButton);

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(e -> showCard(QUESTION_PANEL));
        optionsCountPanel.add(backButton);

        cards.add(optionsCountPanel, OPTIONS_COUNT_PANEL);
    }

    private void setupAndShowAddOptionsPanel() {
        int numberOfOptions = (Integer) optionsCountComboBox.getSelectedItem();
        JPanel addOptionsPanel = new JPanel();
        addOptionsPanel.setLayout(new BoxLayout(addOptionsPanel, BoxLayout.Y_AXIS));
        addOptionsPanel.add(new JLabel("Ingrese el contenido de las opciones:"));

        optionFields = new ArrayList<>();
        for (int i = 0; i < numberOfOptions; i++) {
            JTextField optionField = new JTextField(20);
            addOptionsPanel.add(new JLabel("Opción " + (i + 1) + ":"));
            addOptionsPanel.add(optionField);
            optionFields.add(optionField);
        }

        JButton nextToPreviewButton = new JButton("Vista Previa");
        nextToPreviewButton.addActionListener(this::showPreviewPanel);
        addOptionsPanel.add(nextToPreviewButton);

        cards.add(addOptionsPanel, ADD_OPTIONS_PANEL);
        showCard(ADD_OPTIONS_PANEL);
    }

    private void showPreviewPanel(ActionEvent e) {
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.add(new JLabel("Vista previa de la Votación:"));

        previewPanel.add(new JLabel("Nombre: " + nombreVotacionField.getText()));
        previewPanel.add(new JLabel("Pregunta: " + questionField.getText()));
        optionFields.forEach(field -> previewPanel.add(new JLabel("Opción: " + field.getText())));

        JButton createButton = new JButton("Crear");
        createButton.addActionListener(this::handleCreateVotation);
        previewPanel.add(createButton);

        cards.add(previewPanel, PREVIEW_PANEL);
        showCard(PREVIEW_PANEL);
    }

    private void handleCreateVotation(ActionEvent e) {
        String nombre = nombreVotacionField.getText();
        String pregunta = questionField.getText();
        List<String> opciones = new ArrayList<>();
        for (JTextField field : optionFields) {
            opciones.add(field.getText().trim());  // Asegurarse de limpiar los campos
        }

        controladorFrame.getControladorVotaciones().agregarYGuardarVotacion(nombre, pregunta, opciones);

        JOptionPane.showMessageDialog(this, "Votación '" + nombre + "' creada con éxito!");
        dispose(); // Cerrar la ventana después de guardar
    }

    private void showCard(String cardName) {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, cardName);
    }
}