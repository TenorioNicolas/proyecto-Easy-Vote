package ventanas;

import datos.ControladorFrame; // Importa la clase ControladorFrame para manejar las acciones.
import dominio.Usuario; // Importa la clase Usuario para obtener información del usuario.

import javax.swing.*; // Importa todas las clases de Swing para la interfaz gráfica.
import java.awt.*; // Importa todas las clases de AWT para gestión de componentes y layouts.

// Clase MainMenuFrame, que extiende de JFrame para crear una ventana de interfaz gráfica.
public class MainMenuFrame extends JFrame {
    private Usuario usuario; // Almacena el usuario que ha iniciado sesión.
    private ControladorFrame controladorFrame; // Controlador para manejar las acciones del menú.

    // Constructor que recibe el controlador y el usuario.
    public MainMenuFrame(ControladorFrame controladorFrame, Usuario usuario) {
        this.controladorFrame = controladorFrame;
        this.usuario = usuario;
        inicializarInterfaz(); // Llama al método para inicializar la interfaz gráfica.
    }

    // Método para inicializar los componentes de la interfaz.
    private void inicializarInterfaz() {
        setTitle("Menú Principal - Easy Vote"); // Establece el título de la ventana.
        setSize(350, 250); // Ajusta el tamaño de la ventana.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Establece la operación de cierre por defecto.
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        setLayout(new GridLayout(5, 1, 10, 10)); // Usa GridLayout para organizar los componentes.

        // Crea y configura la etiqueta de bienvenida.
        JLabel etiquetaBienvenida = new JLabel("BIENVENIDO, " + usuario.getNombre().toUpperCase() + "!", SwingConstants.CENTER);
        etiquetaBienvenida.setFont(new Font("Arial", Font.BOLD, 16)); // Establece la fuente de la etiqueta.

        // Crea botones para las diferentes acciones disponibles en el menú.
        JButton botonCrearVotacion = new JButton("Crear Votación");
        JButton botonVotar = new JButton("Votar");
        JButton botonDeshabilitarVotacion = new JButton("Deshabilitar Votación");
        JButton botonVerResultados = new JButton("Ver resultados");

        // Asigna acciones a los botones mediante expresiones lambda que llaman al controlador.
        botonCrearVotacion.addActionListener(e -> {
            controladorFrame.mostrarCrearVotacionFrame(usuario);
            this.dispose(); // Cierra la ventana del menú principal cuando se abre la ventana de creación de votación.
        });

        botonVotar.addActionListener(e -> {
            controladorFrame.mostrarVotarFrame(usuario);
            this.dispose(); // Cierra la ventana del menú principal cuando se abre la ventana para votar.
        });

        botonDeshabilitarVotacion.addActionListener(e -> {
            controladorFrame.mostrarDeshabilitarVotacionFrame(usuario);
            this.dispose(); // Cierra la ventana del menú principal cuando se abre la ventana para deshabilitar votaciones.
        });

        botonVerResultados.addActionListener(e -> {
            controladorFrame.mostrarResultadosFrame(usuario);
            this.dispose(); // Cierra la ventana del menú principal cuando se abre la ventana para ver resultados.
        });

        // Añade los componentes a la ventana.
        add(etiquetaBienvenida);
        add(botonCrearVotacion);
        add(botonVotar);
        add(botonDeshabilitarVotacion);
        add(botonVerResultados);

        gestionarVisibilidadBotones(usuario.getRol()); // Gestiona la visibilidad de los botones según el rol del usuario.

        setVisible(true); // Hace visible la ventana.
    }

    // Método para gestionar la visibilidad de los botones basado en el rol del usuario.
    private void gestionarVisibilidadBotones(String rol) {
        if (!rol.equals("Creador")) {
            getContentPane().getComponent(1).setVisible(false); // Oculta el botón "Crear Votación".
            getContentPane().getComponent(3).setVisible(false); // Oculta el botón "Deshabilitar Votación".
        }
    }
}