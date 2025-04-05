package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.time.format.DateTimeFormatter;

import model.Transacao;

public class TelaPrincipal extends JPanel {
	private JLabel saudacao;
	private JLabel saldo;
	private JButton botaoEditarCategorias;
	private JButton botaoAdicionarTransacao;
	private JTable tabelaTransacoes;
	
	public TelaPrincipal() {
		setLayout(new BorderLayout());
		
		initComponents();
	}
	
	public void initComponents() {
		initPainelResumo();
		initPainelPrincipal();
	}
	
	public void initPainelResumo() {
		JPanel painelResumo = new JPanel();
		painelResumo.setLayout(new BoxLayout(painelResumo, BoxLayout.Y_AXIS));
		painelResumo.setBackground(Color.decode("#e0e0e0"));
		painelResumo.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
		
		saudacao = new JLabel("Olá, {user}!");
		saudacao.setFont(new Font("SansSerif", Font.PLAIN, 20));
		painelResumo.add(saudacao);
		
		JLabel tituloPainelResumo = new JLabel("Resumo Financeiro",  SwingConstants.CENTER);
		tituloPainelResumo.setFont(new Font("SansSerif", Font.PLAIN, 16));
		tituloPainelResumo.setForeground(Color.decode("#b2b2b2"));
		tituloPainelResumo.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
		tituloPainelResumo.setMaximumSize(new Dimension(Integer.MAX_VALUE, tituloPainelResumo.getPreferredSize().height));
		painelResumo.add(tituloPainelResumo);
		
		JLabel labelSaldoAtual = new JLabel("Saldo atual");
		labelSaldoAtual.setFont(new Font("SansSerif", Font.PLAIN, 12));
		painelResumo.add(labelSaldoAtual);
		
		saldo = new JLabel("R$ 0,00");
		saldo.setFont(new Font("SansSerif", Font.BOLD, 28));
		painelResumo.add(saldo);
		
		JLabel labelTotalDespesas = new JLabel("Total de despesas");
		labelTotalDespesas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		labelTotalDespesas.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		painelResumo.add(labelTotalDespesas);
		
		JLabel despesas = new JLabel("R$ 0,00");
		despesas.setFont(new Font("SansSerif", Font.BOLD, 18));
		despesas.setForeground(Color.decode("#dd4b4b"));
		despesas.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		painelResumo.add(despesas);
		
		JLabel labelTotalReceitas = new JLabel("Total de receitas");
		labelTotalReceitas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		labelTotalReceitas.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		painelResumo.add(labelTotalReceitas);
		
		JLabel receitas = new JLabel("R$ 0,00");
		receitas.setFont(new Font("SansSerif", Font.BOLD, 18));
		receitas.setForeground(Color.decode("#5c7bed"));
		receitas.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		painelResumo.add(receitas);
		
		add(painelResumo, BorderLayout.WEST);
	}
	
	public void initPainelPrincipal() {
		JPanel painelPrincipal = new JPanel(new BorderLayout());
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel painelSuperior = new JPanel(new BorderLayout());
		
		JPanel cabecalho = new JPanel(new BorderLayout());
		
		JLabel tituloPainelPrincipal = new JLabel("Histórico Financeiro");
		tituloPainelPrincipal.setFont(new Font("SansSerif", Font.PLAIN, 16));
		tituloPainelPrincipal.setForeground(Color.decode("#C2C2C2"));
		cabecalho.add(tituloPainelPrincipal, BorderLayout.WEST);
		
		JPanel containerBotoesCabecalho = new JPanel(new FlowLayout());
		
		botaoEditarCategorias = new JButton("Editar categorias");
		botaoEditarCategorias.setFont(new Font("SansSerif", Font.PLAIN, 12));
		containerBotoesCabecalho.add(botaoEditarCategorias);
		
		botaoAdicionarTransacao = new JButton("Adicionar transação");
		botaoAdicionarTransacao.setFont(new Font("SansSerif", Font.PLAIN, 12));
		containerBotoesCabecalho.add(botaoAdicionarTransacao);
		
		cabecalho.add(containerBotoesCabecalho, BorderLayout.EAST);
		
		JPanel painelFiltros = new JPanel(new BorderLayout());
		
		JLabel filtrosTitulo = new JLabel("Filtros");
		filtrosTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
		filtrosTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
		painelFiltros.add(filtrosTitulo, BorderLayout.NORTH);
		
		JPanel containerFiltros = new JPanel(new BorderLayout());
		
		JPanel inputsFiltros = new JPanel(new FlowLayout());
		
		JLabel labelDataInicial = new JLabel("De:");
		labelDataInicial.setFont(new Font("SansSerif", Font.PLAIN, 14));
		inputsFiltros.add(labelDataInicial);
		
		JTextField inputDataInicial = new JTextField(8);
		inputsFiltros.add(inputDataInicial);
		
		JLabel labelDataFinal = new JLabel("Até:");
		labelDataFinal.setFont(new Font("SansSerif", Font.PLAIN, 14));
		inputsFiltros.add(labelDataFinal);
		
		JTextField inputDataFinal = new JTextField(8);
		inputsFiltros.add(inputDataFinal);
		
		JComboBox<String> selectClassificacao = new JComboBox<>(new String[]{"Classificação"});
		selectClassificacao.setFont(new Font("SansSerif", Font.PLAIN, 12));
		selectClassificacao.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		inputsFiltros.add(selectClassificacao);
		
		JComboBox<String> selectCategoria = new JComboBox<>(new String[]{"Categoria"});
		selectCategoria.setFont(new Font("SansSerif", Font.PLAIN, 12));
		selectCategoria.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		inputsFiltros.add(selectCategoria);
		
		containerFiltros.add(inputsFiltros, BorderLayout.WEST);
		
		JPanel containerBotoesFiltros = new JPanel(new FlowLayout());
		
		JButton botaoLimparFiltros = new JButton("Limpar filtros");
		botaoLimparFiltros.setFont(new Font("SansSerif", Font.PLAIN, 12));
		containerBotoesFiltros.add(botaoLimparFiltros);
		
		JButton botaoAplicarFiltros = new JButton("Aplicar filtros");
		botaoAplicarFiltros.setFont(new Font("SansSerif", Font.PLAIN, 12));
		containerBotoesFiltros.add(botaoAplicarFiltros);
		
		containerFiltros.add(containerBotoesFiltros, BorderLayout.EAST);
		
		painelFiltros.add(containerFiltros, BorderLayout.CENTER);
		
		painelSuperior.add(cabecalho, BorderLayout.NORTH);
		painelSuperior.add(painelFiltros, BorderLayout.CENTER);
		
		painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
		
		JPanel painelHistorico = new JPanel(new BorderLayout());
		
		JLabel tituloPainelHistorico = new JLabel("Histórico de transações");
		tituloPainelHistorico.setFont(new Font("SansSerif", Font.BOLD, 20));
		tituloPainelHistorico.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
		painelHistorico.add(tituloPainelHistorico, BorderLayout.NORTH);
		
		String[] colunasTabela = {"Data", "Valor", "Descrição", "Categoria", "Classificação"};
		
		Object[][] dados = {};
		
		DefaultTableModel modeloTabelaTransacoes = new DefaultTableModel(dados, colunasTabela);
		tabelaTransacoes = new JTable(modeloTabelaTransacoes);
		tabelaTransacoes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
		tabelaTransacoes.getTableHeader().setBackground(Color.decode("#D0D7FF"));
		tabelaTransacoes.getTableHeader().setOpaque(true);
		tabelaTransacoes.setFont(new Font("SansSerif", Font.PLAIN, 14));
		tabelaTransacoes.setRowHeight(tabelaTransacoes.getFontMetrics(tabelaTransacoes.getFont()).getHeight() + 4);
		
		TableColumnModel modeloColunas = tabelaTransacoes.getColumnModel();
		modeloColunas.getColumn(0).setPreferredWidth(60);;
		modeloColunas.getColumn(1).setPreferredWidth(60);
		modeloColunas.getColumn(2).setPreferredWidth(300);
		modeloColunas.getColumn(3).setPreferredWidth(100);
		modeloColunas.getColumn(4).setPreferredWidth(100);
		
		JScrollPane scrollTabela = new JScrollPane(tabelaTransacoes);
		
		painelHistorico.add(scrollTabela, BorderLayout.CENTER);
		
		painelPrincipal.add(painelHistorico, BorderLayout.CENTER);
		
		add(painelPrincipal, BorderLayout.CENTER);
	}
	
	// getters e setters
	public void setSaudacao(String saudacao) {
		this.saudacao.setText(saudacao);;
	}
	
	public void setSaldo(String saldo) {
		this.saldo.setText(saldo);
	}
	
	public JButton getBotaoEditarCategorias() {
		return botaoEditarCategorias;
	}
	
	public JButton getBotaoAdicionarTransacao() {
		return botaoAdicionarTransacao;
	}
	
	// outros métodos
	public void adicionarTransacaoTabela(Transacao novaTransacao) {
		String classificacao = novaTransacao.getClassificacao();
		
		DecimalFormatSymbols modeloSimbolosValor = new DecimalFormatSymbols();
		modeloSimbolosValor.setDecimalSeparator(',');
		DecimalFormat formatadorDecimal = new DecimalFormat("0.00", modeloSimbolosValor);
		String valor = "R$" + formatadorDecimal.format(novaTransacao.getValor());
		
		String categoria = novaTransacao.getCategoria().getDescricao();
		DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String data = novaTransacao.getData().format(formatadorData);
		String descricao = novaTransacao.getDescricao();
		
		DefaultTableModel modeloTabela = (DefaultTableModel) tabelaTransacoes.getModel();
		modeloTabela.insertRow(0, new Object[]{data, valor, descricao, categoria, classificacao});
		
		tabelaTransacoes.revalidate();
		tabelaTransacoes.repaint();
	}
	
	public void atualizarSaldo(String novoSaldo) {
		saldo.setText("R$ " + novoSaldo);
	}
}
