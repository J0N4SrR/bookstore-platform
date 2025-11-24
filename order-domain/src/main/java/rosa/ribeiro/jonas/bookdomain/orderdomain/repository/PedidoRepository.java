package rosa.ribeiro.jonas.bookdomain.orderdomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rosa.ribeiro.jonas.bookdomain.orderdomain.pedido.Pedido;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {
    List<Pedido> findByClienteId(String clienteId);
}