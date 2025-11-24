package rosa.ribeiro.jonas.bookdomain.orderdomain.pagamento;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import rosa.ribeiro.jonas.bookdomain.orderdomain.pedido.Pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@DiscriminatorValue("CARTAO")
public class PagamentoCartao extends Pagamento {

    private String nomeTitular;
    private String numeroCartao;
    private Integer numeroParcelas;

    protected PagamentoCartao() {}

    public PagamentoCartao(Pedido pedido, BigDecimal valorOriginal, String nomeTitular, String numeroCartao, Integer numeroParcelas) {
        super(pedido, valorOriginal);
        this.nomeTitular = nomeTitular;
        this.numeroCartao = numeroCartao;

        if (numeroParcelas < 1 || numeroParcelas > 3) {
            throw new IllegalArgumentException("Parcelamento inválido. Máximo 3x.");
        }
        this.numeroParcelas = numeroParcelas;

        processarDesconto();
    }

    @Override
    public void processarDesconto() {
        if (this.numeroParcelas == 1) {
            BigDecimal fator = new BigDecimal("0.97");
            setValorFinal(getValorOriginal().multiply(fator).setScale(2, RoundingMode.HALF_UP));
        } else {
            setValorFinal(getValorOriginal());
        }
    }

    public Integer getNumeroParcelas() { return numeroParcelas; }
}