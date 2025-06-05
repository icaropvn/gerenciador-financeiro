package controller;

import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.JOptionPane;
import model.entity.Transacao;
import model.service.GerenciadorCategorias;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;
import view.TelaResumoFinanceiro;

public class ResumoFinanceiroController {

    private final TelaResumoFinanceiro view;
    private final GerenciadorCategorias gerenciadorCategorias;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;
    private final GerenciadorUsuario gerenciadorUsuario;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat DF;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DF = new DecimalFormat("#,##0.00", symbols);
    }

    public ResumoFinanceiroController(
            TelaResumoFinanceiro view,
            GerenciadorCategorias gerenciadorCategorias,
            GerenciadorFinanceiro gerenciadorFinanceiro,
            GerenciadorUsuario gerenciadorUsuario
    ) {
        this.view = view;
        this.gerenciadorCategorias   = gerenciadorCategorias;
        this.gerenciadorFinanceiro   = gerenciadorFinanceiro;
        this.gerenciadorUsuario      = gerenciadorUsuario;

        initController();
    }

    private void initController() {
        List<String> descricoes = gerenciadorCategorias.listarCategorias()
            .stream()
            .map(c -> c.getDescricao())
            .toList();
        view.getSelectCategoria().removeAllItems();
        view.getSelectCategoria().addItem("Categoria");
        for (String desc : descricoes) {
            view.getSelectCategoria().addItem(desc);
        }

        view.getBotaoAplicarFiltros().addActionListener(e -> aplicarFiltros());

        view.getBotaoLimparFiltros().addActionListener(e -> view.limparFiltros());

        view.getBotaoVoltar().addActionListener(e -> {
            view.limparFiltros();
            view.dispose();
        });
    }

    private void aplicarFiltros() {
        try {
            String textoInicio = view.getInputDataInicial().getText().trim();
            String textoFim    = view.getInputDataFinal().getText().trim();

            LocalDate dataInicio = null;
            LocalDate dataFim    = null;

            if (!textoInicio.isEmpty()) {
                try {
                    dataInicio = LocalDate.parse(textoInicio, FORMATTER);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                        view,
                        "Data inicial inválida. Use dd/MM/yyyy.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            if (!textoFim.isEmpty()) {
                try {
                    dataFim = LocalDate.parse(textoFim, FORMATTER);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                        view,
                        "Data final inválida. Use dd/MM/yyyy.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
                JOptionPane.showMessageDialog(
                    view,
                    "Data inicial não pode ser posterior à data final.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String classificacaoSelecionada = (String) view.getSelectClassificacao().getSelectedItem();
            String categoriaSelecionada     = (String) view.getSelectCategoria().getSelectedItem();

            Long usuarioId = gerenciadorUsuario.getUsuarioAtual().getId();
            List<Transacao> todasTransacoes = gerenciadorFinanceiro.buscarTransacoesPorUsuario(usuarioId);

            final LocalDate fi = dataInicio;
            final LocalDate ff = dataFim;

            List<Transacao> filtradas = todasTransacoes.stream()
                .filter(t -> {
                    if (fi != null) {
                        return !t.getData().isBefore(fi);
                    }
                    return true;
                })
                .filter(t -> {
                    if (ff != null) {
                        return !t.getData().isAfter(ff);
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
                .toList();

            double totalReceitas = gerenciadorFinanceiro.calcularTotalReceitas(filtradas);
            double totalDespesas = gerenciadorFinanceiro.calcularTotalDespesas(filtradas);
            double saldoFinal    = totalReceitas - totalDespesas;

            String periodoTexto;
            if (fi == null && ff == null) {
                periodoTexto = "Todos os períodos";
            } else if (fi == null) {
                periodoTexto = "Até " + textoFim;
            } else if (ff == null) {
                periodoTexto = textoInicio + " em diante";
            } else {
                periodoTexto = textoInicio + " - " + textoFim;
            }

            String textoClassificacao = 
                ("Classificação".equals(classificacaoSelecionada) ? "Todas" : classificacaoSelecionada);
            String textoCategoria = 
                ("Categoria".equals(categoriaSelecionada) ? "Todas" : categoriaSelecionada);

            view.setPeriodoAnalisado(periodoTexto);
            view.setClassificacao(textoClassificacao);
            view.setCategoria(textoCategoria);
            view.setTotalReceitas("R$ " + DF.format(totalReceitas));
            view.setTotalDespesas("R$ " + DF.format(totalDespesas));
            view.setSaldo("R$ " + DF.format(saldoFinal));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                view,
                "Erro ao aplicar filtros: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}
