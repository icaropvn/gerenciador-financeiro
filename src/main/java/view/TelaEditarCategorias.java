package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import controller.CategoriasController;
import model.entity.Categoria;
import util.CapitalizeString;

public class TelaEditarCategorias extends JFrame {
	private MainFrame mainFrame;
	private List<Categoria> categorias;
	private boolean modoEdicao = false;
	
	private JTextField adicionarCategoriaInput;
	private JButton adicionarCategoriaBotao;
	private JPanel listaCategorias;
	private JButton botaoOk;
	
	public TelaEditarCategorias(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.categorias = new ArrayList<>();
		
		setTitle("Editar categorias de transações");
		setSize(500, 500);
		setLocationRelativeTo(this.mainFrame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		initComponents();
	}
	
	public void initComponents() {
		JPanel painelRaiz = new JPanel(new BorderLayout());
		
		JLabel titulo = new JLabel("Editar categorias");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
		titulo.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 0));
		titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel containerCategorias = new JPanel(new BorderLayout());
		
		JPanel containerAdicionarCategorias = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		containerAdicionarCategorias.setAlignmentX(Component.CENTER_ALIGNMENT);
		containerAdicionarCategorias.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		
		adicionarCategoriaInput = new JTextField();
		adicionarCategoriaInput.setFont(new Font("SansSerif", Font.PLAIN, 12));
		adicionarCategoriaInput.setMargin(new Insets(3, 5, 3, 5));
		adicionarCategoriaInput.setPreferredSize(new Dimension(120, adicionarCategoriaInput.getPreferredSize().height));
		
		adicionarCategoriaBotao = new JButton("Adicionar categoria");
		adicionarCategoriaBotao.setFont(new Font("SansSerif", Font.PLAIN, 12));
		
		JPanel capsulaListaCategorias = new JPanel(new BorderLayout());
		
		listaCategorias = new JPanel();
		listaCategorias.setLayout(new BoxLayout(listaCategorias, BoxLayout.Y_AXIS));
		
		capsulaListaCategorias.add(listaCategorias, BorderLayout.NORTH);
		
		JScrollPane listaCategoriasRolavel = new JScrollPane(capsulaListaCategorias);
		listaCategoriasRolavel.getVerticalScrollBar().setUnitIncrement(10);
		
		JPanel containerBotoesFinalizar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		containerBotoesFinalizar.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		
		botaoOk = new JButton("Ok");
		botaoOk.setFont(new Font("SansSerif", Font.PLAIN, 14));
		botaoOk.setPreferredSize(new Dimension(120, botaoOk.getPreferredSize().height));
		
		painelRaiz.add(titulo, BorderLayout.NORTH);
		containerAdicionarCategorias.add(adicionarCategoriaInput);
		containerAdicionarCategorias.add(adicionarCategoriaBotao);
		containerCategorias.add(containerAdicionarCategorias, BorderLayout.NORTH);
		containerCategorias.add(listaCategoriasRolavel, BorderLayout.CENTER);
		painelRaiz.add(containerCategorias, BorderLayout.CENTER);
		containerBotoesFinalizar.add(botaoOk);
		painelRaiz.add(containerBotoesFinalizar, BorderLayout.SOUTH);
		
		add(painelRaiz);
	}
	
	public boolean isModoEdicao() {
		return modoEdicao;
	}
	
	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}
	
	public JTextField getAdicionarCategoriaInput() {
		return adicionarCategoriaInput;
	}
	
	public JButton getAdicionarCategoriaBotao() {
		return adicionarCategoriaBotao;
	}
	
	public JButton getBotaoOk() {
		return botaoOk;
	}
	
	public void atualizarListaAdicao(String novaCategoria, ActionListener listenerBotaoEditar, ActionListener listenerBotaoExcluir) {
		JPanel linhaCategoria = new JPanel(new FlowLayout());
		
		JTextField labelCategoria = new JTextField(novaCategoria);
		labelCategoria.setEnabled(false);
		labelCategoria.setDisabledTextColor(Color.decode("#939393"));
		labelCategoria.setMargin(new Insets(3, 5, 3, 5));
		labelCategoria.setHorizontalAlignment(JTextField.CENTER);
		labelCategoria.setPreferredSize(new Dimension(160, labelCategoria.getPreferredSize().height));
		
		JButton editarCategoria = new JButton("Editar");
		editarCategoria.putClientProperty("categoria", novaCategoria);
		editarCategoria.addActionListener(listenerBotaoEditar);
		
		JButton excluirCategoria = new JButton("Deletar");
		excluirCategoria.putClientProperty("categoria", novaCategoria);
		excluirCategoria.addActionListener(listenerBotaoExcluir);
		
		linhaCategoria.add(labelCategoria);
		linhaCategoria.add(editarCategoria);
		linhaCategoria.add(excluirCategoria);
		
		listaCategorias.add(linhaCategoria);
		
		listaCategorias.revalidate();
		listaCategorias.repaint();
	}
	
	public void atualizarListaRemocao(String categoriaRemovida, JPanel linhaRemover) {
		listaCategorias.remove(linhaRemover);
		listaCategorias.revalidate();
		listaCategorias.repaint();
	}
	
	public void atualizarListaEdicao(String novoNomeCategoria, JTextField inputNomeCategoria, JButton botaoEditar, JButton botaoExcluir) {
		inputNomeCategoria.setEnabled(false);
		inputNomeCategoria.setText(novoNomeCategoria);
		
		botaoEditar.setText("Editar");
		botaoExcluir.setEnabled(true);
	}
	
	public void desabilitarBotoesEdicao(String categoriaAtual) {
		for(Component linha : listaCategorias.getComponents()) {
			JPanel linhaContainer = (JPanel)linha;
			JTextField inputCategoria = (JTextField)linhaContainer.getComponent(0);
			
			if(inputCategoria.getText().equals(categoriaAtual))
				continue;
			
			JButton botaoEditar = (JButton)linhaContainer.getComponent(1);
			JButton botaoExcluir = (JButton)linhaContainer.getComponent(2);
			
			botaoEditar.setEnabled(false);
			botaoExcluir.setEnabled(false);
		}
	}
	
	public void habilitarBotoesEdicao(String categoriaAtual) {
		for(Component linha : listaCategorias.getComponents()) {
			JPanel linhaContainer = (JPanel)linha;
			JTextField inputCategoria = (JTextField)linhaContainer.getComponent(0);
			
			if(inputCategoria.getText().equals(categoriaAtual))
				continue;
			
			JButton botaoEditar = (JButton)linhaContainer.getComponent(1);
			JButton botaoExcluir = (JButton)linhaContainer.getComponent(2);
			
			botaoEditar.setEnabled(true);
			botaoExcluir.setEnabled(true);
		}
	}
	    
	public void limparListaCategorias() {
		listaCategorias.removeAll();
	    listaCategorias.revalidate();
	    listaCategorias.repaint();
	}
}
