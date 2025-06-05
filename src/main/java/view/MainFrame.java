package view;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private TelaLogin telaLogin;
	private TelaCadastro telaCadastro;
	private TelaPrincipal telaPrincipal;
	
	public MainFrame() {
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		
		telaLogin = new TelaLogin();
		telaCadastro = new TelaCadastro();
		telaPrincipal = new TelaPrincipal();
		
		cardPanel.add(telaLogin, "login");
		cardPanel.add(telaCadastro, "cadastro");
		cardPanel.add(telaPrincipal, "principal");
		
		add(cardPanel);
		
		setTitle("Gerenciador Financeiro");
		setSize(1100, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void mostrarTela(String nomeTela) {
		cardLayout.show(cardPanel, nomeTela);
	}
	
	public TelaLogin getTelaLogin() {
		return telaLogin;
	}
	
	public TelaCadastro getTelaCadastro() {
		return telaCadastro;
	}
	
	public TelaPrincipal getTelaPrincipal() {
		return telaPrincipal;
	}
}
