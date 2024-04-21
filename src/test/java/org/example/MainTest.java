package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

class MainTest {

    @Test
    void testValidarUsuarioCorrecto() {
        Map<String, String> usuariosContraseñas = new HashMap<>();
        usuariosContraseñas.put("123456789", "contraseña1");
        assertTrue(Main.validarUsuario("123456789", "contraseña1", usuariosContraseñas));
    }

    @Test
    void testValidarUsuarioIncorrecto() {
        Map<String, String> usuariosContraseñas = new HashMap<>();
        usuariosContraseñas.put("123456789", "contraseña1");
        assertFalse(Main.validarUsuario("123456789", "contraseñaIncorrecta", usuariosContraseñas));
    }
}
