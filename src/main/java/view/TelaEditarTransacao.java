package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import model.entity.Categoria;
import model.entity.Transacao;
import util.NumericDocumentFilter;
import util.TransformarStringEmData;

public class TelaEditarTransacao extends JFrame {
	private Transacao transacaoEmEdicao;
	private boolean modoEdicao;
	
	private MainFrame mainFrame;
	private JComboBox<String> selectClassificacao;
	private JComboBox<String> selectCategoria;
	private JSpinner dataInput;
	private JTextField valorInput;
	private JTextArea descricaoInput;
	private JButton botaoRemover, botaoEditar;
	
	public TelaEditarTransacao(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		setTitle("Editar transação");
		setSize(500, 500);
		setLocationRelativeTo(this.mainFrame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		initComponents();
	}
	
	public void initComponents() {
		JPanel painelRaiz = new JPanel(new BorderLayout());
		
		JLabel titulo = new JLabel("Editar transação");
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
		selectClassificacao.setEnabled(false);
		inputsPrimeiraLinha.add(selectClassificacao);
		
		selectCategoria = new JComboBox<>(new String[]{"Categoria"});
		selectCategoria.setFont(new Font("SansSerif", Font.PLAIN, 12));
		selectCategoria.setEnabled(false);
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
		JFormattedTextField dataInputTextField = ((JSpinner.DefaultEditor)dataInput.getEditor()).getTextField();
		dataInputTextField.setEditable(false);
		dataInput.setEnabled(false);
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
		valorInput.setEnabled(false);
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
		descricaoInput.setEnabled(false);
		
		JScrollPane painelRolavelDescricao = new JScrollPane(descricaoInput);
		painelRolavelDescricao.setPreferredSize(new Dimension(300, 100));
		painelRolavelDescricao.setMaximumSize(new Dimension(300, 100));
		painelRolavelDescricao.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputsContainer.add(painelRolavelDescricao);
		
		capsulaInputsContainer.add(inputsContainer, BorderLayout.NORTH);
		
		JPanel botoesRodapeContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		botoesRodapeContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		
		botaoRemover = new JButton("Remover");
		botaoRemover.setFont(new Font("SansSerif", Font.PLAIN, 14));
		botaoRemover.setBackground(Color.decode("#ff7f7f"));
		botoesRodapeContainer.add(botaoRemover);
		
		botaoEditar = new JButton("Editar");
		botaoEditar.setFont(new Font("SansSerif", Font.PLAIN, 14));
		botoesRodapeContainer.add(botaoEditar);
		
		painelRaiz.add(titulo, BorderLayout.NORTH);
		painelRaiz.add(capsulaInputsContainer, BorderLayout.CENTER);
		painelRaiz.add(botoesRodapeContainer, BorderLayout.SOUTH);
		
		add(painelRaiz);
	}
	
	public Transacao getTransacaoEmEdicao() {
		return transacaoEmEdicao;
	}
	
	public void setTransacaoEmEdicao(Transacao transacaoEmEdicao) {
		this.transacaoEmEdicao = transacaoEmEdicao;
	}
	
	public boolean getModoEdicao() {
		return modoEdicao;
	}
	
	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}
	
	public JComboBox getSelectClassificacao() {
		return selectClassificacao;
	}
	
	public JComboBox getSelectCategoria() {
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
	
	public JButton getBotaoRemover() {
		return botaoRemover;
	}
	
	public JButton getBotaoEditar() {
		return botaoEditar;
	}
	
	public void setTextoBotaoEditar(String texto) {
		botaoEditar.setText(texto);
	}
	
	public void atualizarListaCategorias(List<Categoria> listaCategorias) {
		DefaultComboBoxModel<String> modeloSelectCategoria = new DefaultComboBoxModel<>();
		modeloSelectCategoria.addElement("Categoria");
		for(Categoria categoria : listaCategorias) {
			modeloSelectCategoria.addElement(categoria.getDescricao());
		}
		
		selectCategoria.setModel(modeloSelectCategoria);
	}
	
	public void inicializarInputs(Transacao transacao) {
		selectClassificacao.setSelectedItem(transacao.getClassificacao());
		selectCategoria.setSelectedItem(transacao.getCategoria().getDescricao());
		
		Date data = Date.from(transacao.getData().atStartOfDay(ZoneId.systemDefault()).toInstant());
		dataInput.setValue(data);
		
		DecimalFormat df = new DecimalFormat("#,##0.00");
		valorInput.setText(df.format(transacao.getValor()).replace(".", ""));
		descricaoInput.setText(transacao.getDescricao());
	}
	
	public void desbloquearCampos() {
		selectClassificacao.setEnabled(true);
		selectCategoria.setEnabled(true);
		
		JFormattedTextField dataInputTextField = ((JSpinner.DefaultEditor)dataInput.getEditor()).getTextField();
		dataInputTextField.setEditable(true);
		dataInput.setEnabled(true);
		
		valorInput.setEnabled(true);
		descricaoInput.setEnabled(true);
	}
	
	public void bloquearCampos() {
		selectClassificacao.setEnabled(false);
		selectCategoria.setEnabled(false);
		
		JFormattedTextField dataInputTextField = ((JSpinner.DefaultEditor)dataInput.getEditor()).getTextField();
		dataInputTextField.setEditable(false);
		dataInput.setEnabled(false);
		
		valorInput.setEnabled(false);
		descricaoInput.setEnabled(false);
	}
}
