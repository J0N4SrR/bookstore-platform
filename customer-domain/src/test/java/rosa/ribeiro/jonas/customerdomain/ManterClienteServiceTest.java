package rosa.ribeiro.jonas.customerdomain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rosa.ribeiro.jonas.customerdomain.model.Cliente;
import rosa.ribeiro.jonas.customerdomain.repository.ClienteRepository;
import rosa.ribeiro.jonas.customerdomain.service.ManterClienteService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManterClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ManterClienteService service;

    // --- CENÁRIO FELIZ ---

    @Test
    @DisplayName("CSU03: Deve salvar novo cliente quando CPF e Email são únicos")
    void deveSalvarClienteNovo() {
        // 1. Arrange (Cenário)
        Cliente novoCliente = criarClienteExemplo();

        // Ensinamos o Mock: "Ninguém tem esse CPF nem esse Email"
        when(clienteRepository.existsByCpf(novoCliente.getCpf())).thenReturn(false);
        when(clienteRepository.existsByEmail(novoCliente.getEmail())).thenReturn(false);

        // Simulamos o salvamento
        when(clienteRepository.save(any(Cliente.class))).thenReturn(novoCliente);

        // 2. Act (Ação)
        Cliente salvo = service.salvarCliente(novoCliente);

        // 3. Assert (Verificação)
        assertNotNull(salvo);
        verify(clienteRepository).save(novoCliente); // Garante que chamou o banco
    }

    // --- CENÁRIOS DE EXCEÇÃO (Regras de Negócio) ---

    @Test
    @DisplayName("RN: Não deve salvar cliente com CPF duplicado")
    void naoDeveSalvarCpfDuplicado() {
        // 1. Arrange
        Cliente novoCliente = criarClienteExemplo();

        // O Mock diz: "Sim, já existe esse CPF!"
        when(clienteRepository.existsByCpf(novoCliente.getCpf())).thenReturn(true);

        // 2. Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            service.salvarCliente(novoCliente);
        });

        assertEquals("Já existe um cliente cadastrado com este CPF: " + novoCliente.getCpf(), ex.getMessage());

        // Segurança: Garante que NÃO tentou salvar
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("RN: Não deve salvar cliente com E-mail duplicado")
    void naoDeveSalvarEmailDuplicado() {
        // 1. Arrange
        Cliente novoCliente = criarClienteExemplo();

        // CPF ok, mas Email já existe
        when(clienteRepository.existsByCpf(novoCliente.getCpf())).thenReturn(false);
        when(clienteRepository.existsByEmail(novoCliente.getEmail())).thenReturn(true);

        // 2. Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            service.salvarCliente(novoCliente);
        });

        assertTrue(ex.getMessage().contains("Já existe um cliente cadastrado com este E-mail"));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Busca: Deve lançar erro se buscar ID inexistente")
    void deveFalharBuscaIdInexistente() {
        String id = "fantasma";
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(id));
    }

    // --- Helper ---
    private Cliente criarClienteExemplo() {
        return new Cliente(
                "Luke Skywalker",
                "111.222.333-44",
                LocalDate.of(1990, 5, 20),
                "luke@jedi.com",
                "9999-8888",
                "senha123"
        );
    }
}