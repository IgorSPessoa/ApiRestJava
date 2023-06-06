package com.restjava.restapi.pedido;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.restjava.restapi.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id", nullable = false) // Nome da coluna de chave estrangeira
                                                                                 // na tabela Pedido
    private User user;

    @Column(nullable = false)
    private String tipolixo;

    @Column(nullable = false)
    private String statuspedido;

    @Column(nullable = false)
    private String statuspagamento;

    @Column(nullable = false)
    private String formapagamento;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "begindate")
    private LocalDateTime begindate;

    @PrePersist
    public void prePersist() {
        begindate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTipolixo() {
        return tipolixo;
    }

    public void setTipolixo(String tipolixo) {
        this.tipolixo = tipolixo;
    }

    public String getStatuspedido() {
        return statuspedido;
    }

    public void setStatuspedido(String statuspedido) {
        this.statuspedido = statuspedido;
    }

    public String getStatuspagamento() {
        return statuspagamento;
    }

    public void setStatuspagamento(String statuspagamento) {
        this.statuspagamento = statuspagamento;
    }

    public String getFormapagamento() {
        return formapagamento;
    }

    public void setFormapagamento(String formapagamento) {
        this.formapagamento = formapagamento;
    }

    public LocalDateTime getBegindate() {
        return begindate;
    }

    public void setBegindate(LocalDateTime begindate) {
        this.begindate = begindate;
    }

}
