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
        telaPrincipal.getBotaoResumoFinanceiro().addActionListener(e -> abrirTelaResumo());

        telaPrincipal.getBotaoEditarCategorias().addActionListener(e -> abrirTelaEditarCategorias());

        telaPrincipal.getBotaoAdicionarTransacao().addActionListener(e -> abrirTelaAdicionarTransacao());

        telaPrincipal.getTabelaTransacoes().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    abrirTelaEditarTransacao();
                }
            }
        });

        telaPrincipal.getBotaoSair().addActionListener(e -> fazerLogout());
        
        telaPrincipal.getBotaoAplicarFiltros().addActionListener(e -> aplicarFiltros());

        telaPrincipal.getBotaoLimparFiltros().addActionListener(e -> limparFiltros());
    }

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

    private void abrirTelaEditarCategorias() {
        TelaEditarCategorias tela = new TelaEditarCategorias(mainFrame);
        new CategoriasController(
            mainFrame,
            tela,
            gerenciadorCategorias
        );
        tela.setVisible(true);
    }

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

	     tela.setTransacaoEmEdicao(selecionada);
	     tela.atualizarListaCategorias(gerenciadorCategorias.listarCategorias());
	     tela.inicializarInputs(selecionada);
	
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
            String textoInicio = telaPrincipal.getInputDataInicial().getText().trim();
            String textoFim    = telaPrincipal.getInputDataFinal().getText().trim();

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

            if (dataInicioTemp != null && dataFimTemp != null && dataInicioTemp.isAfter(dataFimTemp)) {
                JOptionPane.showMessageDialog(
                    telaPrincipal,
                    "Data inicial não pode ser posterior à data final.",
                    "Erro nos filtros",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            final LocalDate dataInicio = dataInicioTemp;
            final LocalDate dataFim    = dataFimTemp;

            String classificacaoSelecionada = (String) telaPrincipal.getSelectClassificacao().getSelectedItem();
            String categoriaSelecionada     = (String) telaPrincipal.getSelectCategoria().getSelectedItem();

            Long usuarioId = gerenciadorUsuario.getUsuarioAtual().getId();
            List<Transacao> todasTransacoes = gerenciadorFinanceiro.buscarTransacoesPorUsuario(usuarioId);

            List<Transacao> filtradas = todasTransacoes.stream()
                .filter(t -> {
                    if (dataInicio != null) {
                        return !t.getData().isBefore(dataInicio);
                    }
                    return true;
                })
                .filter(t -> {
                    if (dataFim != null) {
                        return !t.getData().isAfter(dataFim);
                    }
                    return true;
                })
                .filter(t -> {
                    if (!"Classificação".equals(classificacaoSelecionada)) {
                        return classificacaoSelecionada.equalsIgnoreCase(t.getClassificacao());
                    }
                    return true;
                })
                .filter(t -> {
                    if (!"Categoria".equals(categoriaSelecionada)) {
                        return categoriaSelecionada.equalsIgnoreCase(t.getCategoria().getDescricao());
                    }
                    return true;
                })
                .collect(Collectors.toList());

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
            telaPrincipal.getInputDataInicial().setText("");
            telaPrincipal.getInputDataFinal().setText("");

            telaPrincipal.getSelectClassificacao().setSelectedItem("Classificação");
            telaPrincipal.getSelectCategoria().setSelectedItem("Categoria");

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
