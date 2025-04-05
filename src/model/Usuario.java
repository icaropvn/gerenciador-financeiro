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
	
	public void adicionarTransacao(Transacao novaTransacao) {
		historicoTransacoes.add(novaTransacao);
	}
}
