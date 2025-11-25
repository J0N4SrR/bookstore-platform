package rosa.ribeiro.jonas.orderdomain.dto;

import java.util.List;

public record DadosPedidoDTO(String clienteId, List<DadosItemDTO> itens, DadosPagamentoDTO pagamento, String cepEntrega) {
}
