package ventanas;

import datos.ControladorVotaciones;
import dominio.Votacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class VerResultadosFrame extends JFrame {
    private ControladorVotaciones controladorVotaciones;
    private JComboBox<String> comboBoxVotaciones;
    private JTextArea textAreaResultados;

    public VerResultadosFrame(ControladorVotaciones controladorVotaciones) {
        this.controladorVotaciones = controladorVotaciones;
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Ver Resultados");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        comboBoxVotaciones = new JComboBox<>();
        textAreaResultados = new JTextArea();
        textAreaResultados.setEditable(false);

        List<Votacion> votaciones = controladorVotaciones.getVotaciones();
        for (Votacion votacion : votaciones) {
            comboBoxVotaciones.addItem(votacion.getId() + " - " + votacion.getNombre());
        }

        JButton botonVerResultados = new JButton("Ver Resultados");
        botonVerResultados.addActionListener((ActionEvent e) -> {
            String seleccion = (String) comboBoxVotaciones.getSelectedItem();
            if (seleccion != null) {
                String idVotacion = seleccion.split(" - ")[0];
                Map<String, Integer> resultados = controladorVotaciones.obtenerResultadosVotacion(idVotacion);

                StringBuilder resultadosTexto = new StringBuilder();
                String opcionGanadora = null;
                int maxVotos = -1;

                for (Map.Entry<String, Integer> entry : resultados.entrySet()) {
                    resultadosTexto.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    if (entry.getValue() > maxVotos) {
                        maxVotos = entry.getValue();
                        opcionGanadora = entry.getKey();
                    }
                }

                if (opcionGanadora != null) {
                    resultadosTexto.append("\nLa opción ganadora es: ").append(opcionGanadora);
                }

                textAreaResultados.setText(resultadosTexto.toString());
            }
        });

        JButton botonSalir = new JButton("Salir");
        botonSalir.addActionListener(e -> System.exit(0));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.add(botonVerResultados);
        panelBotones.add(botonSalir);

        add(comboBoxVotaciones, BorderLayout.NORTH);
        add(new JScrollPane(textAreaResultados), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
}
