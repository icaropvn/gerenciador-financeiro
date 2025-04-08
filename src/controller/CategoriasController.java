package controller;

import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.GerenciadorCategorias;
import model.GerenciadorCategorias.TipoVerificacao;
import model.GerenciadorUsuario;
import model.Usuario;
import util.CapitalizeString;
import view.TelaEditarCategorias;
import view.TelaPrincipal;



import java.util.ArrayList;
import model.Categoria;



public class CategoriasController {
	private TelaEditarCategorias view;
	private GerenciadorCategorias gerenciador;
	private GerenciadorUsuario gerenciadorUsuario;
	private TelaPrincipal telaPrincipal;
	
	private JTextField inputCategoriaEdicao;
	private JButton botaoEditarEdicao;
	private JButton botaoExcluirEdicao;
	
	public CategoriasController(TelaEditarCategorias view, GerenciadorCategorias gerenciador, GerenciadorUsuario gerenciadorUsuario, TelaPrincipal telaPrincipal) {
		this.view = view;
		this.gerenciador = gerenciador;
		this.gerenciadorUsuario = gerenciadorUsuario;
		this.telaPrincipal = telaPrincipal;
		
		initControllers();
	}
	
	public void initControllers() {
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				acoesAoFecharJanela();
			}
		});
		
		view.getAdicionarCategoriaBotao().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				validarAdicaoCategoria();
			}
		});
		
		view.getBotaoOk().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				acoesAoFecharJanela();
			}
		});
	}
	
	public void validarAdicaoCategoria() {
		String novaCategoria = view.getAdicionarCategoriaInput().getText().trim();
		
		// validar campo preenchido
		if(novaCategoria.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Preencha o nome da categoria para adicioná-la.", "Erro ao adicionar categoria", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		novaCategoria = CapitalizeString.capitalize(novaCategoria);
		
		// validar categoria com nome igual
		if(gerenciador.isCategoriaDuplicada(novaCategoria, novaCategoria, TipoVerificacao.ADICAO)) {
			JOptionPane.showMessageDialog(view, "Essa categoria já está cadastrada.", "Erro ao adicionar categoria", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		view.getAdicionarCategoriaInput().setText("");
		gerenciador.adicionarCategoria(novaCategoria);
		view.atualizarListaAdicao(novaCategoria, getListenerBotaoEditar(), getListenerBotaoExcluir());
		telaPrincipal.atualizarFiltroCategorias(gerenciador.getListaCategorias());
	}
	
	// TESTE TEMPORARIO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//	public void validarAdicaoCategoria() {
//		ArrayList<String> categorias = new ArrayList<>();
//		
//		categorias.add("Alimentação");
//		categorias.add("Transporte");
//		categorias.add("Lazer");
//		categorias.add("Educação");
//		categorias.add("Salário");
//
//		for(String categoria : categorias) {
//			gerenciador.adicionarCategoria(categoria);
//			view.atualizarListaAdicao(categoria, getListenerBotaoEditar(), getListenerBotaoExcluir());
//			telaPrincipal.atualizarFiltroCategorias(gerenciador.getListaCategorias());
//		}
//	}
	
	public void validarRemocaoCategoria(JButton botaoExcluir) {
		// validar se tem alguma transação com essa categoria antes de remover
		ArrayList<Usuario> listaUsuarios = gerenciadorUsuario.getUsuariosCadastrados();
		boolean existeTransacoes = false;
		
		for(Usuario usuario : listaUsuarios) {
			existeTransacoes = usuario.existeTransacaoComCategoria((String)botaoExcluir.getClientProperty("categoria"));
			
			if(existeTransacoes)
				break;
		}
		
		if(existeTransacoes) {
			JOptionPane.showMessageDialog(view, "Ops! Parece que existe transações que utilizam essa categoria. Ela não pode ser removida.", "Erro ao remover categoria", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int confirmarExclusao = JOptionPane.showConfirmDialog(view, "Tem certeza que deseja excluir essa categoria?", "Confirmação de exclusão de categoria", JOptionPane.YES_NO_OPTION);
		
		if(confirmarExclusao == JOptionPane.YES_OPTION) {
			String categoriaRemover = (String)botaoExcluir.getClientProperty("categoria");
			gerenciador.removerCategoria(categoriaRemover);
			view.atualizarListaRemocao(categoriaRemover, (JPanel)botaoExcluir.getParent());
		}
	}
	
	public void validarEdicaoCategoria(JTextField inputNomeCategoria, JButton botaoEditar, JButton botaoExcluir) {
		if("Editar".equals(botaoEditar.getText())) {
			setInputCategoriaEdicao(inputNomeCategoria);
			setBotaoEditarEdicao(botaoEditar);
			setBotaoExcluirEdicao(botaoExcluir);
			
			inputNomeCategoria.setEnabled(true);
			inputNomeCategoria.requestFocusInWindow();
			inputNomeCategoria.selectAll();
			botaoEditar.setText("Salvar Alteração");
			botaoExcluir.setEnabled(false);
			
			view.setModoEdicao(true);
			view.desabilitarBotoesEdicao((String)botaoEditar.getClientProperty("categoria"));
			return;
		}
		
		String descricaoOriginal = (String)botaoEditar.getClientProperty("categoria");
		String novoNomeCategoria = inputNomeCategoria.getText().trim();
		
		// valida o preenchimento do novo nome
		if(novoNomeCategoria.isEmpty()) {
			JOptionPane.showMessageDialog(view, "O novo nome da categoria não pode ser vazio.", "Erro ao editar categoria", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		novoNomeCategoria = CapitalizeString.capitalize(novoNomeCategoria);
		
		// valida a disponibilidade do novo nome
		if(gerenciador.isCategoriaDuplicada(novoNomeCategoria, descricaoOriginal, TipoVerificacao.EDICAO)) {
			JOptionPane.showMessageDialog(view, "Este nome já está sendo usado por outra categoria.", "Erro ao editar categoria", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		gerenciador.editarCategoria(novoNomeCategoria, descricaoOriginal);
		
		botaoEditar.putClientProperty("categoria", novoNomeCategoria);
		botaoExcluir.putClientProperty("categoria", novoNomeCategoria);
		
		view.setModoEdicao(false);
		view.habilitarBotoesEdicao(novoNomeCategoria);
		view.atualizarListaEdicao(novoNomeCategoria, inputNomeCategoria, botaoEditar, botaoExcluir);
	}
	
	public void acoesAoFecharJanela() {
		if(view.isModoEdicao()) {
			getInputCategoriaEdicao().setEnabled(false);
			getInputCategoriaEdicao().setText((String)getBotaoEditarEdicao().getClientProperty("categoria"));
			getBotaoEditarEdicao().setText("Editar");
			getBotaoExcluirEdicao().setEnabled(true);
			
			view.habilitarBotoesEdicao((String)getBotaoEditarEdicao().getClientProperty("categoria"));
		}
		
		view.getAdicionarCategoriaInput().setText("");
		view.setModoEdicao(false);
		view.dispose();
	}
	
	public JTextField getInputCategoriaEdicao() {
		return inputCategoriaEdicao;
	}
	
	public void setInputCategoriaEdicao(JTextField inputCategoriaEdicao) {
		this.inputCategoriaEdicao = inputCategoriaEdicao;
	}
	
	public JButton getBotaoEditarEdicao() {
		return botaoEditarEdicao;
	}
	
	public void setBotaoEditarEdicao(JButton botaoEditarEdicao) {
		this.botaoEditarEdicao = botaoEditarEdicao;
	}
	
	public JButton getBotaoExcluirEdicao() {
		return botaoExcluirEdicao;
	}
	
	public void setBotaoExcluirEdicao(JButton botaoExcluirEdicao) {
		this.botaoExcluirEdicao = botaoExcluirEdicao;
	}
	
	private ActionListener getListenerBotaoExcluir() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton botaoExcluir = (JButton) e.getSource();
				validarRemocaoCategoria(botaoExcluir);
			}
		};
	}
	
	private ActionListener getListenerBotaoEditar() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel linhaRegistroCategoria = (JPanel)((JButton)e.getSource()).getParent();
				
				JTextField inputNomeCategoria = (JTextField)linhaRegistroCategoria.getComponent(0);
				JButton botaoEditar = (JButton) e.getSource();
				JButton botaoExcluir = (JButton)linhaRegistroCategoria.getComponent(2);
				
				validarEdicaoCategoria(inputNomeCategoria, botaoEditar, botaoExcluir);
			}
		};
	}
}
