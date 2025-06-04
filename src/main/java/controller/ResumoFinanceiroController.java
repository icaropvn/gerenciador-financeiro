package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import model.entity.Transacao;
import model.service.GerenciadorCategorias;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;
import view.TelaResumoFinanceiro;

public class ResumoFinanceiroController {
	private TelaResumoFinanceiro view;
	private GerenciadorCategorias gerenciadorCategorias;
	private GerenciadorFinanceiro gerenciadorFinanceiro;
	private GerenciadorUsuario gerenciadorUsuario;
	
	public ResumoFinanceiroController(TelaResumoFinanceiro view, GerenciadorCategorias gerenciadorCategorias, GerenciadorFinanceiro gerenciadorFinanceiro, GerenciadorUsuario gerenciadorUsuario) {
		this.view = view;
		this.gerenciadorCategorias = gerenciadorCategorias;
		this.gerenciadorFinanceiro = gerenciadorFinanceiro;
		this.gerenciadorUsuario = gerenciadorUsuario;
		
		initControllers();
	}
	
	public void initControllers() {
		view.getBotaoAplicarFiltros().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aplicarFiltros();
			}
		});
		
		view.getBotaoLimparFiltros().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				limparFiltros();
			}
		});
		
		view.getBotaoVoltar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});
		
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				limparFiltros();
			}
		});
	}
	
	public void aplicarFiltros() {
		String conteudoDataInicial = view.getInputDataInicial().getText();
		String conteudoDataFinal = view.getInputDataFinal().getText();
		String conteudoSelectClassificacao = (String)view.getSelectClassificacao().getSelectedItem();
		String conteudoSelectCategoria = (String)view.getSelectCategoria().getSelectedItem();
		
		// se todos os inputs estiverem "vazios", alertar o usuário
		if(conteudoDataInicial.isEmpty() &&
		   conteudoDataFinal.isEmpty() &&
		   conteudoSelectClassificacao.equals("Classificação") &&
		   conteudoSelectCategoria.equals("Categoria")) {
			JOptionPane.showMessageDialog(view, "Forneça algum parâmetro para filtrar as transações.", "Nenhum filtro selecionado", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// verificação se a categoria existe no sistema
		if(!conteudoSelectCategoria.equals("Categoria") && !gerenciadorCategorias.isThereCategoria(conteudoSelectCategoria)) {
			JOptionPane.showMessageDialog(view, "Algo deu errado ao filtrar por essa categoria.", "Erro ao filtrar por categoria", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// verificar formato das datas
		Pattern pattern = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$");
		Matcher matcherDataInicial = pattern.matcher(conteudoDataInicial);
		Matcher matcherDataFinal = pattern.matcher(conteudoDataFinal);
		
		if(!conteudoDataInicial.isEmpty() && !matcherDataInicial.matches()) {
			JOptionPane.showMessageDialog(view, "Insira uma data inicial válida para filtrar. Use o formato DD/MM/AAAA.", "Data de filtragem inválida", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!conteudoDataFinal.isEmpty() && !matcherDataFinal.matches()) {
			JOptionPane.showMessageDialog(view, "Insira uma data final válida para filtrar. Use o formato DD/MM/AAAA.", "Data de filtragem inválida", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		LocalDate dataInicial;
		LocalDate dataFinal;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		try {
			dataInicial = LocalDate.parse(conteudoDataInicial, formatter);
		} catch(DateTimeParseException e) {
			dataInicial = null;
		}
		
		try {
			dataFinal = LocalDate.parse(conteudoDataFinal, formatter);
		} catch(DateTimeParseException e) {
			dataFinal = null;
		}
		
		// filtra as transações de acordo com os parâmetros
		List<Transacao> transacoesFiltradas = gerenciadorFinanceiro.buscarTransacoesFiltradas(gerenciadorUsuario.getUsuarioAtual().getId(), dataInicial, dataFinal, conteudoSelectClassificacao, conteudoSelectCategoria);
		
		double despesasIntervalo = gerenciadorFinanceiro.somaDespesasFiltradas(gerenciadorUsuario.getUsuarioAtual().getId(), dataInicial, dataFinal, conteudoSelectClassificacao, conteudoSelectCategoria);
		double receitasIntervalo = gerenciadorFinanceiro.somaReceitasFiltradas(gerenciadorUsuario.getUsuarioAtual().getId(), dataInicial, dataFinal, conteudoSelectClassificacao, conteudoSelectCategoria);
		double saldo = receitasIntervalo - despesasIntervalo;
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	    symbols.setDecimalSeparator(',');
	    symbols.setGroupingSeparator('.');
	    DecimalFormat valorFormatter = new DecimalFormat("0.00", symbols);
	    
	    view.setTotalReceitas("R$ " + valorFormatter.format(receitasIntervalo));
	    view.setTotalDespesas("R$ " + valorFormatter.format(despesasIntervalo));
	    view.setSaldo("R$ " + valorFormatter.format(saldo));
	    
	    if(saldo > 0)
	    	view.getSaldo().setForeground(Color.decode("#2A6DFF"));
	    else
	    	view.getSaldo().setForeground(Color.decode("#F54747"));
	    
	    view.setPeriodoAnalisado((conteudoDataInicial.isEmpty() ? "#" : conteudoDataInicial) + " - " + (conteudoDataFinal.isEmpty() ? "#" : conteudoDataFinal));
	    view.setClassificacao(conteudoSelectClassificacao.isEmpty() || conteudoSelectClassificacao.equals("Classificação") ? "nenhum" : conteudoSelectClassificacao);
	    view.setCategoria(conteudoSelectCategoria.isEmpty() || conteudoSelectCategoria.equals("Categoria") ? "nenhum" : conteudoSelectCategoria);
	}
	
	public void limparFiltros() {
		view.limparFiltros();
	}
	
	public void voltar() {
		view.limparFiltros();
		view.dispose();
	}
}
