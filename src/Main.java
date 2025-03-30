import view.MainFrame;
import controller.*;
import model.*;

public class Main {
	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		
		// instanciamento das classes de dados
		GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario();
		
		// atribuição dos controllers de cada tela
		LoginController loginController = new LoginController(mainFrame, mainFrame.getTelaLogin(), gerenciadorUsuario);
		CadastroController cadastroController = new CadastroController(mainFrame, mainFrame.getTelaCadastro(), gerenciadorUsuario);
		
		mainFrame.setVisible(true);
	}
}