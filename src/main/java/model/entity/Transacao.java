package model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transacao")
public class Transacao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 20)
	private String classificacao;
	
	@Column(nullable = false)
	private double valor;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;
	
	@Column(nullable = false)
	private LocalDate data;
	
	@Column(length = 255)
	private String descricao;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;
	
	public Transacao(){}
	
	public Transacao(String classificacao, double valor, Categoria categoria, LocalDate data, String descricao, Usuario usuario) {
		this.classificacao = classificacao;
		this.valor = valor;
		this.categoria = categoria;
		this.data = data;
		this.descricao = descricao;
		this.usuario = usuario;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClassificacao() {
		return classificacao;
	}
	
	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}
	
	public double getValor() {
		return valor;
	}
	
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}
	
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	public LocalDate getData() {
		return data;
	}
	
	public void setData(LocalDate data) {
		this.data = data;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static Transacao copiarTransacao(Transacao transacaoACopiar, Usuario usuarioAtual) {
		LocalDate dataCopiada = transacaoACopiar.getData();
		double valorCopiado = transacaoACopiar.getValor();
		String descricaoCopiada = transacaoACopiar.getDescricao();
		Categoria categoriaCopiada = transacaoACopiar.getCategoria();
		String classificacaoCopiada = transacaoACopiar.getClassificacao();
		
		return new Transacao(classificacaoCopiada, valorCopiado, categoriaCopiada, dataCopiada, descricaoCopiada, usuarioAtual);
	}
}
