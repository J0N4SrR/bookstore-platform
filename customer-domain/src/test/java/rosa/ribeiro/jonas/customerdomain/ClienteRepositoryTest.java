package rosa.ribeiro.jonas.customerdomain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rosa.ribeiro.jonas.customerdomain.model.Cliente;
import rosa.ribeiro.jonas.customerdomain.repository.ClienteRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve encontrar cliente por Email (Login)")
    void deveEncontrarPorEmail() {
        Cliente cliente = new Cliente("Leia", "123", LocalDate.now(), "leia@rebel.com", "111", "senha");
        clienteRepository.save(cliente);

        Optional<Cliente> encontrado = clienteRepository.findByEmail("leia@rebel.com");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNomeCompleto()).isEqualTo("Leia");
    }
}