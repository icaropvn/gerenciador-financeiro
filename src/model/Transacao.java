package model;

import java.time.LocalDate;

public class Transacao {
	private String classificacao;
	private double valor;
	private Categoria categoria;
	private LocalDate data;
	private String descricao;
	
	public Transacao(String classificacao, double valor, Categoria categoria, LocalDate data, String descricao) {
		this.classificacao = classificacao;
		this.valor = valor;
		this.categoria = categoria;
		this.data = data;
		this.descricao = descricao;
	}
	
	public String getClassificacao() {
		return classificacao;
	}
	
	public double getValor() {
		return valor;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}
	
	public LocalDate getData() {
		return data;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
