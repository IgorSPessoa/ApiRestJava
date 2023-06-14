package com.restjava.restapi.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.restjava.restapi.user.User;

public interface RepositorioUser extends JpaRepository<User, Long> {

    User findByEmailAndSenha(String email, String senha);

    // Recebe da função POST(Cadastrar) o parametro email para verificar no banco de
    // dados se existe ja o email cadastrado
    User findByEmail(@Param("email") String email);
}
