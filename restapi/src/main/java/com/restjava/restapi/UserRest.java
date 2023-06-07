package com.restjava.restapi;

import java.util.Collections;
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

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/user")
public class UserRest {

    @Autowired //
    private RepositorioUser repositorio;

    @GetMapping
    public List<User> listar() {
        try {
            return repositorio.findAll();
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();
            return Collections.emptyList(); // Retorna uma lista vazia como fallback
        }
    }

    @PostMapping
    public void salvar(@RequestBody User user) {
        try {
            repositorio.save(user);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();
        }
    }

    @PutMapping
    public void alterar(@RequestBody User user) {
        try {
            if (user.getId() > 0)
                repositorio.save(user);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();

        }
    }

    @DeleteMapping
    public void excluir(@RequestBody User user) {
        try {
            repositorio.delete(user);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        try {
            return repositorio.findById(id)
                    .map(record -> ResponseEntity.ok().body(record))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/login") // metodo de login requer 2 parametros email e senha por http
    public ResponseEntity<?> obterDadosUsuario(@RequestParam String email, @RequestParam String senha) {
        try {
            User usuario = repositorio.findByEmailAndSenha(email, senha);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
            }
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cadastro") // metodo de cadastro requer um json no body do request
    public User cadastrarUsuario(@RequestBody User usuario) {
        try {
            return repositorio.save(usuario);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/deletar/{id}") // metodo de delete requer um id
    public ResponseEntity<String> deletarDadosPorId(@PathVariable Long id) {
        try {
            User entidade = repositorio.findById(id).orElse(null);

            if (entidade == null) {
                throw new EntityNotFoundException("Entidade não encontrada");
            }

            repositorio.delete(entidade);

            return ResponseEntity.ok().body("Entidade deletada com sucesso");
        } catch (EntityNotFoundException e) {
            // Lidar com a exceção EntityNotFoundException de alguma forma
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entidade não encontrada");
        } catch (Exception e) {
            // Lidar com outras exceções de alguma forma
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar a entidade");
        }
    }

    @PutMapping("/atualizar/{id}") // metodo de atualizar dados requer id do usuario e json no body
    public User atualizar(@PathVariable Long id, @RequestBody User Atualizado) {
        try {
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
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();
            throw new UpdateUserException("Erro ao atualizar o usuário com o ID: " + id);

        }

    }

}
