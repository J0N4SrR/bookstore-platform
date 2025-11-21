package rosa.ribeiro.jonas.model.livro;

import rosa.ribeiro.jonas.model.autor.AutorModel;
import rosa.ribeiro.jonas.model.editora.EditoraModel;
import rosa.ribeiro.jonas.model.categoria.CategoriaModel;
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
    private EditoraModel editoraModel;

    @ManyToMany
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<AutorModel> autores;

    @ManyToMany
    @JoinTable(
            name = "livro_categoria",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<CategoriaModel> categoriaModels;

    protected Livro() {
    }

    public Livro(String titulo, String isbn, int numPaginas, int anoPublicacao, String resumo, int quantidadeEstoque, BigDecimal precoBase, EditoraModel editoraModel, List<AutorModel> autores, Set<CategoriaModel> categoriaModels) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.numPaginas = numPaginas;
        this.anoPublicacao = anoPublicacao;
        this.resumo = resumo;
        this.quantidadeEstoque = quantidadeEstoque;
        this.precoBase = precoBase;
        this.status = StatusLivro.DISPONIVEL;;
        this.editoraModel = editoraModel;
        this.autores = autores;
        this.categoriaModels = categoriaModels;
    }

    public abstract BigDecimal calcularPreco();

    public void decrementarEstoque(int quantidade) throws Exception {
        if(quantidadeEstoque < quantidade){
            throw new Exception("Não há quantidade suficiente no estoque!");
        }

        quantidadeEstoque-=quantidade;

        if(verificarEstoqueMinimo()){
            System.out.println("Atingido estoque minimo!");
        }
    }

    public void incrementarEstoque(int quantidade) throws Exception {
        if(quantidade < 0){
            throw new Exception("Quantidade nao pode ser negativa!");
        }

        quantidadeEstoque+=quantidade;
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

    public EditoraModel getEditora() {
        return editoraModel;
    }

    public List<AutorModel> getAutores() {
        return autores;
    }

    public Set<CategoriaModel> getCategorias() {
        return categoriaModels;
    }
}
