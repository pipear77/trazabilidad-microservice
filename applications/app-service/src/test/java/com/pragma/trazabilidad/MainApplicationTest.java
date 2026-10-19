package com.pragma.trazabilidad;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class MainApplicationTest {

    @Test
    void contextLoads() {
        // Verifica que el contexto levante correctamente
    }

    @Test
    void mainMethodTest() {
        // Invoca el método main para cubrir la línea de arranque y alcanzar el 100% en JaCoCo
        assertDoesNotThrow(() -> MainApplication.main(new String[]{}));
    }
}