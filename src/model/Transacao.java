package model;

import java.time.LocalDate;

public class Transacao {
	private static int nextId = 0;
	
	private int id;
	private String classificacao;
	private double valor;
	private Categoria categoria;
	private LocalDate data;
	private String descricao;
	
	public Transacao(String classificacao, double valor, Categoria categoria, LocalDate data, String descricao) {
		id = nextId++;
		this.classificacao = classificacao;
		this.valor = valor;
		this.categoria = categoria;
		this.data = data;
		this.descricao = descricao;
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
	
	public static Transacao copiarTransacao(Transacao transacaoACopiar) {
		LocalDate dataCopiada = transacaoACopiar.getData();
		double valorCopiado = transacaoACopiar.getValor();
		String descricaoCopiada = transacaoACopiar.getDescricao();
		Categoria categoriaCopiada = transacaoACopiar.getCategoria();
		String classificacaoCopiada = transacaoACopiar.getClassificacao();
		
		return new Transacao(classificacaoCopiada, valorCopiado, categoriaCopiada, dataCopiada, descricaoCopiada);
	}
}
