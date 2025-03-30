package controller;

import java.awt.event.*;

import javax.swing.JOptionPane;

import view.MainFrame;
import view.TelaLogin;
import model.GerenciadorUsuario;
import util.CriptografarSenha;

public class LoginController {
	private MainFrame mainFrame;
	private TelaLogin telaLogin;
	private GerenciadorUsuario gerenciadorUsuario;
	
	public LoginController(MainFrame mainFrame, TelaLogin telaLogin, GerenciadorUsuario gerenciadorUsuario) {
		this.mainFrame = mainFrame;
		this.telaLogin = telaLogin;
		this.gerenciadorUsuario = gerenciadorUsuario;
		
		initControllers();
	}
	
	public void initControllers() {
		telaLogin.getLoginButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validarLogin();
			}
		});
		
		telaLogin.setCreateAccountLabelListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainFrame.mostrarTela("cadastro");
			}
		});
	}
	
	public void validarLogin() {
		String nome = telaLogin.getUserInput().getText().trim().toLowerCase();
		String senha = telaLogin.getPasswordInput().getText().trim();
		
		if(nome.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(telaLogin, "Preencha todos os campos.", "Erro ao fazer login", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!gerenciadorUsuario.validarNomeUsuario(nome) ||
		   !gerenciadorUsuario.validarSenhaUsuario(nome, senha)) {
			JOptionPane.showMessageDialog(telaLogin, "Nome de usuário ou senha inválidos.", "Erro ao fazer login", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// ir para tela principal
	}
}
