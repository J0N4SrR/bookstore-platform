package rosa.ribeiro.jonas.api.bookdomain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rosa.ribeiro.jonas.bookdomain.model.editora.Editora;
import rosa.ribeiro.jonas.bookdomain.service.ManterEditoraService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/editoras")
public class EditoraController {

    private final ManterEditoraService service;

    public EditoraController(ManterEditoraService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Editora>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editora> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Editora> criar(@RequestBody Editora editora) {
        Editora salva = service.salvar(editora);
        return ResponseEntity.created(URI.create("/api/editoras/" + salva.getId())).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editora> atualizar(@PathVariable String id, @RequestBody Editora editora) {
        try {
            Editora atualizada = service.atualizar(id, editora);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}