package controller;

import java.awt.event.*;
import javax.swing.JOptionPane;

import view.*;
import model.*;
import util.CriptografarSenha;

public class CadastroController {
	private MainFrame mainFrame;
	private TelaCadastro telaCadastro;
	private GerenciadorUsuario gerenciadorUsuario;
	
	public CadastroController(MainFrame mainFrame, TelaCadastro telaCadastro, GerenciadorUsuario gerenciadorUsuario) {
		this.mainFrame = mainFrame;
		this.telaCadastro = telaCadastro;
		this.gerenciadorUsuario = gerenciadorUsuario;
		
		initControllers();
	}
	
	public void initControllers() {
		telaCadastro.getRegisterButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validarCadastro();
			}
		});
	}
	
	public void validarCadastro() {
		String nome = telaCadastro.getUserInput().getText().trim().toLowerCase();
		String salarioString = telaCadastro.getIncomeInput().getText().trim();
		String senha = telaCadastro.getPasswordInput().getText().trim();
		String confirmarSenha = telaCadastro.getConfirmPasswordInput().getText().trim();
		
		// verifica se todos os campos foram preenchidos
		if(nome.isEmpty() || salarioString.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
			JOptionPane.showMessageDialog(telaCadastro, "Preencha todos os campos.", "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// verifica se o nome de usuário já existe
		if(gerenciadorUsuario.validarNomeUsuario(nome)) {
			JOptionPane.showMessageDialog(telaCadastro, "Este nome de usuário já está sendo utilizado.", "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// verifica se os campos de senha correspondem
		if(!senha.equals(confirmarSenha)) {
			JOptionPane.showMessageDialog(telaCadastro, "Os campos \"Senha\" e \"Confirmar senha\" devem ser iguais.", "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// cria usuário e adiciona aos cadastros
		double salarioDouble = Double.parseDouble(salarioString.replace(",", "."));
		String senhaCriptografada = CriptografarSenha.criptografarSenha(senha);
		
		Usuario novoUsuario = new Usuario(nome, senhaCriptografada, salarioDouble);
		gerenciadorUsuario.adicionarUsuario(novoUsuario);
		
		mainFrame.mostrarTela("login");
		JOptionPane.showMessageDialog(mainFrame, "Sua conta foi criada com sucesso!", "Cadastro realizado", JOptionPane.INFORMATION_MESSAGE);
	}
}
