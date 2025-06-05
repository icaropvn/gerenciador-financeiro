package controller;

import java.text.DecimalFormat;
import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;

import view.MainFrame;
import view.TelaLogin;
import view.TelaPrincipal;

import model.entity.Transacao;
import model.entity.Usuario;
import model.entity.Categoria;
import model.dao.CategoriaDAO;
import model.service.GerenciadorUsuario;
import model.service.GerenciadorFinanceiro;

public class LoginController {

    private MainFrame mainFrame;
    private TelaLogin telaLogin;
    private TelaPrincipal telaPrincipal;
    private GerenciadorUsuario gerenciadorUsuario;
    private GerenciadorFinanceiro gerenciadorFinanceiro;

    public LoginController(MainFrame mainFrame,
                           TelaLogin telaLogin,
                           TelaPrincipal telaPrincipal,
                           GerenciadorUsuario gerenciadorUsuario,
                           GerenciadorFinanceiro gerenciadorFinanceiro) {
        this.mainFrame = mainFrame;
        this.telaLogin = telaLogin;
        this.telaPrincipal = telaPrincipal;
        this.gerenciadorUsuario = gerenciadorUsuario;
        this.gerenciadorFinanceiro = gerenciadorFinanceiro;
        initController();
    }

    private void initController() {
    	telaLogin.getLoginButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tentativaLogin();
			}
		});
		
		telaLogin.setCreateAccountLabelListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainFrame.mostrarTela("cadastro");
			}
		});
    }

    private void tentativaLogin() {
        String nome = telaLogin.getUserInput().getText().trim().toLowerCase();
        String senha = telaLogin.getPasswordInput().getText().trim();

        // Validação básica de preenchimento
        if (nome.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(
                telaLogin,
                "Preencha usuário e senha.",
                "Erro ao entrar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Usuario u = gerenciadorUsuario.validarLogin(nome, senha);

            if (u == null) {
                // Credenciais inválidas
                JOptionPane.showMessageDialog(
                    telaLogin,
                    "Usuário ou senha incorretos.",
                    "Erro ao entrar",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 3) Se chegou aqui, login OK. Define usuário atual no serviço
            gerenciadorUsuario.setUsuarioAtual(u);

            // 4) Carrega a lista de categorias do banco e preenche o filtro
            List<Categoria> listaCategorias = new CategoriaDAO().buscarTodos();
            telaPrincipal.atualizarFiltroCategorias(listaCategorias);

            // 5) Exibe saudação no canto (ou onde estiver na tela principal)
            telaPrincipal.setSaudacao("Olá, " + u.getNome() + "!");

            // 6) Busca transações diretamente da entidade (já inicializada no DAO)
            List<Transacao> listaTransacoes = u.getHistoricoTransacoes();
            // Caso prefira recarregar do BD, poderia usar:
            // List<Transacao> listaTransacoes = gerenciadorFinanceiro.buscarTransacoesPorUsuario(u.getId());
            telaPrincipal.substituirTabelaTransacoes(listaTransacoes);

            // 7) Exibe o saldo formatado
            DecimalFormat formatador = new DecimalFormat("R$ #,##0.00");
            String saldoFormatado = formatador.format(u.getSaldo());
            telaPrincipal.setSaldo(saldoFormatado);

            // 8) Limpa campos da tela de login
            telaLogin.getUserInput().setText("");
            telaLogin.getPasswordInput().setText("");

            // 9) Troca para a tela principal
            mainFrame.mostrarTela("principal");

        } catch (Exception ex) {
            // Qualquer erro inesperado (conexão, DAO, etc.)
            JOptionPane.showMessageDialog(
                telaLogin,
                "Falha ao tentar entrar: " + ex.getMessage(),
                "Erro ao entrar",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}

