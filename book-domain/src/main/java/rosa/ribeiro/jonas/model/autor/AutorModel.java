package rosa.ribeiro.jonas.model.autor;

import jakarta.persistence.*;
import rosa.ribeiro.jonas.model.livro.Livro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="autores")
public class AutorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;
    private LocalDate dataNascimento;
    private String nacionalidade;

    @ManyToMany(mappedBy = "autores")
    private List<Livro> livros = new ArrayList<>();

    protected AutorModel() {}

    public AutorModel(String nome, LocalDate dataNascimento, String nacionalidade) {
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
