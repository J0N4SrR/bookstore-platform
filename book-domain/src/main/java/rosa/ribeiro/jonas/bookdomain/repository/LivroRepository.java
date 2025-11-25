package rosa.ribeiro.jonas.bookdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.bookdomain.model.livro.StatusLivro;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, String> {


    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    Optional<Livro> findByIsbn(String isbn);
    List<Livro> findByStatus(StatusLivro status);

    List<Livro> findByAutoresNomeContainingIgnoreCase(String nomeAutor);

    List<Livro> findByEditoraNomeContainingIgnoreCase(String nomeEditora);

    List<Livro> findByCategoriasNomeContainingIgnoreCase(String nomeCategoria);

    @Query("SELECT l FROM Livro l WHERE l.quantidadeEstoque <= :limite AND l.status = 'DISPONIVEL'")
    List<Livro> findLivrosComEstoqueBaixo(@Param("limite") int limite);

}
