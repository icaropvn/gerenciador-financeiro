package controller;

import java.awt.event.*;
import view.TelaPrincipal;
import view.TelaEditarCategorias;
import view.TelaAdicionarTransacao;

public class PrincipalController {
	private TelaPrincipal telaPrincipal;
	private TelaEditarCategorias telaCategorias;
	private TelaAdicionarTransacao telaTransacao;
	
	public PrincipalController(TelaPrincipal telaPrincipal, TelaEditarCategorias telaCategorias, TelaAdicionarTransacao telaTransacao) {
		this.telaPrincipal = telaPrincipal;
		this.telaCategorias = telaCategorias;
		this.telaTransacao = telaTransacao;
		
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
				telaTransacao.setVisible(true);
			}
		});
	}
	
	public void mostrarTelaEditarCategorias() {
		telaCategorias.setVisible(true);
	}
}
