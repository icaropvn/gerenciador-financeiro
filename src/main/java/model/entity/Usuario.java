package model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 100, unique = true)
	private String nome;
	
	@Column(nullable = false, length = 255)
	private String senha;
	
	@Column(nullable = false)
	private double saldo;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Transacao> historicoTransacoes;
	
	public Usuario(){}
	
	public Usuario(String nome, String senha, double saldo) {
		this.nome = nome;
		this.senha = senha;
		this.saldo = saldo;
		this.historicoTransacoes = new ArrayList<>();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public double getSaldo() {
		return saldo;
	}
	
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public List<Transacao> getHistoricoTransacoes() {
		return historicoTransacoes;
	}
	
	public void adicionarTransacao(Transacao novaTransacao) {
		historicoTransacoes.add(novaTransacao);
	}
	
	public void removerTransacao(Transacao transacao) {
		historicoTransacoes.remove(transacao);
	}
	
	// VAMOS PASSAR PARA TransacaoDAO
	
//	public boolean existeTransacaoComCategoria(String categoria) {
//		for(Transacao transacao : historicoTransacoes) {
//			if(transacao.getCategoria().getDescricao().equals(categoria))
//				return true;
//		}
//		
//		return false;
//	}
}
