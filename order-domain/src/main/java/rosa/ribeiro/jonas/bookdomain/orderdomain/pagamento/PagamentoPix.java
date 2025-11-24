package rosa.ribeiro.jonas.bookdomain.orderdomain.pagamento;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import rosa.ribeiro.jonas.bookdomain.orderdomain.pedido.Pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@DiscriminatorValue("PIX")
public class PagamentoPix extends Pagamento {

    private String codigoPix;

    protected PagamentoPix() {}

    public PagamentoPix(Pedido pedido, BigDecimal valorOriginal) {
        super(pedido, valorOriginal);
        this.codigoPix = "PIX-" + java.util.UUID.randomUUID().toString();
        processarDesconto();
    }

    @Override
    public void processarDesconto() {
        BigDecimal fator = new BigDecimal("0.92");
        BigDecimal comDesconto = getValorOriginal().multiply(fator).setScale(2, RoundingMode.HALF_UP);
        setValorFinal(comDesconto);
    }

    public String getCodigoPix() { return codigoPix; }
}
