package rosa.ribeiro.jonas.bookdomain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rosa.ribeiro.jonas.bookdomain.model.editora.Editora;
import rosa.ribeiro.jonas.bookdomain.repository.EditoraRepository;

import java.util.List;

@Service
public class ManterEditoraService {

    private final EditoraRepository repository;

    public ManterEditoraService(EditoraRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Editora salvar(Editora editora) {
        return repository.save(editora);
    }

    @Transactional(readOnly = true)
    public List<Editora> listarTodas() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Editora buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada com ID: " + id));
    }

    @Transactional
    public void deletar(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Editora não encontrada para exclusão.");
        }

        repository.deleteById(id);
    }

    @Transactional
    public Editora atualizar(String id, Editora dadosNovos) {
        Editora editora = buscarPorId(id);
        editora.setNome(dadosNovos.getNome());
        editora.setTelefone(dadosNovos.getTelefone());
        editora.setEmail(dadosNovos.getEmail());

        return repository.save(editora);
    }
}