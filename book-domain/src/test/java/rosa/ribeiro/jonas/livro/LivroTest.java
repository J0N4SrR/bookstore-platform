package rosa.ribeiro.jonas.livro;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rosa.ribeiro.jonas.model.livro.Livro;
import rosa.ribeiro.jonas.model.livro.LivroBrochura;
import rosa.ribeiro.jonas.model.livro.LivroDigital;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LivroTest {
    @Test
    @DisplayName("RN02: Livro Brochura deve ter 5% de desconto")
    void deveCalcularPrecoBrochuraCorretamente() {
        BigDecimal precoBase = new BigDecimal("100.00");
        Livro livro = new LivroBrochura(
                "Titulo", "ISBN", 100, 2020, "Resumo", 10, precoBase,
                null, null, null
        );

        BigDecimal precoFinal = livro.calcularPreco();
        assertEquals(0, new BigDecimal("95.00").compareTo(precoFinal), "O desconto de 5% não foi aplicado corretamente.");
    }
    @Test
    @DisplayName("RN02: Livro Digital deve ter 10% de desconto")
    void deveCalcularPrecoDigitalCorretamente() {
        BigDecimal precoBase = new BigDecimal("100.00");
        Livro livro = new LivroDigital(
                "Titulo", "ISBN", 100, 2020, "Resumo", 10, precoBase,
                null, null, null
        );

        BigDecimal precoFinal = livro.calcularPreco();

        assertEquals(0, new BigDecimal("90.00").compareTo(precoFinal), "O desconto de 10% não foi aplicado corretamente.");
    }
}
