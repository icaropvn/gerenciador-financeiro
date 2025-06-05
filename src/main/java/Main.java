import view.MainFrame;
import view.TelaEditarCategorias;
import view.TelaEditarTransacao;
import view.TelaResumoFinanceiro;
import view.TelaAdicionarTransacao;

import controller.LoginController;
import controller.CadastroController;
import controller.PrincipalController;

import model.service.GerenciadorCategorias;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;

public class Main {
    public static void main(String[] args) {
        // 1) Cria instâncias dos serviços (singleton de escopo simples)
        GerenciadorUsuario gerenciadorUsuario       = new GerenciadorUsuario();
        GerenciadorCategorias gerenciadorCategorias = new GerenciadorCategorias();
        GerenciadorFinanceiro gerenciadorFinanceiro = new GerenciadorFinanceiro();

        // 2) Cria o frame principal (que já contém as telas internas, como Login, Cadastro e TelaPrincipal)
        MainFrame mainFrame = new MainFrame();
        
        // 3) Instancia apenas os controllers “de entrada” (Login e Cadastro), 
        //    que serão usados assim que a aplicação abrir:
        LoginController  loginController  = new LoginController(
            mainFrame,
            mainFrame.getTelaLogin(),
            mainFrame.getTelaPrincipal(),
            gerenciadorUsuario,
            gerenciadorFinanceiro
        );

        CadastroController cadastroController = new CadastroController(
            mainFrame,
            mainFrame.getTelaCadastro(),
            gerenciadorUsuario
        );

        PrincipalController principalController = new PrincipalController(
            mainFrame,
            mainFrame.getTelaPrincipal(),
            gerenciadorCategorias,
            gerenciadorFinanceiro,
            gerenciadorUsuario
        );

        // 5) Exibe a aplicação
        mainFrame.setVisible(true);
    }
}
