package controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import view.TelaAdicionarTransacao;
import view.TelaPrincipal;

import model.entity.Categoria;
import model.entity.Transacao;
import model.entity.Usuario;
import model.service.GerenciadorCategorias;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;

/**
 * Controller responsável pelo fluxo de “Adicionar Nova Transação”.
 * Regras:
 *  1) Ao clicar em “Adicionar” (botão OK), persiste no banco, fecha a janela,
 *     limpa todos os campos e atualiza a tabela de transações na TelaPrincipal.
 *  2) Ao clicar em “Cancelar” ou fechar a janela, apenas fecha sem limpar campos.
 */
public class TransacoesController {

    private final TelaAdicionarTransacao view;
    private final TelaPrincipal telaPrincipal;
    private final GerenciadorUsuario gerenciadorUsuario;
    private final GerenciadorCategorias gerenciadorCategorias;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;

    public TransacoesController(
            TelaAdicionarTransacao view,
            TelaPrincipal telaPrincipal,
            GerenciadorUsuario gerenciadorUsuario,
            GerenciadorCategorias gerenciadorCategorias,
            GerenciadorFinanceiro gerenciadorFinanceiro
    ) {
        this.view = view;
        this.telaPrincipal = telaPrincipal;
        this.gerenciadorUsuario = gerenciadorUsuario;
        this.gerenciadorCategorias = gerenciadorCategorias;
        this.gerenciadorFinanceiro = gerenciadorFinanceiro;

        initController();
    }

    private void initController() {
        // 1) Popula combo de categorias assim que a janela for aberta
        carregarCategoriasNoCombo();

        // 2) Botão "Adicionar" (OK)
        view.getBotaoAdicionar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarAdicionarTransacao();
            }
        });

        // 3) Botão "Cancelar" fecha sem limpar
        view.getBotaoCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });

        // 4) Ao fechar (clicar no X), mesma lógica de “Cancelar”
        view.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Apenas fecha, não limpa campos
                view.dispose();
            }
        });
    }

    /**
     * Carrega todas as categorias do banco e atualiza o JComboBox na view.
     */
    private void carregarCategoriasNoCombo() {
        List<Categoria> listaCategorias = gerenciadorCategorias.listarCategorias();
        view.atualizarListaCategorias(listaCategorias);
    }

    /**
     * Lê campos da view, valida, persiste a nova transação, limpa campos,
     * fecha a janela e atualiza a tabela de transações na TelaPrincipal.
     */
    private void executarAdicionarTransacao() {
        // 1) Validação de preenchimento: classificação
        String classificacao = (String) view.getSelectClassificacao().getSelectedItem();
        if (classificacao == null || classificacao.equals("Classificação")) {
            JOptionPane.showMessageDialog(
                view,
                "Defina a classificação.",
                "Erro ao adicionar transação",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 2) Validação: categoria
        String categoriaDesc = (String) view.getSelectCategoria().getSelectedItem();
        if (categoriaDesc == null || categoriaDesc.equals("Categoria")) {
            JOptionPane.showMessageDialog(
                view,
                "Defina a categoria.",
                "Erro ao adicionar transação",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 3) Validação: valor
        String valorStr = view.getValorInput().getText().trim();
        if (valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(
                view,
                "Preencha o valor.",
                "Erro ao adicionar transação",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr.replace(",", "."));
            if (valor <= 0) {
                JOptionPane.showMessageDialog(
                    view,
                    "Informe um valor positivo.",
                    "Erro ao adicionar transação",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                view,
                "Valor inválido.",
                "Erro ao adicionar transação",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 4) Validação: data (agora JSpinner)
        Date dataDate = (Date) view.getDataInput().getValue();
        if (dataDate == null) {
            JOptionPane.showMessageDialog(
                view,
                "Defina a data.",
                "Erro ao adicionar transação",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        LocalDate data = dataDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 5) Descrição (opcional)
        String descricao = view.getDescricaoInput().getText().trim();
        if (descricao.isEmpty()) {
            descricao = "";
        }

        // 6) Obtém usuário atual (deve estar logado)
        Usuario usuario = gerenciadorUsuario.getUsuarioAtual();
        if (usuario == null) {
            JOptionPane.showMessageDialog(
                view,
                "Nenhum usuário autenticado.",
                "Erro interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 7) Recupera a Categoria gerenciada
        Categoria categoria = gerenciadorCategorias.getInstanciaCategoria(categoriaDesc);
        if (categoria == null) {
            JOptionPane.showMessageDialog(
                view,
                "Categoria selecionada não encontrada.",
                "Erro ao adicionar transação",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            // 8) Cria entidade Transacao e persiste via serviço
            Transacao novaTransacao = new Transacao(
                classificacao,
                valor,
                categoria,
                data,
                descricao,
                usuario
            );
            gerenciadorFinanceiro.adicionarTransacao(novaTransacao);

            // 9) Ajusta saldo do usuário conforme classificação (serviço já faz ajuste)
            if ("Receita".equalsIgnoreCase(classificacao)) {
                gerenciadorFinanceiro.adicionarSaldo(usuario, valor);
            } else {
                gerenciadorFinanceiro.retirarSaldo(usuario, valor);
            }

            // 10) Atualiza tabela na TelaPrincipal: adiciona somente essa nova transação
            telaPrincipal.adicionarTransacaoTabela(novaTransacao);

            // 11) Atualiza saldo exibido na TelaPrincipal
            DecimalFormat formatadorSaldo = new DecimalFormat("#,##0.00");
            String saldoFormatado = formatadorSaldo.format(usuario.getSaldo());
            telaPrincipal.atualizarSaldo(saldoFormatado);

            // 12) Limpa todos os campos da view
            view.getSelectClassificacao().setSelectedItem("Classificação");
            view.getSelectCategoria().setSelectedItem("Categoria");
            view.getValorInput().setText("");
            view.getDescricaoInput().setText("");
            // Ajuste do JSpinner: coloca data atual ou null
            view.getDataInput().setValue(new Date());

            // 13) Fecha a janela
            view.dispose();
            
            model.util.DebugDatabasePrinter.imprimirTodasTabelas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                view,
                "Erro ao salvar transação: " + ex.getMessage(),
                "Erro ao adicionar transação",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}
