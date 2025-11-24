package rosa.ribeiro.jonas.bookdomain.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.bookdomain.repository.LivroRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ManterLivroService {
    private final LivroRepository livroRepository;

    public ManterLivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Transactional
    public Livro salvarLivro(Livro livro) {
        if (livro.getId() == null) {
            Optional<Livro> livroExistente = livroRepository.findByIsbn(livro.getIsbn());
            if (livroExistente.isPresent()) {
                throw new IllegalArgumentException("Já existe um livro cadastrado com este ISBN: " + livro.getIsbn());
            }
        }
        return livroRepository.save(livro);
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Livro buscarPorId(String id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado com o ID: " + id));
    }

    @Transactional
    public void deletarLivro(String id) {
        if (!livroRepository.existsById(id)) {
            throw new IllegalArgumentException("Livro não encontrado para exclusão.");
        }
        livroRepository.deleteById(id);
    }

    @Transactional
    public void adicionarEstoque(String id, int quantidade) {
        Livro livro = buscarPorId(id);
        try {
            livro.incrementarEstoque(quantidade);
            livroRepository.save(livro);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar estoque: " + e.getMessage());
        }
    }

    @Transactional
    public void baixarEstoque(String id, int quantidade) {
        Livro livro = buscarPorId(id);
        try {
            livro.decrementarEstoque(quantidade);
            livroRepository.save(livro);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao baixar estoque: " + e.getMessage());
        }
    }

    @Transactional
    public Livro atualizarLivro(String id, Livro dadosAtualizados) {
        Livro livroExistente = buscarPorId(id);

        livroExistente.setTitulo(dadosAtualizados.getTitulo());
        livroExistente.setIsbn(dadosAtualizados.getIsbn());
        livroExistente.setNumPaginas(dadosAtualizados.getNumPaginas());
        livroExistente.setAnoPublicacao(dadosAtualizados.getAnoPublicacao());
        livroExistente.setResumo(dadosAtualizados.getResumo());
        livroExistente.setPrecoBase(dadosAtualizados.getPrecoBase());

        return livroRepository.save(livroExistente);
    }

}
