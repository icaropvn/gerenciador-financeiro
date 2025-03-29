package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaLogin extends JPanel {
	private JLabel createAccountLabel;
	
	public TelaLogin() {
		setLayout(new GridBagLayout());
		
		initComponents();
	}
	
	public void initComponents() {
		JLabel heading = new JLabel("<html><body style=\"text-align: center;\">Bem-vindo ao seu<br>Gerenciador Financeiro!</body></html>");
		heading.setVerticalAlignment(JLabel.CENTER);
		heading.setFont(new Font("SansSerif", Font.PLAIN, 24));
		heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		
		JLabel paragraph1 = new JLabel("faça login para acessar sua conta");
		paragraph1.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		paragraph1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JLabel userLabel = new JLabel("Usuário");
		JTextField userInput = new JTextField(20);
		userInput.setMargin(new Insets(5, 10, 10, 5));
		
		JLabel passwordLabel = new JLabel("Senha");
		JTextField passwordInput = new JPasswordField(20);
		passwordInput.setMargin(new Insets(5, 10, 10, 5));
		
		JButton loginButton = new JButton("Entrar");
		loginButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		loginButton.setPreferredSize(new Dimension(100, loginButton.getPreferredSize().height));
		
		JLabel paragraph2 = new JLabel("não tem uma conta?");
		
		createAccountLabel = new JLabel("<html><a href=\\\"\\\">Crie uma agora mesmo</a></html>");
		createAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// adição dos elementos em cadeia
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		add(heading, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 1;
		add(paragraph1, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 20, 5, 0);
		add(userLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 3;
		add(userInput, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 20, 5, 0);
		add(passwordLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 5;
		add(passwordInput, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 6;
		gbc.insets = new Insets(20, 0, 0, 0);
		add(loginButton, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 7;
		gbc.insets = new Insets(20, 0, 0, 0);
		add(paragraph2, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 8;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(createAccountLabel, gbc);
	}
	
	public void setCreateAccountLabelListener(MouseListener listener) {
		createAccountLabel.addMouseListener(listener);
	}
}