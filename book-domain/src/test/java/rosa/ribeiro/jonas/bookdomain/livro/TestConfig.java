package rosa.ribeiro.jonas.bookdomain.livro;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Esta classe serve apenas como ponto de entrada para os testes do módulo book-domain.
 * O @DataJpaTest procura por uma classe @SpringBootApplication no pacote atual ou acima.
 * Como este é um módulo de biblioteca (não tem main), criamos esta classe fake nos testes.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "rosa.ribeiro.jonas.bookdomain.repository")
@EntityScan(basePackages = "rosa.ribeiro.jonas.bookdomain.model")
public class TestConfig {
}