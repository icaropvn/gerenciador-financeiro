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

    // Formato de data esperado nos campos de texto
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Formatador numérico para exibir valores monetários
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
        // 1) Ao abrir a janela, popular o combo de categorias com dados do banco:
        List<String> descricoes = gerenciadorCategorias.listarCategorias()
            .stream()
            .map(c -> c.getDescricao())
            .toList();
        view.getSelectCategoria().removeAllItems();
        view.getSelectCategoria().addItem("Categoria");
        for (String desc : descricoes) {
            view.getSelectCategoria().addItem(desc);
        }

        // 2) Registrar listener do botão “Aplicar Filtros”
        view.getBotaoAplicarFiltros().addActionListener(e -> aplicarFiltros());

        // 3) Registrar listener do botão “Limpar Filtros”
        view.getBotaoLimparFiltros().addActionListener(e -> view.limparFiltros());

        // 4) Registrar listener do botão “Voltar”
        view.getBotaoVoltar().addActionListener(e -> {
            view.limparFiltros();
            view.dispose();
        });
    }

    private void aplicarFiltros() {
        try {
            // 1) Lê texto dos campos de data
            String textoInicio = view.getInputDataInicial().getText().trim();
            String textoFim    = view.getInputDataFinal().getText().trim();

            LocalDate dataInicio = null;
            LocalDate dataFim    = null;

            // 2) Converte data início, se informado
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

            // 3) Converte data fim, se informado
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

            // 4) Se ambas preenchidas, valida ordem
            if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
                JOptionPane.showMessageDialog(
                    view,
                    "Data inicial não pode ser posterior à data final.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 5) Lê classificação e categoria selecionadas
            String classificacaoSelecionada = (String) view.getSelectClassificacao().getSelectedItem();
            String categoriaSelecionada     = (String) view.getSelectCategoria().getSelectedItem();

            // 6) Busca todas as transações do usuário atual
            Long usuarioId = gerenciadorUsuario.getUsuarioAtual().getId();
            List<Transacao> todasTransacoes = gerenciadorFinanceiro.buscarTransacoesPorUsuario(usuarioId);

            // 7) Filtrar passo a passo, ignorando critérios não informados:
            final LocalDate fi = dataInicio; // variáveis final para lambda
            final LocalDate ff = dataFim;

            List<Transacao> filtradas = todasTransacoes.stream()
                // Filtrar por data início
                .filter(t -> {
                    if (fi != null) {
                        return !t.getData().isBefore(fi);
                    }
                    return true;
                })
                // Filtrar por data fim
                .filter(t -> {
                    if (ff != null) {
                        return !t.getData().isAfter(ff);
                    }
                    return true;
                })
                // Filtrar por classificação
                .filter(t -> {
                    if (!"Classificação".equals(classificacaoSelecionada)) {
                        return classificacaoSelecionada.equalsIgnoreCase(t.getClassificacao());
                    }
                    return true;
                })
                // Filtrar por categoria
                .filter(t -> {
                    if (!"Categoria".equals(categoriaSelecionada)) {
                        return categoriaSelecionada.equalsIgnoreCase(t.getCategoria().getDescricao());
                    }
                    return true;
                })
                .toList();

            // 8) Calcular totais
            double totalReceitas = gerenciadorFinanceiro.calcularTotalReceitas(filtradas);
            double totalDespesas = gerenciadorFinanceiro.calcularTotalDespesas(filtradas);
            double saldoFinal    = totalReceitas - totalDespesas;

            // 9) Montar texto de período (“dd/MM/yyyy - dd/MM/yyyy” ou caso apenas uma data)
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

            // 10) Montar texto de classificação e categoria
            String textoClassificacao = 
                ("Classificação".equals(classificacaoSelecionada) ? "Todas" : classificacaoSelecionada);
            String textoCategoria = 
                ("Categoria".equals(categoriaSelecionada) ? "Todas" : categoriaSelecionada);

            // 11) Exibir nos labels da view
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
