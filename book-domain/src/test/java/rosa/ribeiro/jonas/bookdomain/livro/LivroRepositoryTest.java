package rosa.ribeiro.jonas.bookdomain.livro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.bookdomain.model.livro.LivroCapaDura;
import rosa.ribeiro.jonas.bookdomain.model.livro.StatusLivro;
import rosa.ribeiro.jonas.bookdomain.repository.LivroRepository;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class LivroRepositoryTest {

    @Autowired
    private LivroRepository livroRepository;

    @Test
    @DisplayName("RN03: Deve encontrar apenas livros disponíveis com estoque baixo")
    void deveEncontrarEstoqueBaixo() {

//        O Cenário: Eu salvei 3 livros no banco(H2):
//        - Tem estoque 1 e está Disponível. (Deve vir).
//        - Tem estoque 10. (Não deve vir, pois tem muito estoque).
//        - Tem estoque 1, mas está Indisponível. (Não deve vir, pois o status bloqueia).

        // Caso Positivo: Estoque 1 (<=2) e Disponível
        Livro livroBaixo = new LivroCapaDura("Livro Raro", "111", 100, 2020, "R", 1, BigDecimal.TEN, null, null, null);
        livroBaixo.mudarStatus(StatusLivro.DISPONIVEL);

        // Caso Negativo 1: Estoque 10 (>2)
        Livro livroAlto = new LivroCapaDura("Livro Comum", "222", 100, 2020, "R", 10, BigDecimal.TEN, null, null, null);
        livroAlto.mudarStatus(StatusLivro.DISPONIVEL);

        // Caso Negativo 2: Estoque 1 (<=2) mas Indisponível
        Livro livroBaixoMasIndisponivel = new LivroCapaDura("Livro Velho", "333", 100, 2020, "R", 1, BigDecimal.TEN, null, null, null);
        livroBaixoMasIndisponivel.mudarStatus(StatusLivro.INDISPONIVEL);

        livroRepository.saveAll(List.of(livroBaixo, livroAlto, livroBaixoMasIndisponivel));

        List<Livro> resultado = livroRepository.findLivrosComEstoqueBaixo(2);

        assertThat(resultado).hasSize(1); // Garante que o filtro funcionou
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Livro Raro");
        assertThat(resultado.get(0).getId()).isNotNull(); // Garante que foi salvo e ganhou ID
    }

//    Salvamos um livro com ISBN "999-888". Pedimos ao repositório para buscar exatamente essa String.
//    Para validar a função "Pesquisar Livro"
    @Test
    @DisplayName("CSU01: Deve buscar livro por ISBN (Query Method)")
    void deveBuscarPorIsbn() {
        Livro livro = new LivroCapaDura("Busca ISBN", "999-888", 100, 2020, "R", 10, BigDecimal.TEN, null, null, null);
        livroRepository.save(livro);

        Optional<Livro> encontrado = livroRepository.findByIsbn("999-888");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getTitulo()).isEqualTo("Busca ISBN");
    }

//    Valida a keyword IgnoreCase e Garante que o banco está fazendo a comparação insensível a maiúsculas/minúscula
    @Test
    @DisplayName("CSU01: Deve buscar por Título ignorando Case (Query Method)")
    void deveBuscarPorTitulo() {
        Livro livro = new LivroCapaDura("Harry Potter", "123", 100, 2020, "R", 10, BigDecimal.TEN, null, null, null);
        livroRepository.save(livro);

        List<Livro> encontrados = livroRepository.findByTituloContainingIgnoreCase("harry");

        assertThat(encontrados).isNotEmpty();
        assertThat(encontrados.get(0).getTitulo()).isEqualTo("Harry Potter");
    }

}
