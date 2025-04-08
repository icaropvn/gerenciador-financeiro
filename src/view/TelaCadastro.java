package view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.*;
import util.NumericDocumentFilter;

public class TelaCadastro extends JPanel {
	private JButton botaoVoltar;
	private JTextField userInput;
	private JTextField incomeInput;
	private JTextField passwordInput;
	private JTextField confirmPasswordInput;
	private JButton registerButton;
	
	public TelaCadastro() {
		setLayout(new BorderLayout());
		
		initComponents();
	}
	
	public void initComponents() {
		JPanel containerBotaoVoltar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
		
		botaoVoltar = new JButton("Voltar");
		
		containerBotaoVoltar.add(botaoVoltar);
		
		JPanel painelPrincipal = new JPanel(new GridBagLayout());
		
		JLabel heading = new JLabel("<html><body style=\"text-align: center;\">Vamos criar a sua conta!</body></html>");
		heading.setVerticalAlignment(JLabel.CENTER);
		heading.setFont(new Font("SansSerif", Font.PLAIN, 24));
		heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		
		JLabel paragraph1 = new JLabel("é rápido, só levará alguns segundos");
		paragraph1.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		paragraph1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JLabel userLabel = new JLabel("Nome de usuário");
		userInput = new JTextField(20);
		userInput.setMargin(new Insets(5, 10, 10, 5));
		
		JLabel incomeLabel = new JLabel("Saldo atual da sua conta");
		incomeInput = new JTextField(20);
		incomeInput.setMargin(new Insets(5, 10, 10, 5));
		((AbstractDocument) incomeInput.getDocument()).setDocumentFilter(new NumericDocumentFilter());
		
		JLabel passwordLabel = new JLabel("Senha");
		passwordInput = new JPasswordField(20);
		passwordInput.setMargin(new Insets(5, 10, 10, 5));
		
		JLabel confirmPasswordLabel = new JLabel("Confirmar senha");
		confirmPasswordInput = new JPasswordField(20);
		confirmPasswordInput.setMargin(new Insets(5, 10, 10, 5));
		
		registerButton = new JButton("Cadastrar");
		registerButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		registerButton.setPreferredSize(new Dimension(100, registerButton.getPreferredSize().height));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		painelPrincipal.add(heading, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 1;
		painelPrincipal.add(paragraph1, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 20, 5, 0);
		painelPrincipal.add(userLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 3;
		painelPrincipal.add(userInput, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 20, 5, 0);
		painelPrincipal.add(incomeLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 5;
		painelPrincipal.add(incomeInput, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 20, 5, 0);
		painelPrincipal.add(passwordLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 7;
		painelPrincipal.add(passwordInput, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 8;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 20, 5, 0);
		painelPrincipal.add(confirmPasswordLabel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 9;
		painelPrincipal.add(confirmPasswordInput, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 10;
		gbc.insets = new Insets(20, 0, 0, 0);
		painelPrincipal.add(registerButton, gbc);
		
		add(containerBotaoVoltar, BorderLayout.NORTH);
		add(painelPrincipal, BorderLayout.CENTER);
	}
	
	public JButton getBotaoVoltar() {
		return botaoVoltar;
	}
	
	public JTextField getUserInput() {
		return userInput;
	}
	
	public JTextField getIncomeInput() {
		return incomeInput;
	}
	
	public JTextField getPasswordInput() {
		return passwordInput;
	}
	
	public JTextField getConfirmPasswordInput() {
		return confirmPasswordInput;
	}
	
	public JButton getRegisterButton() {
		return registerButton;
	}
}
