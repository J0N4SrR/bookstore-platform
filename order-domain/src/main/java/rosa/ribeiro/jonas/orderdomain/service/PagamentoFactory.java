package rosa.ribeiro.jonas.orderdomain.service;

import org.springframework.stereotype.Component;
import rosa.ribeiro.jonas.bookdomain.orderdomain.dto.DadosPagamentoDTO;
import rosa.ribeiro.jonas.bookdomain.orderdomain.pagamento.Pagamento;
import rosa.ribeiro.jonas.bookdomain.orderdomain.pagamento.PagamentoCartao;
import rosa.ribeiro.jonas.bookdomain.orderdomain.pagamento.PagamentoPix;
import rosa.ribeiro.jonas.bookdomain.orderdomain.pedido.Pedido;

@Component
public class PagamentoFactory {
    public Pagamento criarPagamento(Pedido pedido, DadosPagamentoDTO dados){
        if("PIX".equalsIgnoreCase(dados.tipo())){
            return new PagamentoPix(pedido, pedido.getValorTotal());
        }
         else if("CARTAO".equalsIgnoreCase(dados.tipo())){
            return new PagamentoCartao(
                    pedido,
                    pedido.getValorTotal(),
                    dados.nomeTitular(),
                    dados.numeroCartao(),
                    dados.parcelas()
            );
        } else {
            throw new IllegalArgumentException("Forma de pagamento não suportada: " + dados.tipo());
        }
    }
}
