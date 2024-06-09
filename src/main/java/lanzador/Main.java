package lanzador;

import datos.GestorUsuarios;
import datos.ControladorVotaciones;
import dominio.Menu;

public class Main {
    public static void main(String[] args) {
        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        ControladorVotaciones controladorVotaciones = new ControladorVotaciones();
        Menu menu = new Menu(gestorUsuarios, controladorVotaciones);
        menu.iniciarSesionYMostrarMenu();
    }
}