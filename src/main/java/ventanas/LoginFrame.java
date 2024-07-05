package ventanas;

import datos.ControladorFrame;  // Importación de la clase que maneja la lógica relacionada con las operaciones del frame.
import javax.swing.*;  // Importación de componentes de Swing para la interfaz gráfica.
import java.awt.*;  // Herramientas de AWT para gestionar componentes y eventos.

// Clase que extiende de JFrame para crear una ventana de login.
public class LoginFrame extends JFrame {
    private JTextField usuarioField;  // Campo de texto para el usuario.
    private JPasswordField contrasenaField;  // Campo de contraseña para el usuario.
    private JButton entrarButton;  // Botón para iniciar sesión.
    private ControladorFrame controladorFrame;  // Referencia al controlador para manejar la lógica de negocio.

    // Constructor que recibe una instancia de ControladorFrame.
    public LoginFrame(ControladorFrame controladorFrame) {
        this.controladorFrame = controladorFrame;
        setTitle("Easy-Vote - Login");  // Título de la ventana.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Operación al cerrar.
        setResizable(false);  // Deshabilita la redimensión de la ventana.
        initComponents();
        pack();  // Ajusta el tamaño de la ventana automáticamente.
        setLocationRelativeTo(null);  // Centra la ventana en la pantalla.
    }

    // Inicializa los componentes de la interfaz.
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());  // Panel con GridBagLayout para la disposición flexible de los componentes.
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);  // Margen para los componentes.

        // Etiqueta y campo para el usuario.
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioField = new JTextField(20);
        setupComponent(panel, usuarioLabel, constraints, 0, 0);  // Añade la etiqueta al panel.
        setupComponent(panel, usuarioField, constraints, 1, 0);  // Añade el campo de texto al panel.

        // Etiqueta y campo para la contraseña.
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaField = new JPasswordField(20);
        setupComponent(panel, contrasenaLabel, constraints, 0, 1);  // Añade la etiqueta al panel.
        setupComponent(panel, contrasenaField, constraints, 1, 1);  // Añade el campo de contraseña al panel.

        // Botón de entrada.
        entrarButton = new JButton("Entrar");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;  // El botón abarca dos columnas.
        constraints.insets = new Insets(10, 0, 0, 0);  // Margen superior para el botón.
        entrarButton.addActionListener(e -> {
            String username = usuarioField.getText();
            String password = new String(contrasenaField.getPassword());
            controladorFrame.autenticarUsuario(username, password, this);  // Invoca al controlador para autenticar al usuario.
        });
        panel.add(entrarButton, constraints);

        add(panel);  // Añade el panel al frame.
        setVisible(true);  // Asegura que la ventana sea visible.
    }

    // Método auxiliar para configurar y añadir componentes al panel.
    private void setupComponent(JPanel panel, JComponent component, GridBagConstraints constraints, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        panel.add(component, constraints);
    }
}