package rosa.ribeiro.jonas.orderdomain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rosa.ribeiro.jonas.customerdomain.model.Cliente;
import rosa.ribeiro.jonas.customerdomain.service.ManterClienteService;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.bookdomain.model.livro.LivroCapaDura;
import rosa.ribeiro.jonas.bookdomain.service.ManterLivroService;
import rosa.ribeiro.jonas.commondomain.calculocep.service.CalculoFreteService;

import rosa.ribeiro.jonas.orderdomain.dto.DadosItemDTO;
import rosa.ribeiro.jonas.orderdomain.dto.DadosPagamentoDTO;
import rosa.ribeiro.jonas.orderdomain.dto.DadosPedidoDTO;
import rosa.ribeiro.jonas.orderdomain.pagamento.Pagamento;
import rosa.ribeiro.jonas.orderdomain.pagamento.PagamentoPix;
import rosa.ribeiro.jonas.orderdomain.pedido.Pedido;
import rosa.ribeiro.jonas.orderdomain.repository.PagamentoRepository;
import rosa.ribeiro.jonas.orderdomain.repository.PedidoRepository;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EfetuarPedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private PagamentoRepository pagamentoRepository;
    @Mock
    private ManterClienteService manterClienteService;
    @Mock
    private ManterLivroService manterLivroService;
    @Mock
    private PagamentoFactory pagamentoFactory;
    @Mock
    private CalculoFreteService calculoFreteService;

    @InjectMocks
    private EfetuarPedidoService service;

    @Test
    @DisplayName("CSU02: Deve efetuar pedido com sucesso (Pix + Frete)")
    void deveEfetuarPedidoComSucesso() {
        String clienteId = "cli123";
        String livroId = "book456";
        String cep = "12345-678";

        DadosItemDTO itemDTO = new DadosItemDTO(livroId, 2);
        DadosPagamentoDTO pgtoDTO = new DadosPagamentoDTO("PIX", null, null, null);

        DadosPedidoDTO pedidoDTO = new DadosPedidoDTO(clienteId, List.of(itemDTO), pgtoDTO, cep);

        Cliente clienteMock = new Cliente("Han Solo", "123", LocalDate.now(), "han@solo.com", "111", "senha");

        Livro livroMock = new LivroCapaDura("Estrela da Morte", "isbn", 100, 2020, "desc", 10, new BigDecimal("50.00"), null, null, null);
        setLivroId(livroMock, livroId);

        when(manterClienteService.buscarPorId(clienteId)).thenReturn(clienteMock);
        when(manterLivroService.buscarPorId(livroId)).thenReturn(livroMock);
        when(calculoFreteService.calcularFrete(cep)).thenReturn(new BigDecimal("20.00")); // Mock do Frete

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArguments()[0]);

        Pagamento pagamentoMock = new PagamentoPix(new Pedido(clienteMock), new BigDecimal("100.00"));
        when(pagamentoFactory.criarPagamento(any(), eq(pgtoDTO))).thenReturn(pagamentoMock);

        Pedido pedidoSalvo = service.efetuarPedido(pedidoDTO);

        assertNotNull(pedidoSalvo);
        assertEquals(clienteMock, pedidoSalvo.getCliente());

        assertEquals(new BigDecimal("20.00"), pedidoSalvo.getValorFrete());

        verify(calculoFreteService).calcularFrete(cep);
        verify(manterLivroService).baixarEstoque(livroId, 2);
        verify(pedidoRepository).save(any(Pedido.class));
        verify(pagamentoRepository).save(pagamentoMock);
    }

    @Test
    @DisplayName("Erro: Deve falhar se CEP não for informado")
    void deveFalharSemCep() {
        String livroId = "book-1";
        DadosItemDTO item = new DadosItemDTO(livroId, 1);

        DadosPedidoDTO dto = new DadosPedidoDTO("cli123", List.of(item), null, null); // CEP Nulo


        Cliente clienteMock = mock(Cliente.class);
        when(manterClienteService.buscarPorId(any())).thenReturn(clienteMock);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.efetuarPedido(dto));

        assertTrue(ex.getMessage().contains("CEP"), "A mensagem deveria mencionar o CEP, mas foi: " + ex.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Erro: Deve falhar se Estoque Insuficiente")
    void deveFalharEstoqueInsuficiente() {
        String livroId = "book-sem-estoque";
        String cep = "12345-000";
        DadosItemDTO itemDTO = new DadosItemDTO(livroId, 10);
        DadosPedidoDTO dto = new DadosPedidoDTO("cli-ok", List.of(itemDTO), null, cep);

        when(manterClienteService.buscarPorId(any())).thenReturn(new Cliente("Ok", "1", null, "e", "1", "s"));
        when(calculoFreteService.calcularFrete(cep)).thenReturn(BigDecimal.TEN);

        Livro livroMock = new LivroCapaDura("Java", "1", 1, 1, "d", 0, BigDecimal.TEN, null, null, null);
        setLivroId(livroMock, livroId);

        when(manterLivroService.buscarPorId(livroId)).thenReturn(livroMock);

        doThrow(new IllegalArgumentException("Estoque insuficiente"))
                .when(manterLivroService).baixarEstoque(livroId, 10);

        assertThrows(IllegalArgumentException.class, () -> service.efetuarPedido(dto));

        verify(pedidoRepository, never()).save(any());
    }

    private void setLivroId(Livro livro, String id) {
        try {
            Field field = Livro.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(livro, id);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao setar ID", e);
        }
    }
}