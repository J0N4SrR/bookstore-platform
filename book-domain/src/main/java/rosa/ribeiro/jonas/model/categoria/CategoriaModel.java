package rosa.ribeiro.jonas.model.categoria;

import jakarta.persistence.*;
import rosa.ribeiro.jonas.model.livro.Livro;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
public class CategoriaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "categorias")
    private Set<Livro> livros = new HashSet<>();

    protected CategoriaModel() {}

    public CategoriaModel(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public Set<Livro> getLivros() {
        return livros;
    }

    public String getNome() {
        return nome;
    }
}
