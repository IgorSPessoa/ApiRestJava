package com.restjava.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restjava.restapi.database.RepositorioPedido;
import com.restjava.restapi.pedido.Pedido;

@RestController
@RequestMapping("/pedido")
public class PedidoRest {
    @Autowired
    private RepositorioPedido repositorio;

    @GetMapping
    public List<Pedido> listarPedidos() {
        return repositorio.findAll();
    }

    @PostMapping
    public void salvarPedido(@RequestBody Pedido pedido) {
        repositorio.save(pedido);
    }

    @PutMapping
    public void alterarPedido(@RequestBody Pedido pedido) {
        if (pedido.getId() > 0)
            repositorio.save(pedido);
    }

    @DeleteMapping
    public void deletarPedido(@RequestBody Pedido pedido) {
        repositorio.delete(pedido);
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Pedido>> getPedidosByUserId(@PathVariable Long userId) {
        List<Pedido> pedidos = repositorio.findByUserId(userId);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }
}
