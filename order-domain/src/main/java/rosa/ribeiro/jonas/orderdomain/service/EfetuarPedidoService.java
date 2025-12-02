package rosa.ribeiro.jonas.orderdomain.service;

import jakarta.transaction.Transactional;
import rosa.ribeiro.jonas.commondomain.calculocep.service.CalculoFreteService;
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

import java.math.BigDecimal;

@Service
public class EfetuarPedidoService {
    private final PedidoRepository pedidoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ManterClienteService manterClienteService;
    private final ManterLivroService manterLivroService;
    private final PagamentoFactory pagamentoFactory;
    private final CalculoFreteService calculoFreteService;

    public EfetuarPedidoService(PedidoRepository pedidoRepository,
                                PagamentoRepository pagamentoRepository,
                                ManterClienteService manterClienteService,
                                ManterLivroService manterLivroService,
                                PagamentoFactory pagamentoFactory,
                                CalculoFreteService calculoFreteService) {
        this.pedidoRepository = pedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.manterClienteService = manterClienteService;
        this.manterLivroService = manterLivroService;
        this.pagamentoFactory = pagamentoFactory;
        this.calculoFreteService = calculoFreteService;
    }

    @Transactional
    public Pedido efetuarPedido(DadosPedidoDTO dados) {
        if (dados.itens() == null || dados.itens().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item.");
        }

        Cliente cliente = manterClienteService.buscarPorId(dados.clienteId());

        if (cliente == null) {
            throw new SecurityException("Operação não permitida: Usuário não cadastrado ou não encontrado na base de dados.");
        }

        Pedido pedido = new Pedido(cliente);

        if (dados.cepEntrega() == null || dados.cepEntrega().isBlank()) {
            throw new IllegalArgumentException("CEP de entrega é obrigatório.");
        }

        BigDecimal valorFrete = calculoFreteService.calcularFrete(dados.cepEntrega());

        pedido.definirFrete(valorFrete, 5, dados.cepEntrega());

        for (DadosItemDTO itemDTO : dados.itens()) {
            Livro livro = manterLivroService.buscarPorId(itemDTO.livroId());

            // Validação se o livro existe no banco
            if (livro == null) {
                throw new IllegalArgumentException("Livro com ID " + itemDTO.livroId() + " não encontrado.");
            }

            if (!livro.podeSerVendido()) {
                throw new IllegalStateException(
                        String.format("O livro '%s' não está disponível para venda. (Status: %s)",
                                livro.getTitulo(), livro.getStatus().getDescricao())
                );
            }
            manterLivroService.baixarEstoque(livro.getId(), itemDTO.quantidade());

            pedido.adicionarItem(new ItemPedido(livro, itemDTO.quantidade()));
        }

        pedido.calcularValorTotal();
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



