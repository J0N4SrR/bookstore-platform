package rosa.ribeiro.jonas.orderdomain.pedido;


import jakarta.persistence.*;
import rosa.ribeiro.jonas.customerdomain.model.Cliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private BigDecimal valorTotal = BigDecimal.ZERO;
    private BigDecimal valorFrete;
    private Integer prazoEntregaDias;
    private String enderecoEntregaSnapshot;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    protected Pedido() {}

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.EM_PROCESSAMENTO;
    }


    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
        calcularValorTotal();
    }

    public void definirFrete(BigDecimal valor, Integer dias, String cep) {
        this.valorFrete = valor;
        this.prazoEntregaDias = dias;
        this.enderecoEntregaSnapshot = cep;
    }

    public void calcularValorTotal() {
        BigDecimal subtotalItens = itens.stream()
                .map(ItemPedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal frete = (valorFrete != null) ? valorFrete : BigDecimal.ZERO;

        this.valorTotal = subtotalItens.add(frete);
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
    }

    // Getters
    public String getId() { return id; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public StatusPedido getStatus() { return status; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return itens; }
    public BigDecimal getValorFrete() { return valorFrete; }
}