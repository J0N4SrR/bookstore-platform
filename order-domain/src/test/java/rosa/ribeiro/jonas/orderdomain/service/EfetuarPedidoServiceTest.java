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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Classe de Teste Unitário para EfetuarPedidoService.
 * * <p><strong>Abordagem:</strong> Estrutural (Caixa Branca).
 * O objetivo é garantir a cobertura de todos os caminhos lógicos (statements/branches) do Service.</p>
 * * <p><strong>Técnicas Funcionais Aplicadas na escolha dos dados:</strong>
 * <ul>
 * <li>Partição de Equivalência (PE): Entradas válidas vs Inválidas.</li>
 * <li>Análise de Valor Limite (AVL): Listas vazias, Estoque insuficiente.</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class EfetuarPedidoServiceTest {

    @InjectMocks
    private EfetuarPedidoService service;

    @Mock private PedidoRepository pedidoRepository;
    @Mock private PagamentoRepository pagamentoRepository;
    @Mock private ManterClienteService manterClienteService;
    @Mock private ManterLivroService manterLivroService;
    @Mock private PagamentoFactory pagamentoFactory;
    @Mock private CalculoFreteService calculoFreteService;

    // --- TESTES DE VALOR LIMITE (Boundary Value Analysis) ---

    /**
     * CT00: Validação de Lista Vazia.
     * <br><strong>Técnica:</strong> Análise de Valor Limite (AVL).
     * <br><strong>Objetivo:</strong> Testar o limite inferior da lista de itens (0 itens).
     * <br><strong>Caminho:</strong> Interrompe no primeiro 'if' do método.
     */
    @Test
    @DisplayName("CT00: Deve falhar se a lista de itens estiver vazia (Valor Limite)")
    void ct00_ValorLimite_ListaVazia() {
        // Arrange
        DadosPedidoDTO dadosSemItens = new DadosPedidoDTO("cli-001", Collections.emptyList(), null, "01001-000");

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.efetuarPedido(dadosSemItens));
        assertEquals("O pedido deve conter ao menos um item.", ex.getMessage());

        // Verifica que nada foi chamado (Isolamento)
        verifyNoInteractions(manterClienteService);
    }

    // --- TESTES DE PARTIÇÃO DE EQUIVALÊNCIA (Classes Válidas) ---

    /**
     * CT01: Fluxo Principal (Caminho Feliz).
     * <br><strong>Técnica:</strong> Partição de Equivalência (Classe Válida).
     * <br><strong>Cenário:</strong> Todos os dados corretos, pagamento PIX.
     * <br><strong>Cobertura:</strong> Percorre o fluxo completo de persistência.
     */
    @Test
    @DisplayName("CT01: Deve efetuar pedido com sucesso via PIX")
    void ct01_FluxoPrincipal_Pix() {
        // Arrange
        String clienteId = "cli-001";
        String livroId = "book-001";
        DadosPedidoDTO dados = montarDto(clienteId, livroId, "01001-000", "PIX", null, null);

        // Mocks (Stubs) para simular o comportamento de sucesso
        mockClienteExistente(clienteId);
        mockLivroDisponivel(livroId);
        mockFrete("01001-000");
        mockSalvarPedido();

        Pagamento pagamentoMock = mock(Pagamento.class);
        when(pagamentoFactory.criarPagamento(any(Pedido.class), eq(dados.pagamento()))).thenReturn(pagamentoMock);

        // Act
        Pedido resultado = service.efetuarPedido(dados);

        // Assert
        assertNotNull(resultado);
        verify(manterLivroService).baixarEstoque(eq(livroId), anyInt()); // Verifica efeito colateral
        verify(pedidoRepository).save(any(Pedido.class));
    }

    /**
     * CT02: Fluxo Alternativo (Cartão Parcelado).
     * <br><strong>Técnica:</strong> Partição de Equivalência (Classe Válida - Variação de Pagamento).
     */
    @Test
    @DisplayName("CT02: Deve efetuar pedido com sucesso via Cartão 3x")
    void ct02_FluxoAlternativo_Cartao3x() {
        // Arrange
        String clienteId = "cli-002";
        DadosPedidoDTO dados = montarDto(clienteId, "book-002", "20000-000", "CARTAO", "Luke", 3);

        mockClienteExistente(clienteId);
        mockLivroDisponivel("book-002");
        mockFrete("20000-000");
        mockSalvarPedido();

        Pagamento pagamentoMock = mock(Pagamento.class);
        when(pagamentoFactory.criarPagamento(any(Pedido.class), eq(dados.pagamento()))).thenReturn(pagamentoMock);

        // Act
        service.efetuarPedido(dados);

        // Assert
        verify(pagamentoFactory).criarPagamento(any(Pedido.class), eq(dados.pagamento()));
        verify(pagamentoRepository).save(pagamentoMock);
    }

    /**
     * CT03: Fluxo Alternativo (Cartão à Vista).
     * <br><strong>Técnica:</strong> Partição de Equivalência (Classe Válida).
     */
    @Test
    @DisplayName("CT03: Deve efetuar pedido com sucesso via Cartão à vista")
    void ct03_FluxoAlternativo_CartaoVista() {
        String clienteId = "cli-003";
        DadosPedidoDTO dados = montarDto(clienteId, "book-003", "90000-000", "CARTAO", "Leia", 1);

        mockClienteExistente(clienteId);
        mockLivroDisponivel("book-003");
        mockFrete("90000-000");
        mockSalvarPedido();

        Pagamento pagamentoMock = mock(Pagamento.class);
        when(pagamentoFactory.criarPagamento(any(Pedido.class), eq(dados.pagamento()))).thenReturn(pagamentoMock);

        service.efetuarPedido(dados);

        verify(pagamentoRepository).save(pagamentoMock);
    }

    // --- TESTES DE PARTIÇÃO DE EQUIVALÊNCIA (Classes Inválidas / Exceções) ---

    /**
     * CT04: Cliente Inexistente.
     * <br><strong>Técnica:</strong> Partição de Equivalência (Classe Inválida - Segurança).
     * <br><strong>Caminho:</strong> Desvio no 'if (cliente == null)'.
     */
    @Test
    @DisplayName("CT04: Deve falhar quando Cliente não está cadastrado")
    void ct04_FluxoExcecao_ClienteInvalido() {
        String clienteId = "cli-fake";
        DadosPedidoDTO dados = montarDto(clienteId, "book-001", "00000-000", "PIX", null, null);

        // Stub: Retorna NULL para simular não encontrado
        when(manterClienteService.buscarPorId(clienteId)).thenReturn(null);

        SecurityException ex = assertThrows(SecurityException.class, () -> service.efetuarPedido(dados));
        assertEquals("Operação não permitida: Usuário não cadastrado ou não encontrado na base de dados.", ex.getMessage());

        verifyNoInteractions(pedidoRepository);
    }

    /**
     * CT05: CEP Inválido.
     * <br><strong>Técnica:</strong> Partição de Equivalência (Dado Obrigatório Ausente).
     */
    @Test
    @DisplayName("CT05: Deve falhar quando CEP é inválido ou nulo")
    void ct05_FluxoExcecao_CepInvalido() {
        DadosPedidoDTO dados = montarDto("cli-001", "book-001", null, "PIX", null, null);
        mockClienteExistente("cli-001");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.efetuarPedido(dados));
        assertEquals("CEP de entrega é obrigatório.", ex.getMessage());
    }

    /**
     * CT06: Livro Inexistente.
     * <br><strong>Técnica:</strong> Partição de Equivalência (Integridade Referencial).
     */
    @Test
    @DisplayName("CT06: Deve falhar quando Livro não existe")
    void ct06_FluxoExcecao_LivroInexistente() {
        String livroId = "book-404";
        DadosPedidoDTO dados = montarDto("cli-001", livroId, "01001-000", "PIX", null, null);

        mockClienteExistente("cli-001");
        mockFrete("01001-000");

        // Stub: Retorna NULL
        when(manterLivroService.buscarPorId(livroId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.efetuarPedido(dados));
        assertTrue(ex.getMessage().contains("não encontrado"));
    }

    /**
     * CT07: Estoque Insuficiente.
     * <br><strong>Técnica:</strong> Análise de Valor Limite (Limite de Estoque).
     * <br><strong>Cenário:</strong> Tenta baixar quantidade maior que a disponível (simulado pela exceção).
     */
    @Test
    @DisplayName("CT07: Deve falhar quando Estoque é insuficiente")
    void ct07_FluxoExcecao_EstoqueInsuficiente() {
        String livroId = "book-sem-estoque";
        DadosPedidoDTO dados = montarDto("cli-001", livroId, "01001-000", "PIX", null, null);

        mockClienteExistente("cli-001");
        mockFrete("01001-000");
        mockLivroDisponivel(livroId); // Prepara o mock (com lenient)

        // Mock: Simula falha na regra de negócio de estoque
        doThrow(new IllegalArgumentException("Estoque insuficiente!"))
                .when(manterLivroService).baixarEstoque(eq(livroId), anyInt());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.efetuarPedido(dados));
        assertEquals("Estoque insuficiente!", ex.getMessage());

        verify(pedidoRepository, never()).save(any());
    }

    // --- Helpers ---

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
        // Lenient: Permite que este stub não seja usado em testes de falha prematura
        lenient().when(livro.calcularPreco()).thenReturn(BigDecimal.TEN);
        when(manterLivroService.buscarPorId(id)).thenReturn(livro);
        return livro;
    }

    private void mockFrete(String cep) {
        when(calculoFreteService.calcularFrete(cep)).thenReturn(new BigDecimal("20.00"));
    }

    private void mockSalvarPedido() {
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }
}

