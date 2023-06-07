package com.restjava.restapi.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restjava.restapi.user.User;

public interface RepositorioUser extends JpaRepository<User, Long> {

    User findByEmailAndSenha(String email, String senha);
}
