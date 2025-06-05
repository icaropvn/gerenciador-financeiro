package controller;

import java.text.DecimalFormat;
import java.util.List;
import java.awt.event.*;
import javax.swing.JOptionPane;

import view.MainFrame;
import view.TelaLogin;
import view.TelaPrincipal;
import model.entity.Transacao;
import model.entity.Usuario;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;

public class LoginController {
	private MainFrame mainFrame;
	private TelaLogin telaLogin;
	private GerenciadorUsuario gerenciadorUsuario;
	private GerenciadorFinanceiro gerenciadorFinanceiro;
	private TelaPrincipal telaPrincipal;
	
	public LoginController(MainFrame mainFrame, TelaLogin telaLogin, GerenciadorUsuario gerenciadorUsuario, GerenciadorFinanceiro gerenciadorFinanceiro, TelaPrincipal telaPrincipal) {
		this.mainFrame = mainFrame;
		this.telaLogin = telaLogin;
		this.gerenciadorUsuario = gerenciadorUsuario;
		this.gerenciadorFinanceiro = gerenciadorFinanceiro;
		this.telaPrincipal = telaPrincipal;
		
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
	
	private void validarLogin() {
        String nome = telaLogin.getUserInput().getText().trim();
        String senha = telaLogin.getPasswordInput().getText().trim();
        
        if (nome.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(telaLogin, "Preencha todos os campos.", "Erro ao fazer login", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario u = gerenciadorUsuario.validarLogin(nome, senha);
        
        if (u == null) {
            JOptionPane.showMessageDialog(telaLogin, "Nome de usuário ou senha inválidos.", "Erro ao fazer login", JOptionPane.ERROR_MESSAGE);
            return;
        }

        gerenciadorUsuario.setUsuarioAtual(u);

        telaPrincipal.setSaudacao("Olá, " + u.getNome() + "!");
        List<Transacao> listaTransacoes = u.getHistoricoTransacoes();
        telaPrincipal.substituirTabelaTransacoes(listaTransacoes);

        DecimalFormat formatador = new DecimalFormat("R$ #,##0.00");
        String saldoFormatado = formatador.format(u.getSaldo());
        telaPrincipal.setSaldo(saldoFormatado);

        telaLogin.getUserInput().setText("");
        telaLogin.getPasswordInput().setText("");

        mainFrame.mostrarTela("principal");
    }
}
