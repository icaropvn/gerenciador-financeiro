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
	private static final long serialVersionUID = 1L;

    private MainFrame mainFrame;
    private JPanel listaCategorias;
    private JScrollPane scrollPane;
    private JTextField inputNomeCategoria;
    private JButton botaoAdicionar;
    private JButton botaoVoltar;
	
	public TelaEditarCategorias(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
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
		
		inputNomeCategoria = new JTextField();
		inputNomeCategoria.setFont(new Font("SansSerif", Font.PLAIN, 12));
		inputNomeCategoria.setMargin(new Insets(3, 5, 3, 5));
		inputNomeCategoria.setPreferredSize(new Dimension(120, inputNomeCategoria.getPreferredSize().height));
		
		botaoAdicionar = new JButton("Adicionar categoria");
		botaoAdicionar.setFont(new Font("SansSerif", Font.PLAIN, 12));
		
		JPanel capsulaListaCategorias = new JPanel(new BorderLayout());
		
		listaCategorias = new JPanel();
		listaCategorias.setLayout(new BoxLayout(listaCategorias, BoxLayout.Y_AXIS));
		
		capsulaListaCategorias.add(listaCategorias, BorderLayout.NORTH);
		
		JScrollPane listaCategoriasRolavel = new JScrollPane(capsulaListaCategorias);
		listaCategoriasRolavel.getVerticalScrollBar().setUnitIncrement(10);
		
		JPanel containerBotoesFinalizar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		containerBotoesFinalizar.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		
		botaoVoltar = new JButton("Ok");
		botaoVoltar.setFont(new Font("SansSerif", Font.PLAIN, 14));
		botaoVoltar.setPreferredSize(new Dimension(120, botaoVoltar.getPreferredSize().height));
		
		painelRaiz.add(titulo, BorderLayout.NORTH);
		containerAdicionarCategorias.add(inputNomeCategoria);
		containerAdicionarCategorias.add(botaoAdicionar);
		containerCategorias.add(containerAdicionarCategorias, BorderLayout.NORTH);
		containerCategorias.add(listaCategoriasRolavel, BorderLayout.CENTER);
		painelRaiz.add(containerCategorias, BorderLayout.CENTER);
		containerBotoesFinalizar.add(botaoVoltar);
		painelRaiz.add(containerBotoesFinalizar, BorderLayout.SOUTH);
		
		add(painelRaiz);
	}
	
	public JTextField getInputNomeCategoria() {
        return inputNomeCategoria;
    }

    public JButton getBotaoAdicionar() {
        return botaoAdicionar;
    }

    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }

    public JPanel getListaCategorias() {
        return listaCategorias;
    }

    public void limparListaCategorias() {
        listaCategorias.removeAll();
        listaCategorias.revalidate();
        listaCategorias.repaint();
    }

    
    public void inserirLinhaCategoria(Long idCategoria, String descricao, ActionListener listenerEditar, ActionListener listenerExcluir) {
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel lbl = new JLabel(descricao);
        lbl.setName("lbl_" + idCategoria);

        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        btnEditar.addActionListener(listenerEditar);
        btnExcluir.addActionListener(listenerExcluir);

        linha.add(lbl);
        linha.add(btnEditar);
        linha.add(btnExcluir);

        linha.setName("linha_" + idCategoria);

        listaCategorias.add(linha);
        listaCategorias.revalidate();
        listaCategorias.repaint();
    }

    public void atualizarLinhaDescricao(Long idCategoria, String novaDescricao) {
        for (Component comp : listaCategorias.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel linha = (JPanel) comp;
                if (("linha_" + idCategoria).equals(linha.getName())) {
                    Component c0 = linha.getComponent(0);
                    if (c0 instanceof JLabel) {
                        ((JLabel) c0).setText(novaDescricao);
                    }
                    break;
                }
            }
        }
    }

    public void removerLinhaCategoria(Long idCategoria) {
        for (Component comp : listaCategorias.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel linha = (JPanel) comp;
                if (("linha_" + idCategoria).equals(linha.getName())) {
                    listaCategorias.remove(linha);
                    listaCategorias.revalidate();
                    listaCategorias.repaint();
                    break;
                }
            }
        }
    }
}