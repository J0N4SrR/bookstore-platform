package rosa.ribeiro.jonas.bookdomain.orderdomain.dto;

import java.util.List;

public record DadosPedidoDTO(String clienteId, List<DadosItemDTO> itens, DadosPagamentoDTO pagamento) {
}
