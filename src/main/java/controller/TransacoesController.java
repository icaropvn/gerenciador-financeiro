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
        carregarCategoriasNoCombo();

        view.getBotaoAdicionar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarAdicionarTransacao();
            }
        });

        view.getBotaoCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });

        view.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                view.dispose();
            }
        });
    }


    private void carregarCategoriasNoCombo() {
        List<Categoria> listaCategorias = gerenciadorCategorias.listarCategorias();
        view.atualizarListaCategorias(listaCategorias);
    }

    private void executarAdicionarTransacao() {
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

        String descricao = view.getDescricaoInput().getText().trim();
        if (descricao.isEmpty()) {
            descricao = "";
        }

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
            Transacao novaTransacao = new Transacao(
                classificacao,
                valor,
                categoria,
                data,
                descricao,
                usuario
            );
            gerenciadorFinanceiro.adicionarTransacao(novaTransacao);

            if ("Receita".equalsIgnoreCase(classificacao)) {
                gerenciadorFinanceiro.adicionarSaldo(usuario, valor);
            } else {
                gerenciadorFinanceiro.retirarSaldo(usuario, valor);
            }

            telaPrincipal.adicionarTransacaoTabela(novaTransacao);

            DecimalFormat formatadorSaldo = new DecimalFormat("#,##0.00");
            String saldoFormatado = formatadorSaldo.format(usuario.getSaldo());
            telaPrincipal.atualizarSaldo(saldoFormatado);

            view.getSelectClassificacao().setSelectedItem("Classificação");
            view.getSelectCategoria().setSelectedItem("Categoria");
            view.getValorInput().setText("");
            view.getDescricaoInput().setText("");
            view.getDataInput().setValue(new Date());

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
