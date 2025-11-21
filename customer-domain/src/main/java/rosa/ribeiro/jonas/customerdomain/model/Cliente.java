package rosa.ribeiro.jonas.customerdomain.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nomeCompleto;
    @Column(unique = true, nullable = false)
    private String cpf;
    private LocalDate dataNascimento;
    @Column(unique = true, nullable = false)
    private String email;
    private String telefone;
    private String senha;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private List<Endereco> enderecos = new ArrayList<>();

    protected Cliente() {}

    public Cliente(String nomeCompleto, String cpf, LocalDate dataNascimento, String email, String telefone, String senha) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    public void adicionarEndereco(Endereco endereco) {
        if (endereco == null) throw new IllegalArgumentException("Endereço inválido");
        this.enderecos.add(endereco);
    }

    public String getId() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getSenha() {
        return senha;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }
}
