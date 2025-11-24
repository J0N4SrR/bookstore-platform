package rosa.ribeiro.jonas.bookdomain.orderdomain.pedido;

import jakarta.persistence.*;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    protected ItemPedido() {}

    public ItemPedido(Livro livro, Integer quantidade) {
        this.livro = livro;
        this.quantidade = quantidade;
        this.precoUnitario = livro.calcularPreco();
    }

    public BigDecimal calcularSubtotal() {
        if (precoUnitario == null || quantidade == null) return BigDecimal.ZERO;
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getId() { return id; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public Livro getLivro() { return livro; }
}