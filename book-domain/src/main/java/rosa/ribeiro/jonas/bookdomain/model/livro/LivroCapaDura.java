package rosa.ribeiro.jonas.bookdomain.model.livro;

import rosa.ribeiro.jonas.bookdomain.model.autor.Autor;
import rosa.ribeiro.jonas.bookdomain.model.categoria.Categoria;
import rosa.ribeiro.jonas.bookdomain.model.editora.Editora;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("CAPA_DURA")
public class LivroCapaDura extends Livro{

    protected LivroCapaDura() {}

    public LivroCapaDura(String titulo, String isbn, int numPaginas, int anoPublicacao, String resumo, int quantidadeEstoque, BigDecimal precoBase, Editora editora, List<Autor> autores, Set<Categoria> categorias) {
        super(titulo, isbn, numPaginas, anoPublicacao, resumo, quantidadeEstoque, precoBase, editora, autores, categorias);
    }

    @Override
    public BigDecimal calcularPreco() {
        return getPrecoBase().setScale(2, RoundingMode.HALF_UP);
    }
}
