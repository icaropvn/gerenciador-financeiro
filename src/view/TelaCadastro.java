package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import util.NumericDocumentFilter;

public class TelaCadastro extends JPanel {
	public TelaCadastro() {
		setLayout(new GridBagLayout());
		
		initComponents();
	}
	
	public void initComponents() {
		JLabel heading = new JLabel("<html><body style=\"text-align: center;\">Vamos criar a sua conta!</body></html>");
		heading.setVerticalAlignment(JLabel.CENTER);
		heading.setFont(new Font("SansSerif", Font.PLAIN, 24));
		heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		
		JLabel paragraph1 = new JLabel("é rápido, só levará alguns segundos");
		paragraph1.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		paragraph1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JLabel userLabel = new JLabel("Nome de usuário");
		JTextField userInput = new JTextField(20);
		userInput.setMargin(new Insets(5, 10, 10, 5));
		
		JLabel incomeLabel = new JLabel("Saldo atual da sua conta");
		JTextField incomeInput = new JTextField(20);
		incomeInput.setMargin(new Insets(5, 10, 10, 5));
		((AbstractDocument) incomeInput.getDocument()).setDocumentFilter(new NumericDocumentFilter());
		
		JLabel passwordLabel = new JLabel("Senha");
		JTextField passwordInput = new JPasswordField(20);
		passwordInput.setMargin(new Insets(5, 10, 10, 5));
		
		JLabel confirmPasswordLabel = new JLabel("Confirmar senha");
		JTextField confirmPasswordInput = new JPasswordField(20);
		confirmPasswordInput.setMargin(new Insets(5, 10, 10, 5));
		
		JButton registerButton = new JButton("Cadastrar");
		registerButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		registerButton.setPreferredSize(new Dimension(100, registerButton.getPreferredSize().height));
		
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
		gbc.insets = new Insets(20, 20, 5, 0);
		add(incomeLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 5;
		add(incomeInput, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 20, 5, 0);
		add(passwordLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 7;
		add(passwordInput, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 8;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 20, 5, 0);
		add(confirmPasswordLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 9;
		add(confirmPasswordInput, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 10;
		gbc.insets = new Insets(20, 0, 0, 0);
		add(registerButton, gbc);
	}
}
