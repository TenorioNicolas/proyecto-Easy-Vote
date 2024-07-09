package ventanas;

import datos.ControladorFrame;  // Importa la clase ControladorFrame que gestiona la lógica de negocio.
import javax.swing.*;  // Importa los componentes de la biblioteca Swing para la interfaz gráfica.
import java.awt.*;  // Importa clases de AWT para manejo de layouts y otros componentes gráficos.

// Esta clase crea una ventana de login, permitiendo al usuario ingresar sus credenciales.
public class LoginFrame extends JFrame {
    private JTextField usuarioField;  // Campo de texto para ingresar el nombre de usuario.
    private JPasswordField contrasenaField;  // Campo de texto para ingresar la contraseña, ocultando los caracteres.
    private JButton entrarButton;  // Botón para iniciar el proceso de login.
    private ControladorFrame controladorFrame;  // Instancia del controlador para interactuar con la lógica de negocio.

    // Constructor de la clase que inicializa la ventana y sus componentes.
    public LoginFrame() {
        this.controladorFrame = new ControladorFrame();  // Crea una instancia del controlador.
        setTitle("Easy-Vote - Login");  // Establece el título de la ventana.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Define la operación de cierre (cerrar la aplicación).
        setResizable(false);  // Impide que la ventana cambie de tamaño.
        initComponents();  // Llama al método que inicializa los componentes de la interfaz.
        pack();  // Ajusta el tamaño de la ventana a los elementos que contiene.
        setLocationRelativeTo(null);  // Centra la ventana en la pantalla.
    }

    // Método que inicializa los componentes de la interfaz de usuario.
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());  // Utiliza GridBagLayout para un posicionamiento flexible de los componentes.
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;  // Ajusta los componentes horizontalmente.
        constraints.insets = new Insets(5, 5, 5, 5);  // Establece márgenes internos.

        // Configura y añade la etiqueta y el campo de texto para el usuario.
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioField = new JTextField(20);  // Campo con longitud de 20 columnas.
        setupComponent(panel, usuarioLabel, constraints, 0, 0);  // Posiciona la etiqueta en la fila 0, columna 0.
        setupComponent(panel, usuarioField, constraints, 1, 0);  // Posiciona el campo de texto en la fila 1, columna 0.

        // Configura y añade la etiqueta y el campo de contraseña.
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaField = new JPasswordField(20);  // Campo de contraseña con longitud de 20 columnas.
        setupComponent(panel, contrasenaLabel, constraints, 0, 1);  // Posiciona la etiqueta en la fila 0, columna 1.
        setupComponent(panel, contrasenaField, constraints, 1, 1);  // Posiciona el campo de contraseña en la fila 1, columna 1.

        // Configura y añade el botón de entrada.
        entrarButton = new JButton("Entrar");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;  // El botón ocupa dos columnas.
        constraints.insets = new Insets(10, 0, 0, 0);  // Aumenta el margen superior del botón.
        entrarButton.addActionListener(e -> {
            String username = usuarioField.getText();
            String password = new String(contrasenaField.getPassword());
            controladorFrame.autenticarUsuario(username, password, this);  // Llama al método de autenticación del controlador.
        });
        panel.add(entrarButton, constraints);  // Añade el botón al panel.

        add(panel);  // Añade el panel al frame.
        setVisible(true);  // Hace visible la ventana.
    }

    // Método auxiliar para configurar y añadir componentes al panel.
    private void setupComponent(JPanel panel, JComponent component, GridBagConstraints constraints, int x, int y) {
        constraints.gridx = x;  // Establece la posición en la columna x.
        constraints.gridy = y;  // Establece la posición en la fila y.
        panel.add(component, constraints);  // Añade el componente al panel con las restricciones especificadas.
    }
}