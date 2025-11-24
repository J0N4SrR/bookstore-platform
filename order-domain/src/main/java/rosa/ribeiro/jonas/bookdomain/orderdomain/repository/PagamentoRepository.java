package rosa.ribeiro.jonas.bookdomain.orderdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rosa.ribeiro.jonas.bookdomain.orderdomain.pagamento.Pagamento;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
}