package rosa.ribeiro.jonas.orderdomain.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.orderdomain.dto.DadosItemDTO;
import rosa.ribeiro.jonas.orderdomain.dto.DadosPedidoDTO;
import rosa.ribeiro.jonas.bookdomain.service.ManterLivroService;
import rosa.ribeiro.jonas.customerdomain.model.Cliente;
import rosa.ribeiro.jonas.customerdomain.service.ManterClienteService;

import rosa.ribeiro.jonas.orderdomain.pagamento.Pagamento;
import rosa.ribeiro.jonas.orderdomain.pedido.ItemPedido;
import rosa.ribeiro.jonas.orderdomain.pedido.Pedido;
import rosa.ribeiro.jonas.orderdomain.pedido.StatusPedido;
import rosa.ribeiro.jonas.orderdomain.repository.PagamentoRepository;
import rosa.ribeiro.jonas.orderdomain.repository.PedidoRepository;

@Service
public class EfetuarPedidoService {
    private final PedidoRepository pedidoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ManterClienteService manterClienteService;
    private final ManterLivroService manterLivroService;
    private final PagamentoFactory pagamentoFactory;

    public EfetuarPedidoService(PedidoRepository pedidoRepository,
                                PagamentoRepository pagamentoRepository,
                                ManterClienteService manterClienteService,
                                ManterLivroService manterLivroService,
                                PagamentoFactory pagamentoFactory) {
        this.pedidoRepository = pedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.manterClienteService = manterClienteService;
        this.manterLivroService = manterLivroService;
        this.pagamentoFactory = pagamentoFactory;
    }

    @Transactional
    public Pedido efetuarPedido(DadosPedidoDTO dados) {
        Cliente cliente = manterClienteService.buscarPorId(dados.clienteId());

        Pedido pedido = new Pedido(cliente);

        for (DadosItemDTO itemDTO : dados.itens()) {
            Livro livro = manterLivroService.buscarPorId(itemDTO.livroId());
            manterLivroService.baixarEstoque(livro.getId(), itemDTO.quantidade());
            pedido.adicionarItem(new ItemPedido(livro, itemDTO.quantidade()));
        }

        pedido = pedidoRepository.save(pedido);

        Pagamento pagamento = pagamentoFactory.criarPagamento(pedido, dados.pagamento());
        pagamentoRepository.save(pagamento);

        return pedido;
    }

    @Transactional
    public Pedido atualizarStatus(String id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        pedido.atualizarStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
}



