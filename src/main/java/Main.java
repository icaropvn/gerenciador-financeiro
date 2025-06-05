import view.MainFrame;

import controller.LoginController;
import controller.CadastroController;
import controller.PrincipalController;

import model.service.GerenciadorCategorias;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;

public class Main {
    public static void main(String[] args) {
        GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario();
        GerenciadorCategorias gerenciadorCategorias = new GerenciadorCategorias();
        GerenciadorFinanceiro gerenciadorFinanceiro = new GerenciadorFinanceiro();

        MainFrame mainFrame = new MainFrame();
        
        LoginController loginController  = new LoginController(
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
        
        mainFrame.setVisible(true);
    }
}
