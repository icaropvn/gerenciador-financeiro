import view.MainFrame;
import controller.*;
import model.*;

public class Main {
	public static void main(String[] args) {
		// instanciamento das classes de dados
		GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario();
		
		// instanciamento do frame principal da aplicação
		MainFrame mainFrame = new MainFrame();
		
		// atribuição dos controllers de cada tela
		LoginController loginController = new LoginController(mainFrame, mainFrame.getTelaLogin(), gerenciadorUsuario, mainFrame.getTelaPrincipal());
		CadastroController cadastroController = new CadastroController(mainFrame, mainFrame.getTelaCadastro(), gerenciadorUsuario);
		
		mainFrame.setVisible(true);
	}
}