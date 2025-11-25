package rosa.ribeiro.jonas.api.bookdomain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rosa.ribeiro.jonas.api.bookdomain.dto.LivroRequestDTO;
import rosa.ribeiro.jonas.api.bookdomain.factory.LivroFactory; // Importe a Factory
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.bookdomain.service.ManterLivroService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final ManterLivroService service;
    private final LivroFactory livroFactory;

    public LivroController(ManterLivroService service, LivroFactory livroFactory) {
        this.service = service;
        this.livroFactory = livroFactory;
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Livro> criar(@RequestBody LivroRequestDTO dto) {
        try {
            Livro novoLivro = livroFactory.criarEntidade(dto);

            Livro salvo = service.salvarLivro(novoLivro);

            return ResponseEntity.created(URI.create("/api/livros/" + salvo.getId())).body(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable String id, @RequestBody LivroRequestDTO dto) {
        try {
            Livro dadosNovos = livroFactory.criarEntidade(dto);
            Livro livroAtualizado = service.atualizarLivro(id, dadosNovos);
            return ResponseEntity.ok(livroAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        try {
            service.deletarLivro(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pesquisa")
    public ResponseEntity<List<Livro>> pesquisar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String editora,
            @RequestParam(required = false) String categoria) {

        List<Livro> resultado = service.pesquisarLivros(titulo, autor, editora, categoria);

        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }
}

