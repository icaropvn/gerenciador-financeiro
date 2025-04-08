package controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import view.MainFrame;
import view.TelaPrincipal;
import view.TelaEditarCategorias;
import view.TelaAdicionarTransacao;
import model.GerenciadorCategorias;
import model.GerenciadorFinanceiro;
import model.GerenciadorUsuario;

public class PrincipalController {
	private MainFrame mainFrame;
	private TelaPrincipal view;
	private TelaEditarCategorias telaCategorias;
	private TelaAdicionarTransacao telaTransacao;
	private GerenciadorCategorias gerenciadorCategorias;
	private GerenciadorFinanceiro gerenciadorFinanceiro;
	private GerenciadorUsuario gerenciadorUsuario;
	
	public PrincipalController(MainFrame mainFrame, TelaPrincipal telaPrincipal, TelaEditarCategorias telaCategorias, TelaAdicionarTransacao telaTransacao, GerenciadorCategorias gerenciadorCategorias, GerenciadorFinanceiro gerenciadorFinanceiro, GerenciadorUsuario gerenciadorUsuario) {
		this.mainFrame = mainFrame;
		this.view = telaPrincipal;
		this.telaCategorias = telaCategorias;
		this.telaTransacao = telaTransacao;
		this.gerenciadorCategorias = gerenciadorCategorias;
		this.gerenciadorFinanceiro = gerenciadorFinanceiro;
		this.gerenciadorUsuario = gerenciadorUsuario;
		
		initControllers();
	}
	
	public void initControllers() {
		view.getBotaoSair().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmarSaida();
			}
		});
		
		view.getBotaoEditarCategorias().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarTelaEditarCategorias();
			}
		});
		
		view.getBotaoAdicionarTransacao().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				telaTransacao.atualizarListaCategorias(gerenciadorCategorias.getListaCategorias());
				telaTransacao.setVisible(true);
			}
		});
		
		view.getBotaoLimparFiltros().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				limparFiltros();
			}
		});
		
		view.getBotaoAplicarFiltros().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aplicarFiltros();
			}
		});
	}
	
	public void confirmarSaida() {
		int confirmarSaida = JOptionPane.showConfirmDialog(view, "Tem certeza que deseja sair da sua conta?", "Confirmação de saída", JOptionPane.YES_NO_OPTION);
		
		if(confirmarSaida == JOptionPane.YES_OPTION) {
			gerenciadorUsuario.setUsuarioAtual(null);				
			mainFrame.mostrarTela("login");
		}
	}
	
	public void mostrarTelaEditarCategorias() {
		telaCategorias.setVisible(true);
	}
	
	public void limparFiltros() {
		view.getInputDataInicial().setText("");
		view.getInputDataFinal().setText("");
		view.getSelectClassificacao().setSelectedItem("Classificação");
		view.getSelectCategoria().setSelectedItem("Categoria");
		
		// retornar tabela ao estado original (mostrar todas as transações)
		Object[][] transacoesSemFiltro = gerenciadorFinanceiro.retirarFiltros(gerenciadorUsuario.getUsuarioAtual());
		view.substituirTabelaTransacoes(transacoesSemFiltro);
		
		// redefinir resumo de despesas e receitas
		gerenciadorFinanceiro.setDespesasIntervaloFiltrado(0);
		gerenciadorFinanceiro.setReceitasIntervaloFiltrado(0);
		view.setDespesasIntervalo("R$ 0,00");
	    view.setReceitasIntervalo("R$ 0,00");
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
		
		// atualiza tabela na tela com as transações filtradas
		Object[][] transacoesFiltradas = gerenciadorFinanceiro.aplicarFiltros(gerenciadorUsuario.getUsuarioAtual(), dataInicial, dataFinal, conteudoSelectClassificacao, conteudoSelectCategoria);
		view.substituirTabelaTransacoes(transacoesFiltradas);
		
		// atualiza resumo de despesas e receitas no intervalo filtrado
		double despesasIntervalo = gerenciadorFinanceiro.getDespesasIntervaloFiltrado();
		double receitasIntervalo = gerenciadorFinanceiro.getReceitasIntervaloFiltrado();
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	    symbols.setDecimalSeparator(',');
	    symbols.setGroupingSeparator('.');
	    DecimalFormat valorFormatter = new DecimalFormat("0.00", symbols);
	    
	    view.setDespesasIntervalo("R$ " + valorFormatter.format(despesasIntervalo));
	    view.setReceitasIntervalo("R$ " + valorFormatter.format(receitasIntervalo));
	}
}
