package controller;

import java.awt.event.*;
import javax.swing.JOptionPane;

import view.*;
import model.*;
import model.entity.Usuario;
import model.service.GerenciadorUsuario;
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
		telaCadastro.getBotaoVoltar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				telaCadastro.getUserInput().setText("");
				telaCadastro.getIncomeInput().setText("");
				telaCadastro.getPasswordInput().setText("");
				telaCadastro.getConfirmPasswordInput().setText("");
				mainFrame.mostrarTela("login");
			}
		});
		
		telaCadastro.getRegisterButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validarCadastro();
			}
		});
	}
	
	 private void validarCadastro() {
        String nome = telaCadastro.getUserInput().getText().trim().toLowerCase();
        String salarioString = telaCadastro.getIncomeInput().getText().trim();
        String senha = telaCadastro.getPasswordInput().getText().trim();
        String confirmarSenha = telaCadastro.getConfirmPasswordInput().getText().trim();

        if (nome.isEmpty() || salarioString.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            JOptionPane.showMessageDialog(telaCadastro, "Preencha todos os campos.", "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (gerenciadorUsuario.buscarPorNome(nome) != null) {
            JOptionPane.showMessageDialog(telaCadastro, "Este nome de usuário já está sendo utilizado.", "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(telaCadastro, "Os campos \"Senha\" e \"Confirmar senha\" devem ser iguais.", "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        double salarioDouble = Double.parseDouble(salarioString.replace(",", "."));
        String senhaCriptografada = CriptografarSenha.criptografarSenha(senha);

        gerenciadorUsuario.adicionarUsuario(nome, senhaCriptografada, salarioDouble);

        telaCadastro.getUserInput().setText("");
        telaCadastro.getIncomeInput().setText("");
        telaCadastro.getPasswordInput().setText("");
        telaCadastro.getConfirmPasswordInput().setText("");

        mainFrame.mostrarTela("login");
        JOptionPane.showMessageDialog(mainFrame, "Sua conta foi criada com sucesso!", "Cadastro realizado", JOptionPane.INFORMATION_MESSAGE);
    }
}
