package rosa.ribeiro.jonas.bookdomain.model.funcionario;

import jakarta.persistence.*;

@Entity
@Table(name = "funcionarios")
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nome;
    private String matricula;

    protected Funcionario() {}

    public Funcionario(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }
    public String getId() { return id; }
    public String getNome() { return nome; }
}