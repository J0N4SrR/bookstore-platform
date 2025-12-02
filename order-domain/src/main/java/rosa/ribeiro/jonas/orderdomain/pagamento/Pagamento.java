package rosa.ribeiro.jonas.orderdomain.pagamento;


import jakarta.persistence.*;
import rosa.ribeiro.jonas.orderdomain.pedido.Pedido;

import java.math.BigDecimal;

@Entity
@Table(name = "pagamentos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_pagamento")
public abstract class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    private BigDecimal valorOriginal;
    private BigDecimal valorFinal;

    protected Pagamento() {}

    public Pagamento(Pedido pedido, BigDecimal valorOriginal) {
        this.pedido = pedido;
        this.valorOriginal = valorOriginal;
        this.status = StatusPagamento.PENDENTE;
    }

    public abstract void processarDesconto();

    // Getters e Setters
    public BigDecimal getValorFinal() { return valorFinal; }
    protected void setValorFinal(BigDecimal valorFinal) { this.valorFinal = valorFinal; }
    public BigDecimal getValorOriginal() { return valorOriginal; }
    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }
}
