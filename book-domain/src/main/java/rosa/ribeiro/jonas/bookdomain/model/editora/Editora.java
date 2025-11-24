package rosa.ribeiro.jonas.bookdomain.model.editora;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "editoras")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Editora {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;

    @Column(unique = true)
    private String cnpj;

    private String telefone;
    private String email;

    @OneToMany(mappedBy = "editora")
    @JsonIgnore
    private List<Livro> livrosPublicados = new ArrayList<>();

    protected Editora() {}

    public Editora(String nome, String cnpj, String telefone, String email) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public List<Livro> getLivrosPublicados() {
        return livrosPublicados;
    }
}
