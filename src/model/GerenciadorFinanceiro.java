package model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class GerenciadorFinanceiro {
	private double despesasIntervaloFiltrado;
	private double receitasIntervaloFiltrado;
	
	public double getDespesasIntervaloFiltrado() {
		return despesasIntervaloFiltrado;
	}
	
	public void setDespesasIntervaloFiltrado(double despesas) {
		despesasIntervaloFiltrado = despesas;
	}
	
	public double getReceitasIntervaloFiltrado() {
		return receitasIntervaloFiltrado;
	}
	
	public void setReceitasIntervaloFiltrado(double receitas) {
		receitasIntervaloFiltrado = receitas;
	}
	
	public void retirarSaldo(Usuario usuario, double despesa) {
		usuario.setSaldo(usuario.getSaldo() - despesa);
	}
	
	public void adicionarSaldo(Usuario usuario, double receita) {
		usuario.setSaldo(usuario.getSaldo() + receita);
	}
	
	public Object[][] aplicarFiltros(Usuario usuarioAtual, LocalDate filtroDataInicial, LocalDate filtroDataFinal, String filtroClassificacao, String filtroCategoria) {
		ArrayList<Transacao> listaTransacoes = usuarioAtual.getHistoricoTransacoes();
		ArrayList<Transacao> listaTransacoesFiltrada = new ArrayList<>();
		setDespesasIntervaloFiltrado(0);
		setReceitasIntervaloFiltrado(0);
		
		Iterator<Transacao> iterador = listaTransacoes.iterator();
		
		while(iterador.hasNext()) {
			boolean incluirTransacao = true;
			Transacao transacaoAtual = iterador.next();
			
			if(filtroDataInicial != null && transacaoAtual.getData().isBefore(filtroDataInicial))
				incluirTransacao = false;
			
			if(filtroDataFinal != null && transacaoAtual.getData().isAfter(filtroDataFinal))
				incluirTransacao = false;
			
			if(!filtroClassificacao.equals("Classificação") && !transacaoAtual.getClassificacao().equals(filtroClassificacao))
				incluirTransacao = false;
			
			if(!filtroCategoria.equals("Categoria") && !transacaoAtual.getCategoria().getDescricao().equals(filtroCategoria))
				incluirTransacao = false;
			
			if(incluirTransacao) {
				listaTransacoesFiltrada.add(transacaoAtual);
				
				if(transacaoAtual.getClassificacao().equals("Despesa"))
					setDespesasIntervaloFiltrado(getDespesasIntervaloFiltrado() + transacaoAtual.getValor());
				else
					setReceitasIntervaloFiltrado(getReceitasIntervaloFiltrado() + transacaoAtual.getValor());
			}
		}
		
		Object[][] transacoesFiltradas = transformarListaEmMatriz(listaTransacoesFiltrada);
	    
	    return transacoesFiltradas;
	}
	
	public Object[][] retirarFiltros(Usuario usuarioAtual) {
		ArrayList<Transacao> transacoes = usuarioAtual.getHistoricoTransacoes();
		
		Object[][] transacoesSemFiltro = transformarListaEmMatriz(transacoes);
	    
	    return transacoesSemFiltro;
	}
	
	public Object[][] transformarListaEmMatriz(ArrayList<Transacao> lista) {
		int tamanhoLista = lista.size();
		
		Object[][] matrizTransacoesTabela = new Object[tamanhoLista][5];
		
		DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	    symbols.setDecimalSeparator(',');
	    DecimalFormat valorFormatter = new DecimalFormat("0.00", symbols);
	    
	    for (int i = 0; i < tamanhoLista; i++) {
	        Transacao t = lista.get(i);
	        String dataFormatada = t.getData().format(dataFormatter);
	        String valorFormatado = "R$" + valorFormatter.format(t.getValor());
	        
	        matrizTransacoesTabela[i][0] = dataFormatada;
	        matrizTransacoesTabela[i][1] = valorFormatado;
	        matrizTransacoesTabela[i][2] = t.getDescricao();
	        matrizTransacoesTabela[i][3] = t.getCategoria().getDescricao();
	        matrizTransacoesTabela[i][4] = t.getClassificacao();
	    }
	    
	    return matrizTransacoesTabela;
	}
}
