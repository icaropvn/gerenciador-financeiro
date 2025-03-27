package view;

import java.awt.*;
import javax.swing.*;

public class TelaLogin extends JFrame {
	public TelaLogin() {
		setTitle("Gerenciador Financeiro - Login");
		setSize(1100, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initComponents();
	}
	
	public void initComponents() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		JLabel heading = new JLabel("<html><body style=\"text-align: center;\">Bem-vindo ao seu<br>Gerenciador Financeiro!</body></html>");
		heading.setVerticalAlignment(JLabel.CENTER);
		
		JLabel paragraph1 = new JLabel("<html><body style=\"text-align: center;\">faça login para acessar sua conta<br>ou <a href=\"\">crie uma agora mesmo</a></body></html>");
		
		// adição dos elementos em cadeia
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		mainPanel.add(heading, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 1;
		mainPanel.add(paragraph1, gbc);
		
		add(mainPanel);
	}
}
