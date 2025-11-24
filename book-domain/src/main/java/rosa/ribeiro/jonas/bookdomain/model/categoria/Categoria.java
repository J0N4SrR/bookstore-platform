package rosa.ribeiro.jonas.bookdomain.model.categoria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "categorias")
    @JsonIgnore
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
