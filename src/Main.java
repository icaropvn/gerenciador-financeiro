import view.MainFrame;
import view.TelaEditarCategorias;
import view.TelaAdicionarTransacao;
import controller.*;
import model.*;

public class Main {
	public static void main(String[] args) {
		// instanciamento das classes de dados
		GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario();
		GerenciadorCategorias gerenciadorCategorias = new GerenciadorCategorias();
		GerenciadorFinanceiro gerenciadorFinanceiro = new GerenciadorFinanceiro();
		
		// instanciamento do frame principal da aplicação
		MainFrame mainFrame = new MainFrame();
		
		// instanciamento das telas à parte da aplicação
		TelaEditarCategorias telaCategorias = new TelaEditarCategorias(mainFrame);
		TelaAdicionarTransacao telaTransacoes = new TelaAdicionarTransacao(mainFrame);
		
		// instanciamento dos controllers das telas
		LoginController loginController = new LoginController(mainFrame, mainFrame.getTelaLogin(), gerenciadorUsuario, gerenciadorFinanceiro, mainFrame.getTelaPrincipal());
		CadastroController cadastroController = new CadastroController(mainFrame, mainFrame.getTelaCadastro(), gerenciadorUsuario);
		PrincipalController principalController = new PrincipalController(mainFrame, mainFrame.getTelaPrincipal(), telaCategorias, telaTransacoes, gerenciadorCategorias, gerenciadorFinanceiro, gerenciadorUsuario);
		CategoriasController categoriasController = new CategoriasController(telaCategorias, gerenciadorCategorias, gerenciadorUsuario, mainFrame.getTelaPrincipal());
		TransacoesController transacoesController = new TransacoesController(telaTransacoes, mainFrame.getTelaPrincipal(), gerenciadorUsuario, gerenciadorCategorias, gerenciadorFinanceiro);
		
		mainFrame.setVisible(true);
	}
}