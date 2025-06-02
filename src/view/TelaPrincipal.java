package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import model.Categoria;
import model.Transacao;
import util.RegExData;
import util.RenderizadorCelulaTabelaCustomizado;
import util.ModeloTabelaTransacoes;

public class TelaPrincipal extends JPanel {
	private JLabel saudacao;
	private JLabel saldo;
	private JLabel despesas;
	private JLabel receitas;
	private JButton botaoSair;
	
	private JButton botaoEditarCategorias;
	private JButton botaoAdicionarTransacao;
	private JTextField inputDataInicial;
	private JTextField inputDataFinal;
	private JComboBox<String> selectClassificacao;
	private JComboBox<String> selectCategoria;
	private JButton botaoAplicarFiltros;
	private JButton botaoLimparFiltros;
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
		JPanel painelRaiz = new JPanel(new BorderLayout());
		painelRaiz.setBackground(Color.decode("#e0e0e0"));
		painelRaiz.setOpaque(true);
		
		JPanel painelHeader = new JPanel(new BorderLayout());
		painelHeader.setOpaque(false);
		painelHeader.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		
		JPanel headerEsquerda = new JPanel();
		headerEsquerda.setOpaque(false);
		headerEsquerda.setLayout(new BoxLayout(headerEsquerda, BoxLayout.Y_AXIS));
		
		ImageIcon imagemLogo = new ImageIcon(TelaPrincipal.class.getResource("../resources/centsable-logo.png"));
		ImageIcon logoRedimensionado = redimensionarLogo(imagemLogo, 200, 100);
		JLabel labelLogo = new JLabel(logoRedimensionado);
		headerEsquerda.add(labelLogo);
		
		headerEsquerda.add(Box.createVerticalStrut(20));
		
		saudacao = new JLabel("Olá, {user}!");
		saudacao.setFont(new Font("SansSerif", Font.PLAIN, 18));
		saudacao.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		headerEsquerda.add(saudacao);
		
		botaoSair = new JButton("Sair");
		botaoSair.setFont(new Font("SansSerif", Font.PLAIN, 12));
		Dimension tamanhoBotao = new Dimension(70, 25);
		botaoSair.setPreferredSize(tamanhoBotao);
		botaoSair.setMaximumSize(tamanhoBotao);
		headerEsquerda.add(botaoSair);
		painelHeader.add(headerEsquerda, BorderLayout.WEST);
		
		JPanel headerDireita = new JPanel();
		headerDireita.setOpaque(false);
		headerDireita.setLayout(new BoxLayout(headerDireita, BoxLayout.Y_AXIS));
		
		headerDireita.add(Box.createVerticalGlue());
		
		JLabel labelSaldoAtual = new JLabel("Saldo atual");
		labelSaldoAtual.setFont(new Font("SansSerif", Font.PLAIN, 12));
		labelSaldoAtual.setAlignmentX(Component.RIGHT_ALIGNMENT);
		headerDireita.add(labelSaldoAtual);
		
		saldo = new JLabel("R$ 0,00");
		saldo.setFont(new Font("SansSerif", Font.BOLD, 28));
		saldo.setAlignmentX(Component.RIGHT_ALIGNMENT);
		headerDireita.add(saldo);
		painelHeader.add(headerDireita, BorderLayout.EAST);
		
		headerDireita.add(Box.createVerticalGlue());
		
//		JLabel labelTotalDespesas = new JLabel("Total de despesas");
//		labelTotalDespesas.setFont(new Font("SansSerif", Font.PLAIN, 12));
//		labelTotalDespesas.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
//		painelResumo.add(labelTotalDespesas);
//		
//		despesas = new JLabel("R$ 0,00");
//		despesas.setFont(new Font("SansSerif", Font.BOLD, 18));
//		despesas.setForeground(Color.decode("#dd4b4b"));
//		despesas.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
//		painelResumo.add(despesas);
//		
//		JLabel labelTotalReceitas = new JLabel("Total de receitas");
//		labelTotalReceitas.setFont(new Font("SansSerif", Font.PLAIN, 12));
//		labelTotalReceitas.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
//		painelResumo.add(labelTotalReceitas);
//		
//		receitas = new JLabel("R$ 0,00");
//		receitas.setFont(new Font("SansSerif", Font.BOLD, 18));
//		receitas.setForeground(Color.decode("#5c7bed"));
//		receitas.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
//		painelResumo.add(receitas);
		
//		JPanel containerBotaoSair = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
//		containerBotaoSair.setOpaque(false);
		
//		containerBotaoSair.add(botaoSair);
		
		painelRaiz.add(painelHeader, BorderLayout.CENTER);
		add(painelRaiz, BorderLayout.NORTH);
	}
	
	public void initPainelPrincipal() {
		JPanel painelPrincipal = new JPanel(new BorderLayout());
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
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
		
		inputDataInicial = new JTextField(8);
		((javax.swing.text.AbstractDocument)inputDataInicial.getDocument()).setDocumentFilter(new RegExData());
		inputsFiltros.add(inputDataInicial);
		
		JLabel labelDataFinal = new JLabel("Até:");
		labelDataFinal.setFont(new Font("SansSerif", Font.PLAIN, 14));
		inputsFiltros.add(labelDataFinal);
		
		inputDataFinal = new JTextField(8);
		((javax.swing.text.AbstractDocument)inputDataFinal.getDocument()).setDocumentFilter(new RegExData());
		inputsFiltros.add(inputDataFinal);
		
		selectClassificacao = new JComboBox<>(new String[]{"Classificação", "Receita", "Despesa"});
		selectClassificacao.setFont(new Font("SansSerif", Font.PLAIN, 12));
		selectClassificacao.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		inputsFiltros.add(selectClassificacao);
		
		selectCategoria = new JComboBox<>(new String[]{"Categoria"});
		selectCategoria.setFont(new Font("SansSerif", Font.PLAIN, 12));
		selectCategoria.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		inputsFiltros.add(selectCategoria);
		
		containerFiltros.add(inputsFiltros, BorderLayout.WEST);
		
		JPanel containerBotoesFiltros = new JPanel(new FlowLayout());
		
		botaoLimparFiltros = new JButton("Limpar filtros");
		botaoLimparFiltros.setFont(new Font("SansSerif", Font.PLAIN, 12));
		containerBotoesFiltros.add(botaoLimparFiltros);
		
		botaoAplicarFiltros = new JButton("Aplicar filtros");
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
		
		ModeloTabelaTransacoes modeloTabelaTransacoes = new ModeloTabelaTransacoes(null);
        
		tabelaTransacoes = new JTable(modeloTabelaTransacoes);
		aplicarEstilosTabela();
		
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
	
	public void setDespesasIntervalo(String despesas) {
		this.despesas.setText(despesas);
	}
	
	public void setReceitasIntervalo(String receitas) {
		this.receitas.setText(receitas);
	}
	
	public JButton getBotaoSair() {
		return botaoSair;
	}
	
	public JButton getBotaoEditarCategorias() {
		return botaoEditarCategorias;
	}
	
	public JButton getBotaoAdicionarTransacao() {
		return botaoAdicionarTransacao;
	}
	
	public JTextField getInputDataInicial() {
		return inputDataInicial;
	}
	
	public JTextField getInputDataFinal() {
		return inputDataFinal;
	}
	
	public JComboBox getSelectClassificacao() {
		return selectClassificacao;
	}
	
	public JComboBox getSelectCategoria() {
		return selectCategoria;
	}
	
	public JButton getBotaoLimparFiltros() {
		return botaoLimparFiltros;
	}
	
	public JButton getBotaoAplicarFiltros() {
		return botaoAplicarFiltros;
	}
	
	public JTable getTabelaTransacoes() {
		return tabelaTransacoes;
	}
	
	// outros métodos
	public void atualizarFiltroCategorias(List<Categoria> listaCategorias) {
		DefaultComboBoxModel<String> modeloSelectCategoria = new DefaultComboBoxModel<>();
		modeloSelectCategoria.addElement("Categoria");
		for(Categoria categoria : listaCategorias) {
			modeloSelectCategoria.addElement(categoria.getDescricao());
		}
		
		selectCategoria.setModel(modeloSelectCategoria);
	}
	
	public void adicionarTransacaoTabela(Transacao novaTransacao) {		
		((ModeloTabelaTransacoes)tabelaTransacoes.getModel()).adicionarTransacao(novaTransacao);
		aplicarEstilosTabela();
	}
	
	public void removerTransacaoTabela(Transacao transacaoRemover) {
		ModeloTabelaTransacoes modelo = (ModeloTabelaTransacoes) tabelaTransacoes.getModel();
		int indiceTransacao = modelo.getTransacoes().indexOf(transacaoRemover);
		
		modelo.removerTransacao(indiceTransacao);
		aplicarEstilosTabela();
	}
	
	public void atualizarTransacaoTabela(Transacao transacaoEditada) {
		ModeloTabelaTransacoes modelo = (ModeloTabelaTransacoes) tabelaTransacoes.getModel();
		int indiceTransacao = modelo.getTransacoes().indexOf(transacaoEditada);
		
		modelo.atualizarTransacao(indiceTransacao, transacaoEditada);
		aplicarEstilosTabela();
	}
	
	public void atualizarSaldo(String novoSaldo) {
		saldo.setText("R$ " + novoSaldo);
	}
	
	public void substituirTabelaTransacoes(List<Transacao> transacoes) {
		List<Transacao> copia = new ArrayList<>(transacoes);
	    ModeloTabelaTransacoes novoModel = new ModeloTabelaTransacoes(copia);
		tabelaTransacoes.setModel(novoModel);
		aplicarEstilosTabela();
	    
	    tabelaTransacoes.revalidate();
	    tabelaTransacoes.repaint();
	}
	
	public void aplicarEstilosTabela() {
		tabelaTransacoes.getColumnModel().getColumn(4).setCellRenderer(new RenderizadorCelulaTabelaCustomizado());
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
		
		tabelaTransacoes.getColumnModel().getColumn(0).setCellRenderer( new DefaultTableCellRenderer() {
			private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		        
	        @Override
	        protected void setValue(Object value) {
	            LocalDate ld = (LocalDate) value;
	            setText(ld.format(DTF));
	        }
		});
		
		tabelaTransacoes.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
		    
			@Override
		    protected void setValue(Object value) {
				setText(value != null ? nf.format((Number) value) : "");
		    }
		});
		
		tabelaTransacoes.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
	        protected void setValue(Object value) {
				setText(((Categoria) value).getDescricao());
	        }
		});
	}
	
	private ImageIcon redimensionarLogo(ImageIcon imagem, int width, int height) {
		int origW = imagem.getIconWidth();
		int origH = imagem.getIconHeight();

		int maxW = width;
		int maxH = height;

		double escala = Math.min((double) maxW / origW, (double) maxH / origH);

		int novoW = (int) (origW * escala);
		int novoH = (int) (origH * escala);

		Image imgOriginal = imagem.getImage();
		Image imgRedimensionada = imgOriginal.getScaledInstance(novoW, novoH, Image.SCALE_SMOOTH);

		return new ImageIcon(imgRedimensionada);
	}
}
