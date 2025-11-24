package rosa.ribeiro.jonas.customerdomain.service;

import org.springframework.stereotype.Service;
import rosa.ribeiro.jonas.customerdomain.model.Cliente;
import rosa.ribeiro.jonas.customerdomain.repository.ClienteRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManterClienteService {
    private final ClienteRepository clienteRepository;

    public ManterClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    private void validarDuplicidade(Cliente cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF: " + cliente.getCpf());
        }
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este E-mail: " + cliente.getEmail());
        }
    }

    @Transactional
    public Cliente salvarCliente(Cliente cliente) {
        if (cliente.getId() == null) {
            validarDuplicidade(cliente);
        }

        return clienteRepository.save(cliente);
    }
    @Transactional(readOnly = true)
    public Cliente buscarPorId(String id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com E-mail: " + email));
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional
    public void deletarCliente(String id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado para exclusão.");
        }
        clienteRepository.deleteById(id);
    }

    @Transactional
    public Cliente atualizarCliente(String id, Cliente dadosNovos) {
        Cliente cliente = buscarPorId(id);
        cliente.setNomeCompleto(dadosNovos.getNomeCompleto());
        cliente.setTelefone(dadosNovos.getTelefone());
        return clienteRepository.save(cliente);
    }


}
