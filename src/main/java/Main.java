import view.MainFrame;
import view.TelaEditarCategorias;
import view.TelaEditarTransacao;
import view.TelaResumoFinanceiro;
import view.TelaAdicionarTransacao;
import controller.*;
import model.*;
import model.service.GerenciadorCategorias;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;

public class Main {
	public static void main(String[] args) {
		// instanciamento das classes de dados
		GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario();
		GerenciadorCategorias gerenciadorCategorias = new GerenciadorCategorias();
		GerenciadorFinanceiro gerenciadorFinanceiro = new GerenciadorFinanceiro();
		
		// instanciamento do frame principal da aplicação
		MainFrame mainFrame = new MainFrame();
		
		// instanciamento das telas à parte da aplicação
		TelaResumoFinanceiro telaResumo = new TelaResumoFinanceiro(mainFrame);
		TelaEditarCategorias telaCategorias = new TelaEditarCategorias(mainFrame);
		TelaAdicionarTransacao telaTransacoes = new TelaAdicionarTransacao(mainFrame);
		TelaEditarTransacao telaEditarTransacoes = new TelaEditarTransacao(mainFrame);
		
		// instanciamento dos controllers das telas
		LoginController loginController = new LoginController(mainFrame, mainFrame.getTelaLogin(), gerenciadorUsuario, gerenciadorFinanceiro, mainFrame.getTelaPrincipal());
		CadastroController cadastroController = new CadastroController(mainFrame, mainFrame.getTelaCadastro(), gerenciadorUsuario);
		PrincipalController principalController = new PrincipalController(mainFrame, mainFrame.getTelaPrincipal(), telaResumo, telaCategorias, telaTransacoes, telaEditarTransacoes, gerenciadorCategorias, gerenciadorFinanceiro, gerenciadorUsuario);
		ResumoFinanceiroController resumoFinanceiroController = new ResumoFinanceiroController(telaResumo, gerenciadorCategorias, gerenciadorFinanceiro, gerenciadorUsuario);
		CategoriasController categoriasController = new CategoriasController(telaCategorias, gerenciadorCategorias, gerenciadorUsuario, mainFrame.getTelaPrincipal());
		TransacoesController transacoesController = new TransacoesController(telaTransacoes, mainFrame.getTelaPrincipal(), gerenciadorUsuario, gerenciadorCategorias, gerenciadorFinanceiro);
		EditarTransacaoController editarTransacaoController = new EditarTransacaoController(telaEditarTransacoes, gerenciadorUsuario, gerenciadorFinanceiro, gerenciadorCategorias, mainFrame.getTelaPrincipal());
		
		mainFrame.setVisible(true);
	}
}