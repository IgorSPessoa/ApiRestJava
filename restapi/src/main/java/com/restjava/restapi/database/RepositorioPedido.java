package com.restjava.restapi.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restjava.restapi.pedido.Pedido;

public interface RepositorioPedido extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUserId(Long userId);

}
