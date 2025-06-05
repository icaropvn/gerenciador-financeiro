package model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;
    
    @Column(nullable = false, length = 255)
    private String senha;

    @Column(nullable = false)
    private Double saldo = 0.0;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> historicoTransacoes = new ArrayList<>();

    public Usuario() {}

    public Usuario(String nome, String senha, Double saldoInicial) {
        this.nome = nome;
        this.senha = senha;
        this.saldo = (saldoInicial != null ? saldoInicial : 0.0);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public List<Transacao> getHistoricoTransacoes() {
        return historicoTransacoes;
    }

    /****************** Métodos auxiliares ******************/

    public void adicionarTransacao(Transacao t) {
        historicoTransacoes.add(t);
        t.setUsuario(this);
    }

    public void removerTransacao(Transacao t) {
        historicoTransacoes.remove(t);
        t.setUsuario(null);
    }

    public void receberValor(Double valor) {
        if (valor != null) {
            this.saldo = this.saldo + valor;
        }
    }

    public void pagarValor(Double valor) {
        if (valor != null) {
            this.saldo = this.saldo - valor;
        }
    }
}
