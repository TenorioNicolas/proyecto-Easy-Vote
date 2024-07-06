package ventanas;

import datos.ControladorVotaciones;
import dominio.Votacion;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


// Clase VerResultadosFrame que extiende de JFrame para crear una ventana de interfaz gráfica.
public class VerResultadosFrame extends JFrame {
    private ControladorVotaciones controladorVotaciones; // Instancia del controlador que maneja las votaciones.
    private JComboBox<String> comboBoxVotaciones; // ComboBox para seleccionar votaciones.
    private JTextArea textAreaResultados; // Área de texto para mostrar los resultados de las votaciones.

    // Constructor que recibe una instancia del controlador de votaciones.
    public VerResultadosFrame(ControladorVotaciones controladorVotaciones) {
        this.controladorVotaciones = controladorVotaciones;
        inicializarUI(); // Método para inicializar la interfaz gráfica.
    }

    // Método para configurar la interfaz de usuario.
    private void inicializarUI() {
        setTitle("Ver Resultados"); // Título de la ventana.
        setSize(400, 300); // Tamaño de la ventana.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Operación por defecto al cerrar.
        setLocationRelativeTo(null); // Centrar la ventana.
        setLayout(new BorderLayout()); // Usar BorderLayout para la distribución de componentes.

        // Obtener listado de votaciones inactivas.
        List<Votacion> votacionesInactivas = controladorVotaciones.getVotacionesInactivas();
        if (votacionesInactivas.isEmpty()) {
            // Si no hay votaciones inactivas, mostrar mensaje y cerrar.
            JOptionPane.showMessageDialog(this, "No hay votaciones inactivas para ver los resultados.", "Información", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            return;
        }

        // Configuración de JComboBox para las votaciones.
        comboBoxVotaciones = new JComboBox<>();
        for (Votacion votacion : votacionesInactivas) {
            comboBoxVotaciones.addItem(votacion.getId() + " - " + votacion.getNombre());
        }

        // Área de texto para los resultados, no editable.
        textAreaResultados = new JTextArea();
        textAreaResultados.setEditable(false);

        // Botón para ver los resultados.
        JButton botonVerResultados = new JButton("Ver Resultados");
        botonVerResultados.addActionListener(e -> mostrarResultados());

        // Botón para salir de la ventana.
        JButton botonSalir = new JButton("Salir");
        botonSalir.addActionListener(e -> System.exit(0));

        // Panel superior para el ComboBox y el área de texto.
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(comboBoxVotaciones, BorderLayout.NORTH);
        panelSuperior.add(new JScrollPane(textAreaResultados), BorderLayout.CENTER);

        // Panel inferior para los botones.
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(botonVerResultados);
        panelInferior.add(botonSalir);

        // Agregar paneles a la ventana principal.
        add(panelSuperior, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    // Método para mostrar los resultados de la votación seleccionada.
// Método para mostrar los resultados de la votación seleccionada y manejar empates.
    private void mostrarResultados() {
        String itemSeleccionado = (String) comboBoxVotaciones.getSelectedItem();
        if (itemSeleccionado == null) return; // Si no se seleccionó nada, no hacer nada.

        String idVotacion = itemSeleccionado.split(" - ")[0];
        Map<String, Integer> resultados = controladorVotaciones.getGestorVotos().contarVotos(idVotacion);

        if (resultados.isEmpty()) {
            textAreaResultados.setText("No hay resultados para mostrar.");
            return;
        }

        // Construir el texto de los resultados y determinar la opción ganadora o un empate.
        StringBuilder resultadosTexto = new StringBuilder();
        int maxVotos = 0;
        List<String> opcionesGanadoras = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : resultados.entrySet()) {
            resultadosTexto.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            if (entry.getValue() > maxVotos) {
                maxVotos = entry.getValue();
                opcionesGanadoras.clear();
                opcionesGanadoras.add(entry.getKey());
            } else if (entry.getValue() == maxVotos) {
                opcionesGanadoras.add(entry.getKey());
            }
        }

        // Mostrar la opción ganadora o indicar empate.
        if (opcionesGanadoras.size() > 1) {
            resultadosTexto.append("\nNo hay una opción ganadora debido a un empate.");
        } else {
            resultadosTexto.append("\nLa opción ganadora es: ").append(opcionesGanadoras.get(0));
        }

        textAreaResultados.setText(resultadosTexto.toString());
    }
}