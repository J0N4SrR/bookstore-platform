package rosa.ribeiro.jonas.bookdomain.orderdomain.pagamento;

public enum StatusPagamento {
    PENDENTE("pendente"),
    APROVADO("aprovado"),
    RECUSADO("recusado"),
    ESTORNADO("estornado");

    private final String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
