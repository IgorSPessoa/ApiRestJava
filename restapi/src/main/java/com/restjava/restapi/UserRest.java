package com.restjava.restapi;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.server.ResponseStatusException;
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
            return Collections.emptyList(); // Retorna uma lista vazia como fallback
        }
    }

    @PostMapping
    public void salvar(@RequestBody User user) {
        try {
            repositorio.save(user);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
        }
    }

    @PutMapping
    public void alterar(@RequestBody User user) {
        try {
            if (user.getId() > 0)
                repositorio.save(user);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma

        }
    }

    @DeleteMapping
    public void excluir(@RequestBody User user) {
        try {
            repositorio.delete(user);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        try {
            // Optional é uma classe que representa uma estrutura de
            // dados que pode conter um valor ou ser vazio. Está sendo usada para evitar o
            // tratamento de valores nulos diretamente
            Optional<User> response = repositorio.findById(id);

            if (response.isPresent()) {
                User user = response.get();
                return ResponseEntity.ok().body(user); //Usuário encontrado e retornou o user
            } else {
                return ResponseEntity.notFound().build(); // Usuário não encontrado
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/login") // metodo de login requer 2 parametros email e senha por http
    // O <?>, estamos permitindo que o tipo do corpo da resposta seja
    // qualquer tipo, pois não sei qual tipo retorna :p
    public ResponseEntity<?> obterDadosUsuario(@RequestParam String email, @RequestParam String senha) {
        try {
            User usuario = repositorio.findByEmail(email);
            if (usuario == null || !usuario.getSenha().equals(senha)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos");
            }
            return ResponseEntity.ok(usuario); // Requisição bem-sucedida, retorna o usuário
        } catch (ResponseStatusException erro) {
            throw erro; // Lança a exceção de resposta com status para ser tratada adequadamente
        } catch (Exception erro) {
            // Lidar com a exceção de alguma forma
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }

    @PostMapping("/cadastro") // metodo de cadastro requer um json no body do request
    public ResponseEntity<?> cadastrarUsuario(@RequestBody User usuario) {
        try {
            // Verificar se o e-mail já está cadastrado
            User usuarioExistente = repositorio.findByEmail(usuario.getEmail());
            if (usuarioExistente != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já está cadastrado");
            }

            User novoUsuario = repositorio.save(usuario);
            if (novoUsuario != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao cadastrar o usuário");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            // Lidar com a exceção de alguma forma
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entidade não encontrada");
        } catch (Exception e) {
            // Lidar com outras exceções de alguma forma

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
                    }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Usuário não encontrado com o ID: " + id));
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            throw new UpdateUserException("Erro ao atualizar o usuário com o ID: " + id);

        }

    }

}
