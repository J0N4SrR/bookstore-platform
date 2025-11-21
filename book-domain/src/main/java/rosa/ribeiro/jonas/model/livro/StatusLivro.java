package rosa.ribeiro.jonas.model.livro;

public enum StatusLivro {
    DISPONIVEL("disponivel"),
    INDISPONIVEL("indisponivel"),
    FORADECIRCULACAO("Fora de Circulação");

    private final String descricao;

    StatusLivro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
