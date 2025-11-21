package rosa.ribeiro.jonas.model.livro;

import rosa.ribeiro.jonas.model.autor.AutorModel;
import rosa.ribeiro.jonas.model.categoria.CategoriaModel;
import rosa.ribeiro.jonas.model.editora.EditoraModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("CAPA_DURA")
public class LivroCapaDura extends Livro{

    protected LivroCapaDura() {}

    public LivroCapaDura(String titulo, String isbn, int numPaginas, int anoPublicacao, String resumo, int quantidadeEstoque, BigDecimal precoBase, EditoraModel editoraModel, List<AutorModel> autores, Set<CategoriaModel> categoriaModels) {
        super(titulo, isbn, numPaginas, anoPublicacao, resumo, quantidadeEstoque, precoBase, editoraModel, autores, categoriaModels);
    }

    @Override
    public BigDecimal calcularPreco() {
        return getPrecoBase();
    }
}
