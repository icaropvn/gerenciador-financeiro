package controller;

import java.awt.event.*;

import view.TelaPrincipal;
import view.TelaEditarCategorias;
import view.TelaAdicionarTransacao;
import model.GerenciadorCategorias;

public class PrincipalController {
	private TelaPrincipal telaPrincipal;
	private TelaEditarCategorias telaCategorias;
	private TelaAdicionarTransacao telaTransacao;
	private GerenciadorCategorias gerenciadorCategorias;
	
	public PrincipalController(TelaPrincipal telaPrincipal, TelaEditarCategorias telaCategorias, TelaAdicionarTransacao telaTransacao, GerenciadorCategorias gerenciadorCategorias) {
		this.telaPrincipal = telaPrincipal;
		this.telaCategorias = telaCategorias;
		this.telaTransacao = telaTransacao;
		this.gerenciadorCategorias = gerenciadorCategorias;
		
		initControllers();
	}
	
	public void initControllers() {
		telaPrincipal.getBotaoEditarCategorias().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarTelaEditarCategorias();
			}
		});
		
		telaPrincipal.getBotaoAdicionarTransacao().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				telaTransacao.atualizarListaCategorias(gerenciadorCategorias.getListaCategorias());
				telaTransacao.setVisible(true);
			}
		});
	}
	
	public void mostrarTelaEditarCategorias() {
		telaCategorias.setVisible(true);
	}
}
