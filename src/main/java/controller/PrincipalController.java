package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        
        telaPrincipal.getBotaoAplicarFiltros().addActionListener(e -> aplicarFiltros());

        // 7) Listener para “Limpar filtros”
        telaPrincipal.getBotaoLimparFiltros().addActionListener(e -> limparFiltros());
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
    
    private void aplicarFiltros() {
    	try {
            // 1) Lê o texto dos dois JTextField de data (podem estar vazios)
            String textoInicio = telaPrincipal.getInputDataInicial().getText().trim();
            String textoFim    = telaPrincipal.getInputDataFinal().getText().trim();

            // 2) Converte para LocalDate em variáveis temporárias
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataInicioTemp = null;
            LocalDate dataFimTemp    = null;

            if (!textoInicio.isEmpty()) {
                try {
                    dataInicioTemp = LocalDate.parse(textoInicio, fmt);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                        telaPrincipal,
                        "Data inicial inválida. Use dd/MM/yyyy.",
                        "Erro nos filtros",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            if (!textoFim.isEmpty()) {
                try {
                    dataFimTemp = LocalDate.parse(textoFim, fmt);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                        telaPrincipal,
                        "Data final inválida. Use dd/MM/yyyy.",
                        "Erro nos filtros",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            // 3) Verifica ordem se ambas não forem nulas
            if (dataInicioTemp != null && dataFimTemp != null && dataInicioTemp.isAfter(dataFimTemp)) {
                JOptionPane.showMessageDialog(
                    telaPrincipal,
                    "Data inicial não pode ser posterior à data final.",
                    "Erro nos filtros",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 4) Agora atribuímos a variáveis final para usar dentro do lambda
            final LocalDate dataInicio = dataInicioTemp;
            final LocalDate dataFim    = dataFimTemp;

            // 5) Lê classificação e categoria selecionadas
            String classificacaoSelecionada = (String) telaPrincipal.getSelectClassificacao().getSelectedItem();
            String categoriaSelecionada     = (String) telaPrincipal.getSelectCategoria().getSelectedItem();

            // 6) Carrega todas as transações do usuário logado
            Long usuarioId = gerenciadorUsuario.getUsuarioAtual().getId();
            List<Transacao> todasTransacoes = gerenciadorFinanceiro.buscarTransacoesPorUsuario(usuarioId);

            // 7) Filtra usando as variáveis final dataInicio e dataFim
            List<Transacao> filtradas = todasTransacoes.stream()
                // Filtra por data inicial, se dataInicio não for null
                .filter(t -> {
                    if (dataInicio != null) {
                        return !t.getData().isBefore(dataInicio);
                    }
                    return true;
                })
                // Filtra por data final, se dataFim não for null
                .filter(t -> {
                    if (dataFim != null) {
                        return !t.getData().isAfter(dataFim);
                    }
                    return true;
                })
                // Filtra por classificação, se for diferente de "Classificação"
                .filter(t -> {
                    if (!"Classificação".equals(classificacaoSelecionada)) {
                        return classificacaoSelecionada.equalsIgnoreCase(t.getClassificacao());
                    }
                    return true;
                })
                // Filtra por categoria, se for diferente de "Categoria"
                .filter(t -> {
                    if (!"Categoria".equals(categoriaSelecionada)) {
                        return categoriaSelecionada.equalsIgnoreCase(t.getCategoria().getDescricao());
                    }
                    return true;
                })
                .collect(Collectors.toList());

            // 8) Recarrega a tabela com a lista filtrada
            telaPrincipal.substituirTabelaTransacoes(filtradas);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                telaPrincipal,
                "Erro ao aplicar filtros: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
    
    private void limparFiltros() {
    	try {
            // A) Limpa os campos de data (JTextField) para ficar em branco
            telaPrincipal.getInputDataInicial().setText("");
            telaPrincipal.getInputDataFinal().setText("");

            // B) Reseta os combo boxes
            telaPrincipal.getSelectClassificacao().setSelectedItem("Classificação");
            telaPrincipal.getSelectCategoria().setSelectedItem("Categoria");

            // C) Recarrega TUDO sem filtro
            Long usuarioId = gerenciadorUsuario.getUsuarioAtual().getId();
            List<Transacao> todasTransacoes = gerenciadorFinanceiro.buscarTransacoesPorUsuario(usuarioId);
            telaPrincipal.substituirTabelaTransacoes(todasTransacoes);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                telaPrincipal,
                "Erro ao limpar filtros: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}
