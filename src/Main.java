import view.MainFrame;
import controller.LoginController;

public class Main {
	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		
		// atribuição dos controllers de cada tela
		LoginController loginController = new LoginController(mainFrame, mainFrame.getTelaLogin());
		
		mainFrame.setVisible(true);
	}
}