package view;

import java.awt.*;
import javax.swing.*;

import java.util.List;

import model.Categoria;
import util.RegExData;

public class TelaResumoFinanceiro extends JFrame {
	private JLabel periodoAnalisado;
	private JLabel classificacao;
	private JLabel categoria;
	private JLabel totalReceitas;
	private JLabel totalDespesas;
	private JLabel saldo;
	private JTextField inputDataInicial;
	private JTextField inputDataFinal;
	private JComboBox<String> selectClassificacao;
	private JComboBox<String> selectCategoria;
	private JButton botaoLimparFiltros;
	private JButton botaoAplicarFiltros;
	private JButton botaoVoltar;
	
	private MainFrame mainFrame;
	
	public TelaResumoFinanceiro(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		setTitle("Resumo financeiro");
		setSize(650, 550);
		setLocationRelativeTo(this.mainFrame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		initComponents();
	}
	
	public void initComponents() {
		JPanel painelRaiz = new JPanel();
		painelRaiz.setLayout(new BoxLayout(painelRaiz, BoxLayout.Y_AXIS));
		painelRaiz.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		
		JLabel titulo = new JLabel("Resumo financeiro");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		painelRaiz.add(titulo);
		
		JPanel resumoFiltroLinha1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		resumoFiltroLinha1.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		resumoFiltroLinha1.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel periodoAnalisadoLabel = new JLabel("Período analisado:");
		periodoAnalisadoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		periodoAnalisadoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		resumoFiltroLinha1.add(periodoAnalisadoLabel);
		
		periodoAnalisado = new JLabel("nenhum");
		periodoAnalisado.setFont(new Font("SansSerif", Font.PLAIN, 16));
		resumoFiltroLinha1.add(periodoAnalisado);
		
		resumoFiltroLinha1.setMaximumSize(new Dimension(Integer.MAX_VALUE, resumoFiltroLinha1.getPreferredSize().height));
		painelRaiz.add(resumoFiltroLinha1);
		
		JPanel resumoFiltroLinha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		resumoFiltroLinha2.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		resumoFiltroLinha2.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel classificacaoLabel = new JLabel("Classificação:");
		classificacaoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		classificacaoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		resumoFiltroLinha2.add(classificacaoLabel);
		
		classificacao = new JLabel("nenhum");
		classificacao.setFont(new Font("SansSerif", Font.PLAIN, 16));
		resumoFiltroLinha2.add(classificacao);
		
		resumoFiltroLinha2.setMaximumSize(new Dimension(Integer.MAX_VALUE, resumoFiltroLinha2.getPreferredSize().height));
		painelRaiz.add(resumoFiltroLinha2);
		
		JPanel resumoFiltroLinha3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		resumoFiltroLinha3.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel categoriaLabel = new JLabel("Categoria:");
		categoriaLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		categoriaLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		resumoFiltroLinha3.add(categoriaLabel);
		
		categoria = new JLabel("nenhum");
		categoria.setFont(new Font("SansSerif", Font.PLAIN, 16));
		resumoFiltroLinha3.add(categoria);
		
		resumoFiltroLinha3.setMaximumSize(new Dimension(Integer.MAX_VALUE, resumoFiltroLinha3.getPreferredSize().height));
		painelRaiz.add(resumoFiltroLinha3);
		
		JPanel painelValores = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		painelValores.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
		painelValores.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel blocoReceita = new JPanel();
		blocoReceita.setLayout(new BoxLayout(blocoReceita, BoxLayout.Y_AXIS));
		blocoReceita.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 80));
		
		JLabel totalReceitasLabel = new JLabel("Total receitas");
		totalReceitasLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		totalReceitasLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		totalReceitasLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		blocoReceita.add(totalReceitasLabel);
		
		totalReceitas = new JLabel("R$ 0,00");
		totalReceitas.setFont(new Font("SansSerif", Font.BOLD, 30));
		totalReceitas.setForeground(Color.decode("#2A6DFF"));
		totalReceitas.setAlignmentX(Component.LEFT_ALIGNMENT);
		blocoReceita.add(totalReceitas);
		
		painelValores.add(blocoReceita);
		
		JPanel blocoDespesa = new JPanel();
		blocoDespesa.setLayout(new BoxLayout(blocoDespesa, BoxLayout.Y_AXIS));
		blocoDespesa.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 80));
		
		JLabel totalDespesasLabel = new JLabel("Total despesas");
		totalDespesasLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		totalDespesasLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		totalDespesasLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		blocoDespesa.add(totalDespesasLabel);
		
		totalDespesas = new JLabel("R$ 0,00");
		totalDespesas.setFont(new Font("SansSerif", Font.BOLD, 30));
		totalDespesas.setForeground(Color.decode("#F54747"));
		totalDespesas.setAlignmentX(Component.LEFT_ALIGNMENT);
		blocoDespesa.add(totalDespesas);
		
		painelValores.add(blocoDespesa);
		
		JPanel blocoSaldo = new JPanel();
		blocoSaldo.setLayout(new BoxLayout(blocoSaldo, BoxLayout.Y_AXIS));
		
		JLabel saldoLabel = new JLabel("Saldo no período");
		saldoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		saldoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		saldoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		blocoSaldo.add(saldoLabel);
		
		saldo = new JLabel("R$ 0,00");
		saldo.setFont(new Font("SansSerif", Font.BOLD, 30));
		saldo.setAlignmentX(Component.LEFT_ALIGNMENT);
		blocoSaldo.add(saldo);
		
		painelValores.add(blocoSaldo);
		
		painelRaiz.add(painelValores);
		
		JLabel filtrosLabel = new JLabel("Filtros");
		filtrosLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
		Dimension filtrosLabelPref = filtrosLabel.getPreferredSize();
		filtrosLabel.setMaximumSize(new Dimension(filtrosLabelPref.width, filtrosLabelPref.height));
		filtrosLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		filtrosLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		painelRaiz.add(filtrosLabel);
		
		JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		filtrosPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel dataInicialLabel = new JLabel("De:");
		dataInicialLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		dataInicialLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		filtrosPanel.add(dataInicialLabel);
		
		inputDataInicial = new JTextField(8);
		((javax.swing.text.AbstractDocument)inputDataInicial.getDocument()).setDocumentFilter(new RegExData());
		filtrosPanel.add(inputDataInicial);
		
		JLabel dataFinalLabel = new JLabel("Até:");
		dataFinalLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		dataFinalLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
		filtrosPanel.add(dataFinalLabel);
		
		inputDataFinal = new JTextField(8);
		((javax.swing.text.AbstractDocument)inputDataFinal.getDocument()).setDocumentFilter(new RegExData());
		filtrosPanel.add(inputDataFinal);
		
		selectClassificacao = new JComboBox<>(new String[]{"Classificação", "Receita", "Despesa"});
		selectClassificacao.setFont(new Font("SansSerif", Font.PLAIN, 12));
		selectClassificacao.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		filtrosPanel.add(selectClassificacao);
		
		selectCategoria = new JComboBox<>(new String[]{"Categoria"});
		selectCategoria.setFont(new Font("SansSerif", Font.PLAIN, 12));
		filtrosPanel.add(selectCategoria);
		
		painelRaiz.add(filtrosPanel);
		
		JPanel painelBotoesFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		painelBotoesFiltro.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		botaoLimparFiltros = new JButton("Limpar filtros");
		botaoLimparFiltros.setFont(new Font("SansSerif", Font.PLAIN, 14));
		painelBotoesFiltro.add(botaoLimparFiltros);
		
		botaoAplicarFiltros = new JButton("Aplicar filtros");
		botaoAplicarFiltros.setFont(new Font("SansSerif", Font.PLAIN, 14));
		painelBotoesFiltro.add(botaoAplicarFiltros);
		
		painelRaiz.add(painelBotoesFiltro);
		
		JPanel painelVoltar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		painelVoltar.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		botaoVoltar = new JButton("Voltar");
		botaoVoltar.setFont(new Font("SansSerif", Font.PLAIN, 14));
		painelVoltar.add(botaoVoltar);
		
		painelRaiz.add(painelVoltar);
		
		add(painelRaiz);
	}
	
	public void inicializarSelectCategorias(List<Categoria> listaCategorias) {
		DefaultComboBoxModel<String> modeloSelectCategoria = new DefaultComboBoxModel<>();
		modeloSelectCategoria.addElement("Categoria");
		for(Categoria categoria : listaCategorias) {
			modeloSelectCategoria.addElement(categoria.getDescricao());
		}
		
		selectCategoria.setModel(modeloSelectCategoria);
	}
	
	public void limparFiltros() {
		setPeriodoAnalisado("nenhum");
		setClassificacao("nenhum");
		setCategoria("nenhum");
		
		setTotalReceitas("R$ 0,00");
		setTotalDespesas("R$ 0,00");
		setSaldo("R$ 0,00");
		saldo.setForeground(Color.black);
		
		inputDataInicial.setText("");
		inputDataFinal.setText("");
		selectClassificacao.setSelectedItem("Classificação");
		selectCategoria.setSelectedItem("Categoria");
	}
	
	public void setPeriodoAnalisado(String periodo) {
		periodoAnalisado.setText(periodo);
	}
	
	public void setClassificacao(String classificacao) {
		this.classificacao.setText(classificacao);
	}
	
	public void setCategoria(String categoria) {
		this.categoria.setText(categoria);
	}
	
	public void setTotalReceitas(String totalReceitas) {
		this.totalReceitas.setText(totalReceitas);
	}
	
	public void setTotalDespesas(String totalDespesas) {
		this.totalDespesas.setText(totalDespesas);
	}
	
	public JLabel getSaldo() {
		return saldo;
	}
	
	public void setSaldo(String saldo) {
		this.saldo.setText(saldo);
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
	
	public JButton getBotaoVoltar() {
		return botaoVoltar;
	}
}
