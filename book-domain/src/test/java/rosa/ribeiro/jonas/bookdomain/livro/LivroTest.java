package rosa.ribeiro.jonas.bookdomain.livro;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.bookdomain.model.livro.LivroBrochura;
import rosa.ribeiro.jonas.bookdomain.model.livro.LivroCapaDura;
import rosa.ribeiro.jonas.bookdomain.model.livro.LivroDigital;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class LivroTest {

    // --- TÉCNICA: PARTIÇÃO DE EQUIVALÊNCIA (RN02 - Preços) ---
    // Testamos um representante de cada classe (Partição 1 - Capa Dura, Partição 2 - Brochura, Partição 3 -Digital)
//        Teste 1 (deveCalcularPrecoCapaDura): Representa a partição 1  "Sem Desconto".
//        Teste 2 (deveCalcularPrecoBrochura): Representa a partição 2 "Desconto 5%".
//        Teste 3 (deveCalcularPrecoDigital): Representa a partição 3 "Desconto 10%".

    @Test
    @DisplayName("RN02: Capa Dura deve custar preço cheio (Partição 1)")
    void deveCalcularPrecoCapaDura() {
        Livro livro = new LivroCapaDura("T", "I", 1, 2020, "R", 10, new BigDecimal("100.00"), null, null, null);
        assertEquals(0, new BigDecimal("100.00").compareTo(livro.calcularPreco()));
    }

    @Test
    @DisplayName("RN02: Brochura deve ter 5% desconto (Partição 2)")
    void deveCalcularPrecoBrochura() {
        Livro livro = new LivroBrochura("T", "I", 1, 2020, "R", 10, new BigDecimal("100.00"), null, null, null);
        assertEquals(0, new BigDecimal("95.00").compareTo(livro.calcularPreco()));
    }

    @Test
    @DisplayName("RN02: Digital deve ter 10% desconto (Partição 3)")
    void deveCalcularPrecoDigital() {
        Livro livro = new LivroDigital("T", "I", 1, 2020, "R", 10, new BigDecimal("100.00"), null, null, null);
        assertEquals(0, new BigDecimal("90.00").compareTo(livro.calcularPreco()));
    }

    // --- TÉCNICA: ANÁLISE DE VALOR LIMITE (RN03 - Estoque Mínimo) ---
    // A regra diz: "Estoque mínimo não pode ser menor do que 2".
    // Limites Críticos: 1 (True), 2 (True), 3 (False)
//   Dessa forma usei o @ParameterizedTest para injetar os valores:
//            1 (Limite inferior/inválido para alerta).
//            2 (Limite exato - Fronteira).
//            3 (Limite superior/válido).

    @ParameterizedTest(name = "Estoque {0} deve ser considerado mínimo? {1}")
    @CsvSource({
            "1, true",  // Abaixo do limite
            "2, true",  // No limite exato (Borda)
            "3, false", // Logo acima do limite
            "10, false" // Partição válida distante
    })
    void deveVerificarEstoqueMinimo(int estoqueAtual, boolean esperado) {
        Livro livro = new LivroCapaDura("T", "I", 1, 2020, "R", estoqueAtual, BigDecimal.TEN, null, null, null);
        assertEquals(esperado, livro.verificarEstoqueMinimo());
    }

    // --- TÉCNICA: VALOR LIMITE E CAMINHO DE EXCEÇÃO (Decremento) ---
//        Nesse teste o Limite: O estoque é 10.
//        Teste 1 (10): Testa o limite superior Válido. (Deve passar).
//        Teste 2 (11): Testa o primeiro valor Inválido logo após o limite. (Deve falhar).

    @Test
    @DisplayName("Deve zerar o estoque quando decrementar o valor total (Limite Exato)")
    void deveZerarEstoque() throws Exception {
        Livro livro = new LivroCapaDura("T", "I", 1, 2020, "R", 10, BigDecimal.TEN, null, null, null);

        livro.decrementarEstoque(10); // Limite exato

        assertEquals(0, livro.getQuantidadeEstoque());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar decrementar 1 a mais que o saldo (Limite Estourado)")
    void naoDeveDecrementarAlemDoSaldo() {
        Livro livro = new LivroCapaDura("T", "I", 1, 2020, "R", 10, BigDecimal.TEN, null, null, null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            livro.decrementarEstoque(11);
        });

        assertEquals("Não há quantidade suficiente no estoque!", exception.getMessage());
    }
}
