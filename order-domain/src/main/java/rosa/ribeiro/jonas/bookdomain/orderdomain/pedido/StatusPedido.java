package rosa.ribeiro.jonas.bookdomain.orderdomain.pedido;

public enum StatusPedido {
    EM_PROCESSAMENTO("Em processamento"),
    PAGAMENTO_PENDENTE("Pagamento Pendente"),
    CONFIRMADO("Confirmado"),
    EM_TRANSPORTE("Em Transporte"),
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
