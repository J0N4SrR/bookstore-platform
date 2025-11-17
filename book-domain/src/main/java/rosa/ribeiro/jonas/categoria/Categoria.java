package rosa.ribeiro.jonas.categoria;

import jakarta.persistence.*;
import rosa.ribeiro.jonas.livro.Livro;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "categorias")
    private Set<Livro> livros = new HashSet<>();

    protected Categoria() {}

    public Categoria(String nome) {
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
