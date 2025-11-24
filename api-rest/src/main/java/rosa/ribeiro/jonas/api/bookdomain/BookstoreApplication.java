package rosa.ribeiro.jonas.api.bookdomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "rosa.ribeiro.jonas")
@EnableJpaRepositories(basePackages = "rosa.ribeiro.jonas")
@EntityScan(basePackages = "rosa.ribeiro.jonas")
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }
}