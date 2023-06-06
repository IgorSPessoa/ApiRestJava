package com.restjava.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
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

    // metodo de login requer 2 parametros email e senha por http
    @GetMapping("/login")
    public ResponseEntity<?> obterDadosUsuario(@RequestParam String email, @RequestParam String senha) {
        User usuario = repositorio.findByEmailAndSenha(email, senha);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        return ResponseEntity.ok(usuario);
    }

    // metodo de cadastro requer um json no body do request
    @PostMapping("/cadastro")
    public User cadastrarUsuario(@RequestBody User usuario) {
        return repositorio.save(usuario);
    }

    // metodo de delete requer um id
    @DeleteMapping("/deletar/{id}")
    public void deletarDadosPorId(@PathVariable Long id) {
        User entidade = repositorio.findById(id).orElse(null);

        if (entidade == null) {
            // Retorne uma resposta adequada caso a entidade não seja encontrada
            // Exemplo: throw new EntityNotFoundException("Entidade não encontrada");
        }

        repositorio.delete(entidade);
    }

    // metodo de atualizar dados requer id do usuario e json no body
    @PutMapping("/atualizar/{id}")
    public User atualizar(@PathVariable Long id, @RequestBody User Atualizado) {
        return repositorio.findById(id)
                .map(novo -> {
                    novo.setNome(Atualizado.getNome()); // Atualize os campos desejados
                    novo.setSobrenome(Atualizado.getSobrenome());
                    novo.setEmail(Atualizado.getEmail());
                    novo.setSenha(Atualizado.getSenha());
                    novo.setCpf(Atualizado.getCpf());
                    novo.setTelefone(Atualizado.getTelefone());
                    // Adicione outros campos que deseja atualizar

                    return repositorio.save(novo);
                }).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com o ID: " + id));
    }

}
