package model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String descricao;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> historicoTransacoes = new ArrayList<>();


    public Categoria() {}

    public Categoria(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Transacao> getHistoricoTransacoes() {
        return historicoTransacoes;
    }

    /****************** Métodos auxiliares ******************/

    public void adicionarTransacao(Transacao t) {
        historicoTransacoes.add(t);
        t.setCategoria(this);
    }

    public void removerTransacao(Transacao t) {
        historicoTransacoes.remove(t);
        t.setCategoria(null);
    }
}
