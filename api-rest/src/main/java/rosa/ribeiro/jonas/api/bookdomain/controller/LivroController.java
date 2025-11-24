package rosa.ribeiro.jonas.api.bookdomain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rosa.ribeiro.jonas.bookdomain.model.livro.Livro;
import rosa.ribeiro.jonas.bookdomain.service.ManterLivroService;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final ManterLivroService service;

    public LivroController(ManterLivroService service) {
        this.service = service;
    }

    @GetMapping
    public List<Livro> listarTodos() {
        return service.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Livro> criar(@RequestBody Livro livro) {
        Livro salvo = service.salvarLivro(livro);
        return ResponseEntity.ok(salvo);
    }
}