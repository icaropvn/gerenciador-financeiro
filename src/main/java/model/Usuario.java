package model;

import java.util.ArrayList;

public class Usuario {
	private String nome;
	private String senha;
	private double saldo;
	private ArrayList<Transacao> historicoTransacoes;
	
	public Usuario(String nome, String senha, double saldo) {
		this.nome = nome;
		this.senha = senha;
		this.saldo = saldo;
		this.historicoTransacoes = new ArrayList<>();
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
	
	public ArrayList<Transacao> getHistoricoTransacoes() {
		return historicoTransacoes;
	}
	
	public void adicionarTransacao(Transacao novaTransacao) {
		historicoTransacoes.add(0, novaTransacao);
	}
	
	public void removerTransacao(Transacao transacao) {
		historicoTransacoes.remove(transacao);
	}
	
	public boolean existeTransacaoComCategoria(String categoria) {
		for(Transacao transacao : historicoTransacoes) {
			if(transacao.getCategoria().getDescricao().equals(categoria))
				return true;
		}
		
		return false;
	}
	
	// DEBUG
	public void imprimirListaTransacoes() {
		for(Transacao transacao : historicoTransacoes) {
			System.out.println(transacao.getData());
			System.out.println(transacao.getValor());
			System.out.println(transacao.getDescricao());
			System.out.println(transacao.getCategoria().getDescricao());
			System.out.println(transacao.getClassificacao());
		}
	}
}
