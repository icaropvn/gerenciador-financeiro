package controller;

import java.awt.event.*;
import view.MainFrame;
import view.TelaLogin;

public class LoginController {
	private MainFrame mainFrame;
	private TelaLogin telaLogin;
	
	public LoginController(MainFrame mainFrame, TelaLogin telaLogin) {
		this.mainFrame = mainFrame;
		this.telaLogin = telaLogin;
		
		initControllers();
	}
	
	public void initControllers() {
		telaLogin.setCreateAccountLabelListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainFrame.mostrarTela("cadastro");
			}
		});
	}
}
