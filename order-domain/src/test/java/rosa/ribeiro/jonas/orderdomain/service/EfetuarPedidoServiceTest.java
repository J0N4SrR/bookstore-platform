package rosa.ribeiro.jonas.orderdomain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.bookdomain.service.ManterLivroService;
import rosa.ribeiro.jonas.commondomain.calculocep.service.CalculoFreteService;
import rosa.ribeiro.jonas.customerdomain.model.Cliente;
import rosa.ribeiro.jonas.customerdomain.service.ManterClienteService;
import rosa.ribeiro.jonas.orderdomain.dto.DadosItemDTO;
import rosa.ribeiro.jonas.orderdomain.dto.DadosPagamentoDTO;
import rosa.ribeiro.jonas.orderdomain.dto.DadosPedidoDTO;
import rosa.ribeiro.jonas.orderdomain.pagamento.Pagamento;
import rosa.ribeiro.jonas.orderdomain.pedido.Pedido;
import rosa.ribeiro.jonas.orderdomain.repository.PagamentoRepository;
import rosa.ribeiro.jonas.orderdomain.repository.PedidoRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EfetuarPedidoServiceTest {

    @InjectMocks
    private EfetuarPedidoService service;

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

    // --- CT01: Fluxo Principal (Pix) ---
    @Test
    @DisplayName("CT01: Deve efetuar pedido com sucesso via PIX (8% desconto implícito)")
    void ct01_FluxoPrincipal_Pix() {
        // Arrange
        String clienteId = "cli-001";
        String livroId = "book-001";
        String cep = "01001-000";

        DadosPedidoDTO dados = montarDto(clienteId, livroId, cep, "PIX", null, null);

        // Mocks
        mockClienteExistente(clienteId);
        mockLivroDisponivel(livroId);
        mockFrete(cep);
        mockSalvarPedido();

        // Factory deve retornar um Pagamento (simulado)
        Pagamento pagamentoMock = mock(Pagamento.class);
        when(pagamentoFactory.criarPagamento(any(Pedido.class), eq(dados.pagamento()))).thenReturn(pagamentoMock);

        // Act
        Pedido resultado = service.efetuarPedido(dados);

        // Assert
        assertNotNull(resultado);
        verify(manterClienteService).buscarPorId(clienteId);
        verify(manterLivroService).buscarPorId(livroId);
        verify(manterLivroService).baixarEstoque(eq(livroId), anyInt());
        verify(pedidoRepository).save(any(Pedido.class));
        verify(pagamentoFactory).criarPagamento(any(Pedido.class), eq(dados.pagamento())); // Garante que a factory recebeu o tipo PIX
        verify(pagamentoRepository).save(pagamentoMock);
    }

    // --- CT02: Fluxo Alternativo (Cartão 3x - Sem juros) ---
    @Test
    @DisplayName("CT02: Deve efetuar pedido com sucesso via Cartão 3x")
    void ct02_FluxoAlternativo_Cartao3x() {
        // Arrange
        String clienteId = "cli-002";
        String livroId = "book-002";
        String cep = "20000-000";

        DadosPedidoDTO dados = montarDto(clienteId, livroId, cep, "CARTAO", "Luke Skywalker", 3);

        // Mocks
        mockClienteExistente(clienteId);
        mockLivroDisponivel(livroId);
        mockFrete(cep);
        mockSalvarPedido();

        Pagamento pagamentoMock = mock(Pagamento.class);
        when(pagamentoFactory.criarPagamento(any(Pedido.class), eq(dados.pagamento()))).thenReturn(pagamentoMock);

        // Act
        Pedido resultado = service.efetuarPedido(dados);

        // Assert
        assertNotNull(resultado);
        verify(pagamentoFactory).criarPagamento(any(Pedido.class), eq(dados.pagamento()));
        verify(pagamentoRepository).save(pagamentoMock);
    }

    // --- CT03: Fluxo Alternativo (Cartão à Vista - 3% desc) ---
    @Test
    @DisplayName("CT03: Deve efetuar pedido com sucesso via Cartão à vista")
    void ct03_FluxoAlternativo_CartaoVista() {
        // Arrange
        String clienteId = "cli-003";
        String livroId = "book-003";
        String cep = "90000-000";

        DadosPedidoDTO dados = montarDto(clienteId, livroId, cep, "CARTAO", "Leia Organa", 1);

        // Mocks
        mockClienteExistente(clienteId);
        mockLivroDisponivel(livroId);
        mockFrete(cep);
        mockSalvarPedido();

        Pagamento pagamentoMock = mock(Pagamento.class);
        when(pagamentoFactory.criarPagamento(any(Pedido.class), eq(dados.pagamento()))).thenReturn(pagamentoMock);

        // Act
        service.efetuarPedido(dados);

        // Assert
        verify(pagamentoFactory).criarPagamento(any(Pedido.class), eq(dados.pagamento()));
        verify(pagamentoRepository).save(pagamentoMock);
    }

    // --- CT04: Fluxo Exceção (Cliente Inválido) ---
    @Test
    @DisplayName("CT04: Deve falhar quando Cliente não está cadastrado")
    void ct04_FluxoExcecao_ClienteInvalido() {
        // Arrange
        String clienteId = "cli-fantasma";
        DadosPedidoDTO dados = montarDto(clienteId, "book-001", "00000-000", "PIX", null, null);

        // Mock: Retorna NULL para simular cliente inexistente
        when(manterClienteService.buscarPorId(clienteId)).thenReturn(null);

        // Act & Assert
        SecurityException ex = assertThrows(SecurityException.class, () -> service.efetuarPedido(dados));
        assertEquals("Operação não permitida: Usuário não cadastrado ou não encontrado na base de dados.", ex.getMessage());

        verifyNoInteractions(pedidoRepository);
        verifyNoInteractions(manterLivroService);
    }

    // --- CT05: Fluxo Exceção (CEP Inválido/Nulo) ---
    @Test
    @DisplayName("CT05: Deve falhar quando CEP é inválido ou nulo")
    void ct05_FluxoExcecao_CepInvalido() {
        // Arrange
        DadosPedidoDTO dados = montarDto("cli-001", "book-001", null, "PIX", null, null);

        mockClienteExistente("cli-001");

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.efetuarPedido(dados));
        assertEquals("CEP de entrega é obrigatório.", ex.getMessage());

        verifyNoInteractions(pedidoRepository);
    }

    // --- CT06: Fluxo Exceção (Livro Inexistente) ---
    @Test
    @DisplayName("CT06: Deve falhar quando Livro não existe")
    void ct06_FluxoExcecao_LivroInexistente() {
        // Arrange
        String livroId = "book-404";
        DadosPedidoDTO dados = montarDto("cli-001", livroId, "01001-000", "PIX", null, null);

        mockClienteExistente("cli-001");
        mockFrete("01001-000");

        // Mock: Retorna NULL para livro
        when(manterLivroService.buscarPorId(livroId)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.efetuarPedido(dados));
        assertTrue(ex.getMessage().contains("não encontrado"));

        verify(pedidoRepository, never()).save(any());
    }

    // --- CT07: Fluxo Exceção (Estoque Insuficiente) ---
    @Test
    @DisplayName("CT07: Deve falhar quando Estoque é insuficiente")
    void ct07_FluxoExcecao_EstoqueInsuficiente() {
        // Arrange
        String livroId = "book-sem-estoque";
        DadosPedidoDTO dados = montarDto("cli-001", livroId, "01001-000", "PIX", null, null);

        mockClienteExistente("cli-001");
        mockFrete("01001-000");

        Livro livroMock = mockLivroDisponivel(livroId);

        // Mock: Simula erro ao tentar baixar estoque (quantidade pedida > estoque)
        doThrow(new IllegalArgumentException("Estoque insuficiente!"))
                .when(manterLivroService).baixarEstoque(eq(livroId), anyInt());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.efetuarPedido(dados));
        assertEquals("Estoque insuficiente!", ex.getMessage());

        verify(pedidoRepository, never()).save(any());
    }

    // --- Helpers (Métodos Auxiliares para limpar o código) ---

    private DadosPedidoDTO montarDto(String clienteId, String livroId, String cep, String tipoPgto, String titular, Integer parcelas) {
        DadosItemDTO item = new DadosItemDTO(livroId, 1);
        DadosPagamentoDTO pgto = new DadosPagamentoDTO(tipoPgto, titular, "1234-5678", parcelas);
        return new DadosPedidoDTO(clienteId, List.of(item), pgto, cep);
    }

    private void mockClienteExistente(String id) {
        Cliente cliente = mock(Cliente.class);
        when(manterClienteService.buscarPorId(id)).thenReturn(cliente);
    }

    private Livro mockLivroDisponivel(String id) {
        Livro livro = mock(Livro.class);
        when(livro.getId()).thenReturn(id);
        when(livro.podeSerVendido()).thenReturn(true);
        lenient().when(livro.calcularPreco()).thenReturn(BigDecimal.TEN);
        when(manterLivroService.buscarPorId(id)).thenReturn(livro);
        return livro;
    }

    private void mockFrete(String cep) {
        when(calculoFreteService.calcularFrete(cep)).thenReturn(new BigDecimal("20.00"));
    }

    private void mockSalvarPedido() {
        // Retorna o próprio pedido passado como argumento
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }
}