package ventanas;

import datos.ControladorFrame;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JButton entrarButton;
    private ControladorFrame controladorFrame;

    public LoginFrame(ControladorFrame controladorFrame) {
        this.controladorFrame = controladorFrame;
        setTitle("Easy-Vote - Login");
        setSize(300, 200);
        setLocationRelativeTo(null);  // Centra la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta "Usuario"
        JLabel usuarioLabel = new JLabel("Usuario:");
        constraints.gridx = 0; // Columna 0
        constraints.gridy = 0; // Fila 0
        panel.add(usuarioLabel, constraints);

        // Campo de texto "Usuario"
        usuarioField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(usuarioField, constraints);

        // Etiqueta "Contraseña"
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(contrasenaLabel, constraints);

        // Campo de contraseña
        contrasenaField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(contrasenaField, constraints);

        // Botón "Entrar"
        entrarButton = new JButton("Entrar");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;  // Ocupa dos columnas
        constraints.insets = new Insets(10, 0, 0, 0);  // Margen superior
        panel.add(entrarButton, constraints);

        // Configuración del botón para usar el ControladorFrame
        entrarButton.addActionListener(e -> {
            String username = usuarioField.getText();
            String password = new String(contrasenaField.getPassword());
            controladorFrame.autenticarUsuario(username, password, this);
        });

        // Agregar el panel al frame
        add(panel);
        setVisible(true);  // Asegúrate de que la ventana sea visible
    }
}
