package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.AbstractDocument;

import model.entity.Categoria;

import java.util.Date;
import java.util.List;

import util.NumericDocumentFilter;

public class TelaAdicionarTransacao extends JFrame {
	private MainFrame mainFrame;
	
	private JComboBox<String> selectClassificacao;
	private JComboBox<String> selectCategoria;
	private JSpinner dataInput;
	private JTextField valorInput;
	private JTextArea descricaoInput;
	private JButton botaoCancelar;
	private JButton botaoAdicionar;
	
	public TelaAdicionarTransacao(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		setTitle("Adicionar transação");
		setSize(500, 500);
		setLocationRelativeTo(this.mainFrame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		initComponents();
	}
	
	public void initComponents() {
		JPanel painelRaiz = new JPanel(new BorderLayout());
		
		JLabel titulo = new JLabel("Adicionar transação");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		titulo.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 0));
		titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel capsulaInputsContainer = new JPanel(new BorderLayout());
		capsulaInputsContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		
		JPanel inputsContainer = new JPanel();
		inputsContainer.setLayout(new BoxLayout(inputsContainer, BoxLayout.Y_AXIS));
		
		JPanel inputsPrimeiraLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		inputsPrimeiraLinha.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		inputsPrimeiraLinha.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		selectClassificacao = new JComboBox<>(new String[]{"Classificação", "Receita", "Despesa"});
		selectClassificacao.setFont(new Font("SansSerif", Font.PLAIN, 12));
		selectClassificacao.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		inputsPrimeiraLinha.add(selectClassificacao);
		
		selectCategoria = new JComboBox<>(new String[]{"Categoria"});
		selectCategoria.setFont(new Font("SansSerif", Font.PLAIN, 12));
		inputsPrimeiraLinha.add(selectCategoria);
		
		inputsContainer.add(inputsPrimeiraLinha);
		
		JLabel dataLabel = new JLabel("Data");
		dataLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		dataLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		dataLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		inputsContainer.add(dataLabel);
		
		JPanel dataInputLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		dataInputLinha.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		dataInputLinha.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		SpinnerDateModel dataModelo = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
		dataInput = new JSpinner(dataModelo);
		JSpinner.DateEditor dataEditor = new JSpinner.DateEditor(dataInput, "dd/MM/yyyy");
		dataInput.setEditor(dataEditor);
		dataInput.setPreferredSize(new Dimension(150, 30));
		dataInput.setMaximumSize(new Dimension(150, 30));
		JFormattedTextField dataTextField = dataEditor.getTextField();
		dataTextField.setBorder(BorderFactory.createCompoundBorder(dataTextField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		dataInput.setAlignmentX(Component.LEFT_ALIGNMENT);
		dataInputLinha.add(dataInput);
		
		inputsContainer.add(dataInputLinha);
		
		JLabel valorLabel = new JLabel("Valor");
		valorLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		valorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		valorLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		inputsContainer.add(valorLabel);
		
		JPanel valorInputLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		valorInputLinha.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		valorInputLinha.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		valorInput = new JTextField();
		((AbstractDocument) valorInput.getDocument()).setDocumentFilter(new NumericDocumentFilter());
		valorInput.setPreferredSize(new Dimension(150, 30));
		valorInput.setMaximumSize(new Dimension(150, 30));
		valorInput.setMargin(new Insets(5, 5, 5, 5));
		valorInput.setAlignmentX(Component.LEFT_ALIGNMENT);
		valorInputLinha.add(valorInput);
		
		inputsContainer.add(valorInputLinha);
		
		JLabel descricaoLabel = new JLabel("Descrição");
		descricaoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		descricaoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		descricaoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		inputsContainer.add(descricaoLabel);
		
		descricaoInput = new JTextArea();
		descricaoInput.setMargin(new Insets(5, 5, 5, 5));
		descricaoInput.setLineWrap(true);
		descricaoInput.setWrapStyleWord(true);
		
		JScrollPane painelRolavelDescricao = new JScrollPane(descricaoInput);
		painelRolavelDescricao.setPreferredSize(new Dimension(300, 100));
		painelRolavelDescricao.setMaximumSize(new Dimension(300, 100));
		painelRolavelDescricao.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputsContainer.add(painelRolavelDescricao);
		
		capsulaInputsContainer.add(inputsContainer, BorderLayout.NORTH);
		
		JPanel botoesRodapeContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		botoesRodapeContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		
		botaoCancelar = new JButton("Cancelar");
		botaoCancelar.setFont(new Font("SansSerif", Font.PLAIN, 14));
		botoesRodapeContainer.add(botaoCancelar);
		
		botaoAdicionar = new JButton("Adicionar");
		botaoAdicionar.setFont(new Font("SansSerif", Font.PLAIN, 14));
		botoesRodapeContainer.add(botaoAdicionar);
		
		painelRaiz.add(titulo, BorderLayout.NORTH);
		painelRaiz.add(capsulaInputsContainer, BorderLayout.CENTER);
		painelRaiz.add(botoesRodapeContainer, BorderLayout.SOUTH);
		
		add(painelRaiz);
	}
	
	public JComboBox<String> getSelectClassificacao() {
		return selectClassificacao;
	}
	
	public JComboBox<String> getSelectCategoria() {
		return selectCategoria;
	}
	
	public JSpinner getDataInput() {
		return dataInput;
	}
	
	public JTextField getValorInput() {
		return valorInput;
	}
	
	public JTextArea getDescricaoInput() {
		return descricaoInput;
	}
	
	public JButton getBotaoCancelar() {
		return botaoCancelar;
	}

	public JButton getBotaoAdicionar() {
		return botaoAdicionar;
	}
	
	public void atualizarListaCategorias(List<Categoria> listaCategorias) {
		DefaultComboBoxModel<String> modeloSelectCategoria = new DefaultComboBoxModel<>();
		modeloSelectCategoria.addElement("Categoria");
		for(Categoria categoria : listaCategorias) {
			modeloSelectCategoria.addElement(categoria.getDescricao());
		}
		
		selectCategoria.setModel(modeloSelectCategoria);
	}
}
