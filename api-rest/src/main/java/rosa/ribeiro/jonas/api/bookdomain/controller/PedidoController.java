package rosa.ribeiro.jonas.api.bookdomain.controller;

import org.springframework.http.HttpStatus; // Importe o HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rosa.ribeiro.jonas.orderdomain.dto.AtualizacaoStatusDTO;
import rosa.ribeiro.jonas.orderdomain.dto.DadosPedidoDTO;
import rosa.ribeiro.jonas.orderdomain.pedido.Pedido;
import rosa.ribeiro.jonas.orderdomain.pedido.StatusPedido;
import rosa.ribeiro.jonas.orderdomain.service.EfetuarPedidoService;
import rosa.ribeiro.jonas.orderdomain.repository.PedidoRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final EfetuarPedidoService efetuarPedidoService;
    private final PedidoRepository pedidoRepository;

    public PedidoController(EfetuarPedidoService efetuarPedidoService, PedidoRepository pedidoRepository) {
        this.efetuarPedidoService = efetuarPedidoService;
        this.pedidoRepository = pedidoRepository;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody DadosPedidoDTO dados) {
        try {
            Pedido pedidoSalvo = efetuarPedidoService.efetuarPedido(dados);
            return ResponseEntity.created(URI.create("/api/pedidos/" + pedidoSalvo.getId())).body(pedidoSalvo);

        } catch (SecurityException e) {
            // Captura o erro de Usuário Inexistente e retorna 403 Forbidden com a mensagem
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        } catch (IllegalStateException e) {
            // Captura a RN01 (Livro Indisponível) e retorna 409 Conflict com a mensagem
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        } catch (IllegalArgumentException e) {
            // Captura erros de validação básica (Dados nulos) e retorna 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable String id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable String clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable String id, @RequestBody AtualizacaoStatusDTO dto) {
        try {
            StatusPedido novoStatus = StatusPedido.valueOf(dto.status().toUpperCase());
            Pedido atualizado = efetuarPedidoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}