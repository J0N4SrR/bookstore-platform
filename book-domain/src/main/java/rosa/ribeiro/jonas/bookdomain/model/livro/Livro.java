package rosa.ribeiro.jonas.bookdomain.model.livro;

import rosa.ribeiro.jonas.bookdomain.model.autor.Autor;
import rosa.ribeiro.jonas.bookdomain.model.editora.Editora;
import rosa.ribeiro.jonas.bookdomain.model.categoria.Categoria;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "livros")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_livro", discriminatorType = DiscriminatorType.STRING)
public abstract class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String titulo;
    private String isbn;
    private int numPaginas;
    private int anoPublicacao;
    @Column(length = 2000)
    private String resumo;
    private int quantidadeEstoque;
    private BigDecimal precoBase;

    @Enumerated(EnumType.STRING)
    private StatusLivro status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editora_id")
    private Editora editora;

    @ManyToMany
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;

    @ManyToMany
    @JoinTable(
            name = "livro_categoria",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias;

    protected Livro() {
    }

    public Livro(String titulo, String isbn, int numPaginas, int anoPublicacao, String resumo, int quantidadeEstoque, BigDecimal precoBase, Editora editora, List<Autor> autores, Set<Categoria> categorias) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.numPaginas = numPaginas;
        this.anoPublicacao = anoPublicacao;
        this.resumo = resumo;
        this.quantidadeEstoque = quantidadeEstoque;
        this.precoBase = precoBase;
        this.status = StatusLivro.DISPONIVEL;;
        this.editora = editora;
        this.autores = autores;
        this.categorias = categorias;
    }

    public abstract BigDecimal calcularPreco();

    public void decrementarEstoque(int quantidade) {
        if(quantidadeEstoque < quantidade){
            throw new IllegalArgumentException("Não há quantidade suficiente no estoque!");
        }

        quantidadeEstoque -= quantidade;

        if(verificarEstoqueMinimo()){

        }
    }

    public void incrementarEstoque(int quantidade) {
        if(quantidade < 0){
            throw new IllegalArgumentException("Quantidade nao pode ser negativa!");
        }

        quantidadeEstoque += quantidade;
    }
    public boolean verificarEstoqueMinimo(){
        return this.quantidadeEstoque <= 2;
    }

    public void mudarStatus(StatusLivro novoStatus){
        this.status = novoStatus;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public String getResumo() {
        return resumo;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public StatusLivro getStatus() {
        return status;
    }

    public Editora getEditora() {
        return editora;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }

    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public void setResumo(String resumo) { this.resumo = resumo; }

    public void setPrecoBase(BigDecimal precoBase) { this.precoBase = precoBase; }

}
