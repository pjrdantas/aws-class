package com.aws.dynamo.controller;

import com.aws.dynamo.service.UsuarioDynamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dynamo/usuarios")
public class UsuarioDynamoController {

    private final UsuarioDynamoService service;

    public UsuarioDynamoController(UsuarioDynamoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody Map<String, String> payload) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(payload.get("id"), payload.get("nome"), payload.get("email")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(
            @PathVariable String id,
            @RequestBody Map<String, String> payload
    ) {
        return ResponseEntity.ok(service.atualizar(id, payload.get("nome"), payload.get("email")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
