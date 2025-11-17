package rosa.ribeiro.jonas.editora;

import jakarta.persistence.*;
import rosa.ribeiro.jonas.livro.Livro;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "editoras")
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
