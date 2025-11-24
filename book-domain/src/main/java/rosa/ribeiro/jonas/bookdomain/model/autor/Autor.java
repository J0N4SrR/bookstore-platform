package rosa.ribeiro.jonas.bookdomain.model.autor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name ="autores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;
    private LocalDate dataNascimento;
    private String nacionalidade;

    @ManyToMany(mappedBy = "autores")
    @JsonIgnore
    private List<Livro> livros = new ArrayList<>();

    protected Autor() {}

    public Autor(String nome, LocalDate dataNascimento, String nacionalidade) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.nacionalidade = nacionalidade;
    }

    public void adicionarLivro(Livro livro) {
        this.livros.add(livro);
    }

    public String getId() {
        return id;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getNome() {
        return nome;
    }
}
