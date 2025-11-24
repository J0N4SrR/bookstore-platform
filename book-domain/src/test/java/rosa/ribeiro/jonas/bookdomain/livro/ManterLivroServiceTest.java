package rosa.ribeiro.jonas.bookdomain.livro;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.bookdomain.model.livro.LivroCapaDura;
import rosa.ribeiro.jonas.bookdomain.repository.LivroRepository;
import rosa.ribeiro.jonas.bookdomain.service.ManterLivroService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManterLivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private ManterLivroService service;

    // --- TESTES DE FLUXO DE SUCESSO (Caminho Feliz) ---

    @Test
    @DisplayName("CSU05: Deve salvar um novo livro quando ISBN é único")
    void deveSalvarLivroNovo() {
        Livro novoLivro = criarLivroExemplo(null); // ID null = Novo

        when(livroRepository.findByIsbn(novoLivro.getIsbn())).thenReturn(Optional.empty());
        when(livroRepository.save(any(Livro.class))).thenAnswer(i -> {
            Livro l = i.getArgument(0);
            return l;
        });
        Livro salvo = service.salvarLivro(novoLivro);

        assertNotNull(salvo);
        verify(livroRepository, times(1)).save(novoLivro);
    }

    @Test
    @DisplayName("RN03: Deve atualizar estoque corretamente")
    void deveAtualizarEstoque() {
        String id = "123";
        Livro livroNoBanco = criarLivroExemplo(id);
        when(livroRepository.findById(id)).thenReturn(Optional.of(livroNoBanco));
        service.adicionarEstoque(id, 5);
        assertEquals(15, livroNoBanco.getQuantidadeEstoque());
        verify(livroRepository).save(livroNoBanco);
    }

    @Test
    @DisplayName("CSU05: Deve deletar livro existente")
    void deveDeletarLivro() {
        String id = "123";
        when(livroRepository.existsById(id)).thenReturn(true);
        service.deletarLivro(id);
        verify(livroRepository).deleteById(id);
    }

    // --- TESTES DE FLUXO DE EXCEÇÃO (Regras de Negócio e Erros) ---

    @Test
    @DisplayName("Regra de Negócio: Não deve salvar livro com ISBN duplicado")
    void naoDeveSalvarIsbnDuplicado() {
        Livro novoLivro = criarLivroExemplo(null);
        Livro livroExistente = criarLivroExemplo("outro-id");
        when(livroRepository.findByIsbn(novoLivro.getIsbn())).thenReturn(Optional.of(livroExistente));
        assertThrows(IllegalArgumentException.class, () -> {
            service.salvarLivro(novoLivro);
        });
        verify(livroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Erro: Deve falhar ao tentar atualizar estoque de livro inexistente")
    void deveFalharEstoqueLivroInexistente() {
        String id = "fantasma";
        when(livroRepository.findById(id)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            service.adicionarEstoque(id, 5);
        });

        assertTrue(ex.getMessage().contains("Livro não encontrado"));
    }

    @Test
    @DisplayName("Erro: Deve falhar ao deletar ID inexistente")
    void deveFalharDeletarIdInexistente() {
        String id = "fantasma";
        when(livroRepository.existsById(id)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.deletarLivro(id));
        verify(livroRepository, never()).deleteById(any());
    }

    // --- MÉTODOS AUXILIARES (Para deixar o teste limpo) ---

    private Livro criarLivroExemplo(String id) {
        Livro livro = new LivroCapaDura(
                "Java Clean Code",
                "978-85-508-1148-2",
                400,
                2020,
                "Um bom livro",
                10, // Estoque inicial
                new BigDecimal("100.00"),
                null, null, null
        );

        if (id != null) {
       }
        return livro;
    }
}