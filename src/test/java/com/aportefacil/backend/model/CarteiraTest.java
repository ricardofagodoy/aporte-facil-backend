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
                new Ativo("A", 3, 1d, new InfoAtivo(100d, null, null, null, null)),
                new Ativo("A", 4, 1d, new InfoAtivo(100d, null, null, null, null)),
                new Ativo("B", 0, 1d, new InfoAtivo(100d, null, null, null, null)),
                new Ativo("B", 0, 2d, new InfoAtivo(100d, null, null, null, null)),
                new Ativo("A", 2, 1d, new InfoAtivo(100d, null, null, null, null))
        ));

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 27);
        assertEquals(String.format("%.02f", result.get("A").getDesbalanco()), "2733.33");
        assertEquals(result.get("A").getPeso(), 1);

        assertEquals(result.get("B").getAcao(), 73);
        assertEquals(String.format("%.02f", result.get("B").getDesbalanco()), "7266.67");
        assertEquals(result.get("B").getPeso(), 2);
    }

    @Test
    void testBalanceSimple() {

        Carteira carteira = new Carteira(1000.0, Arrays.asList(
                new Ativo("A", 0, 1d, new InfoAtivo(100d, null, null, null, null)),
                new Ativo("B", 0, 1d, new InfoAtivo(100d, null, null, null, null))
        ));

        carteira.balance();

        assertTrue(carteira.equals(new Carteira(1000.0, Arrays.asList(
                new Ativo("A", 0, 1d, new InfoAtivo(100d, null, null, null, null), 500d, 5, false),
                new Ativo("B", 0, 1d, new InfoAtivo(100d, null, null, null, null), 500d, 5, false)
        ))));
    }

    @Test
    void testBalanceDifPesos() {

        // Arrange
        Carteira carteira = new Carteira(1000.0, Arrays.asList(
                new Ativo("A", 0, 1d, new InfoAtivo(100d, null, null, null, null)),
                new Ativo("B", 0, 3d, new InfoAtivo(50d, null, null, null, null))
        ));

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 2);
        assertEquals(result.get("A").getDesbalanco(), 250);

        assertEquals(result.get("B").getAcao(), 16);
        assertEquals(result.get("B").getDesbalanco(), 750);
    }

    @Test
    void testBalanceDifPesosNegativos() {

        // Arrange
        Carteira carteira = new Carteira(1000.0, Arrays.asList(
                new Ativo("A", 5, 1d, new InfoAtivo(100d, null, null, null, null)),
                new Ativo("B", 0, 3d, new InfoAtivo(50d, null, null, null, null))
        ));

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 0);
        assertEquals(result.get("A").getDesbalanco(), -125);

        assertEquals(result.get("B").getAcao(), 20);
        assertEquals(result.get("B").getDesbalanco(), 1125);
    }

    @Test
    void testBalanceDifPesosNegativosComplexo() {

        // Arrange
        Carteira carteira = new Carteira(5500.0, Arrays.asList(
                new Ativo("A", 50, 1d, new InfoAtivo(100d, null, null, null, null)),
                new Ativo("B", 1, 2d, new InfoAtivo(150d, null, null, null, null)),
                new Ativo("C", 5, 3d, new InfoAtivo(200d, null, null, null, null))
        ));

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 0);
        assertEquals(String.format("%.02f", result.get("A").getDesbalanco()), "-3058.33");

        assertEquals(result.get("B").getAcao(), 16);
        assertEquals(String.format("%.02f", result.get("B").getDesbalanco()), "3733.33");

        assertEquals(result.get("C").getAcao(), 15);
        assertEquals(result.get("C").getDesbalanco(), 4825);
    }

    @Test
    void testBalanceDifPesosNegativosComplexo2() {

        // Arrange
        Carteira carteira = new Carteira(500.0, Arrays.asList(
                new Ativo("A", 1, 4d, new InfoAtivo(10d, null, null, null, null)), // 10 (400)   d: 390 a: 39 / (370) d: 360
                new Ativo("B", 13, 2d, new InfoAtivo(20d, null, null, null, null)), // 260 (200) d: -60 a: 0 / --
                new Ativo("C", 10, 4d, new InfoAtivo(23d, null, null, null, null)) // 230 (400)  d: 170 a: 7 / (370) d: 140
        )); // total: 1000 / 740

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 36);
        assertEquals(String.format("%.02f", result.get("A").getDesbalanco()), "390.00");

        assertEquals(result.get("B").getAcao(), 0);
        assertEquals(String.format("%.02f", result.get("B").getDesbalanco()), "-60.00");

        assertEquals(result.get("C").getAcao(), 6);
        assertEquals(result.get("C").getDesbalanco(), 170);
    }

    @Test
    void testBalanceDifPesosNegativosComplexo3() {

        // Arrange
        Carteira carteira = new Carteira(511.0, Arrays.asList(
                new Ativo("A", 1, 4d, new InfoAtivo(10d, null, null, null, null)), // 10 (404.40)   d: 394.40 a: 39 / (375.50) d: 365.50
                new Ativo("B", 13, 2d, new InfoAtivo(20d, null, null, null, null)), // 260 (202,20) d: -57.80 a: 0 / --
                new Ativo("C", 10, 4d, new InfoAtivo(23d, null, null, null, null)) // 230 (404,40)  d: 174.40 a: 7 / (375.50) d: 145.50
        )); // total: 1011 / 751 / 360 + 138 = 498, 511 - 498 = 13 saldo restante, compra mais um do ativo A, subindo para 37 a acao

        // Act
        carteira.balance();
        Map<String, Ativo> result = dict(carteira);

        // Assert
        assertEquals(result.get("A").getAcao(), 37);
        assertEquals(String.format("%.02f", result.get("A").getDesbalanco()), "394.40");

        assertEquals(result.get("B").getAcao(), 0);
        assertEquals(String.format("%.02f", result.get("B").getDesbalanco()), "-57.80");

        assertEquals(result.get("C").getAcao(), 6);
        assertEquals(String.format("%.02f", result.get("C").getDesbalanco()), "174.40");
    }

    private Map<String, Ativo> dict(Carteira carteira) {
        return carteira.getAtivos()
                .stream()
                .collect(Collectors.toMap(Ativo::getTicker, Function.identity()));
    }
}