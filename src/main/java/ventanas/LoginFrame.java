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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
        pack();  // Ajustar el tamaño de la ventana automáticamente
        setLocationRelativeTo(null);  // Centra la ventana
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);  // Margen para mejor visualización

        // Usuario label and field
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioField = new JTextField(20);
        setupComponent(panel, usuarioLabel, constraints, 0, 0);
        setupComponent(panel, usuarioField, constraints, 1, 0);

        // Contraseña label and field
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaField = new JPasswordField(20);
        setupComponent(panel, contrasenaLabel, constraints, 0, 1);
        setupComponent(panel, contrasenaField, constraints, 1, 1);

        // Botón entrar
        entrarButton = new JButton("Entrar");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2; // El botón abarca dos columnas
        constraints.insets = new Insets(10, 0, 0, 0);  // Espacio superior para el botón
        entrarButton.addActionListener(e -> {
            String username = usuarioField.getText();
            String password = new String(contrasenaField.getPassword());
            controladorFrame.autenticarUsuario(username, password, this);
        });
        panel.add(entrarButton, constraints);

        add(panel);
        setVisible(true);  // Asegura que la ventana sea visible
    }

    private void setupComponent(JPanel panel, JComponent component, GridBagConstraints constraints, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        panel.add(component, constraints);
    }
}