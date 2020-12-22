package com.aportefacil.backend.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class CarteiraTest {

    @Test
    void testBalanceMergeDuplicates() {

        Carteira carteira = new Carteira(10000.0, Arrays.asList(
                new Ativo("A", 3, 1d, 100d),
                new Ativo("A", 4, 1d, 100d),
                new Ativo("B", 0, 1d, 100d),
                new Ativo("B", 0, 2d, 100d),
                new Ativo("A", 2, 1d, 100d)
        ));

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 27);
        assertEquals(String.format("%.02f", result.get("A").getDesbalanco()), "2733.33");
        assertEquals(result.get("A").getPeso(), 1);

        assertEquals(result.get("B").getAcao(), 73);
        assertEquals(String.format("%.02f", result.get("B").getDesbalanco()), "7366.67");
        assertEquals(result.get("B").getPeso(), 2);
    }

    @Test
    void testBalanceSimple() {

        Carteira carteira = new Carteira(1000.0, Arrays.asList(
                new Ativo("A", 0, 1d, 100d),
                new Ativo("B", 0, 1d, 100d)
        ));

        carteira.balance();

        assertEquals(carteira, new Carteira(1000.0, Arrays.asList(
                new Ativo("A", 0, 1d, 100d, 500d, 5),
                new Ativo("B", 0, 1d, 100d, 500d, 5)
        )));
    }

    @Test
    void testBalanceDifPesos() {

        // Arrange
        Carteira carteira = new Carteira(1000.0, Arrays.asList(
                new Ativo("A", 0, 1d, 100d),
                new Ativo("B", 0, 3d, 50d)
        ));

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 2);
        assertEquals(result.get("A").getDesbalanco(), 250);

        assertEquals(result.get("B").getAcao(), 16);
        assertEquals(result.get("B").getDesbalanco(), 800);
    }

    @Test
    void testBalanceDifPesosNegativos() {

        // Arrange
        Carteira carteira = new Carteira(1000.0, Arrays.asList(
                new Ativo("A", 5, 1d, 100d),
                new Ativo("B", 0, 3d, 50d)
        ));

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 0);
        assertEquals(result.get("A").getDesbalanco(), -125);

        assertEquals(result.get("B").getAcao(), 20);
        assertEquals(result.get("B").getDesbalanco(), 1000);
    }

    @Test
    void testBalanceDifPesosNegativosComplexo() {

        // Arrange
        Carteira carteira = new Carteira(5500.0, Arrays.asList(
                new Ativo("A", 50, 1d, 100d),
                new Ativo("B", 1, 2d, 150d),
                new Ativo("C", 5, 3d, 200d)
        ));

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 0);
        assertEquals(String.format("%.02f", result.get("A").getDesbalanco()), "-3058.33");

        assertEquals(result.get("B").getAcao(), 16);
        assertEquals(result.get("B").getDesbalanco(), 2510);

        assertEquals(result.get("C").getAcao(), 15);
        assertEquals(result.get("C").getDesbalanco(), 3190);
    }

    private Map<String, Ativo> dict(Carteira carteira) {
        return carteira.getAtivos()
                .stream()
                .collect(Collectors.toMap(Ativo::getTicker, Function.identity()));
    }
}