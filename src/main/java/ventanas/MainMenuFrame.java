package ventanas;

import datos.ControladorFrame;
import dominio.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    private Usuario usuario;
    private ControladorFrame controladorFrame;

    public MainMenuFrame(ControladorFrame controladorFrame, Usuario usuario) {
        this.controladorFrame = controladorFrame;
        this.usuario = usuario;
        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        setTitle("Menú Principal - Easy Vote");
        setSize(350, 250);  // Ajuste en el tamaño para acomodar un botón adicional
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));  // Ajustado a 5 filas para el nuevo botón

        JLabel etiquetaBienvenida = new JLabel("BIENVENIDO, " + usuario.getNombre().toUpperCase() + "!", SwingConstants.CENTER);
        etiquetaBienvenida.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botonCrearVotacion = new JButton("Crear Votación");
        JButton botonVotar = new JButton("Votar");
        JButton botonDeshabilitarVotacion = new JButton("Deshabilitar Votación");
        JButton botonVerResultados = new JButton("Ver resultados");

        botonCrearVotacion.addActionListener(e -> controladorFrame.mostrarCrearVotacionFrame(usuario));
        botonVotar.addActionListener(e -> controladorFrame.mostrarVotarFrame(usuario));
        botonDeshabilitarVotacion.addActionListener(e -> controladorFrame.mostrarDeshabilitarVotacionFrame(usuario));
        botonVerResultados.addActionListener(e -> controladorFrame.mostrarResultadosFrame(usuario));

        add(etiquetaBienvenida);
        add(botonCrearVotacion);
        add(botonVotar);
        add(botonDeshabilitarVotacion);
        add(botonVerResultados);

        gestionarVisibilidadBotones(usuario.getRol());

        setVisible(true);
    }

    private void gestionarVisibilidadBotones(String rol) {
        if (!rol.equals("Creador")) {
            getContentPane().getComponent(1).setVisible(false);  // Oculta "Crear Votación"
            getContentPane().getComponent(3).setVisible(false);  // Oculta "Deshabilitar Votación"
        }
    }
}