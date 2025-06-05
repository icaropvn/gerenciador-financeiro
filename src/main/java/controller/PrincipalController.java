package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;

import view.MainFrame;
import view.TelaPrincipal;
import view.TelaResumoFinanceiro;
import view.TelaEditarCategorias;
import view.TelaAdicionarTransacao;
import view.TelaEditarTransacao;

import model.entity.Categoria;
import model.entity.Transacao;
import model.service.GerenciadorCategorias;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;

/**
 * Controller da TelaPrincipal. Registra listeners para abrir, sob demanda,
 * as demais janelas (ResumoFinanceiro, EditarCategorias, AdicionarTransacao, EditarTransacao).
 */
public class PrincipalController {

    private final MainFrame mainFrame;
    private final TelaPrincipal telaPrincipal;
    private final GerenciadorCategorias gerenciadorCategorias;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;
    private final GerenciadorUsuario gerenciadorUsuario;

    public PrincipalController(
            MainFrame mainFrame,
            TelaPrincipal telaPrincipal,
            GerenciadorCategorias gerenciadorCategorias,
            GerenciadorFinanceiro gerenciadorFinanceiro,
            GerenciadorUsuario gerenciadorUsuario
    ) {
        this.mainFrame = mainFrame;
        this.telaPrincipal = telaPrincipal;
        this.gerenciadorCategorias = gerenciadorCategorias;
        this.gerenciadorFinanceiro = gerenciadorFinanceiro;
        this.gerenciadorUsuario = gerenciadorUsuario;

        initController();
    }

    private void initController() {
        // 1) Ao clicar em “Resumo Financeiro”, cria a tela e seu controller
        telaPrincipal.getBotaoResumoFinanceiro().addActionListener(e -> abrirTelaResumo());

        // 2) Ao clicar em “Editar Categorias”, cria a tela e seu controller
        telaPrincipal.getBotaoEditarCategorias().addActionListener(e -> abrirTelaEditarCategorias());

        // 3) Ao clicar em “Adicionar Transação”, cria a tela e seu controller
        telaPrincipal.getBotaoAdicionarTransacao().addActionListener(e -> abrirTelaAdicionarTransacao());

        // 4) Ao clicar em “Editar Transação”, cria a tela e seu controller
        telaPrincipal.getTabelaTransacoes().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Verifica se foram dois cliques no mesmo ponto
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    abrirTelaEditarTransacao();
                }
            }
        });

        // 5) Ao clicar em “Logout”
        telaPrincipal.getBotaoSair().addActionListener(e -> fazerLogout());
    }

    /**
     * Cria e exibe a TelaResumoFinanceiro e instancia seu controller.
     */
    private void abrirTelaResumo() {
        TelaResumoFinanceiro tela = new TelaResumoFinanceiro(mainFrame);
        new ResumoFinanceiroController(
            tela,
            gerenciadorCategorias,
            gerenciadorFinanceiro,
            gerenciadorUsuario
        );
        tela.setVisible(true);
    }

    /**
     * Cria e exibe a TelaEditarCategorias e instancia seu controller,
     * que já carregará as categorias do banco.
     */
    private void abrirTelaEditarCategorias() {
        TelaEditarCategorias tela = new TelaEditarCategorias(mainFrame);
        new CategoriasController(
            mainFrame,
            tela,
            gerenciadorCategorias
        );
        tela.setVisible(true);
    }

    /**
     * Cria e exibe a TelaAdicionarTransacao e instancia seu controller.
     */
    private void abrirTelaAdicionarTransacao() {
        TelaAdicionarTransacao tela = new TelaAdicionarTransacao(mainFrame);
        new TransacoesController(
            tela,
            telaPrincipal,
            gerenciadorUsuario,
            gerenciadorCategorias,
            gerenciadorFinanceiro
        );
        tela.setVisible(true);
    }

    private void abrirTelaEditarTransacao() {
        Transacao selecionada = telaPrincipal.getTransacaoSelecionada();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(
                telaPrincipal,
                "Selecione uma transação para editar.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        TelaEditarTransacao tela = new TelaEditarTransacao(mainFrame);

	     // 1) informa ao “view” qual transação está sendo editada
	     tela.setTransacaoEmEdicao(selecionada);
	     tela.atualizarListaCategorias(gerenciadorCategorias.listarCategorias());
	     // 2) inicializa os campos (valor, descrição, classificação, categoria, data)
	     tela.inicializarInputs(selecionada);
	
	     // 3) agora sim, cria o controller e exibe a janela
	     new EditarTransacaoController(
	         tela,
	         gerenciadorUsuario,
	         gerenciadorFinanceiro,
	         gerenciadorCategorias,
	         telaPrincipal
	     );
	     tela.setVisible(true);
    }

    private void fazerLogout() {
        gerenciadorUsuario.setUsuarioAtual(null);
        mainFrame.getTelaPrincipal().setVisible(false);
        mainFrame.mostrarTela("login");
    }
}
