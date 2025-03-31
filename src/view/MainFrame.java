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
		
		/* aqui as telas serão instanciadas e adicionadas ao painel principal (cardPanel)
		 * dessa forma poderemos apenas trocar a tela que está em exibição ao decorrer da execução
		 */
		
		// instanciamento das telas
		telaLogin = new TelaLogin();
		telaCadastro = new TelaCadastro();
		telaPrincipal = new TelaPrincipal();
		
		// adição das telas ao painel principal
		cardPanel.add(telaLogin, "login");
		cardPanel.add(telaCadastro, "cadastro");
		cardPanel.add(telaPrincipal, "principal");
		
		add(cardPanel);
		
		setTitle("Gerenciador Financeiro");
		setSize(1100, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// método para fazer a troca da tela em exibição
	public void mostrarTela(String nomeTela) {
		cardLayout.show(cardPanel, nomeTela);
	}
	
	// getters e setters
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
