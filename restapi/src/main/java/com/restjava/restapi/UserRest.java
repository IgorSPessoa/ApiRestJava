package com.restjava.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.restjava.restapi.database.RepositorioUser;
import com.restjava.restapi.user.User;

@RestController
@RequestMapping("/user")
public class UserRest {
    @Autowired //
    private RepositorioUser repositorio;

    @GetMapping
    public List<User> listar() {
        return repositorio.findAll();
    }

    @PostMapping
    public void salvar(@RequestBody User user) {
        repositorio.save(user);
    }

    @PutMapping
    public void alterar(@RequestBody User user) {
        if (user.getId() > 0)
            repositorio.save(user);
    }

    @DeleteMapping
    public void excluir(@RequestBody User user) {
        repositorio.delete(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/login")
    public ResponseEntity<?> obterDadosUsuario(@RequestParam String email, @RequestParam String senha) {
        User usuario = repositorio.findByEmailAndSenha(email, senha);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        return ResponseEntity.ok(usuario);
    }
}
