package controller;

import java.util.List;
import java.awt.event.*;
import javax.swing.*;

import view.MainFrame;
import view.TelaEditarCategorias;

import model.entity.Categoria;
import model.service.GerenciadorCategorias;

public class CategoriasController {

    private final MainFrame mainFrame;
    private final TelaEditarCategorias view;
    private final GerenciadorCategorias gerenciadorCategorias;

    public CategoriasController(MainFrame mainFrame,
                                TelaEditarCategorias view,
                                GerenciadorCategorias gerenciadorCategorias) {
        this.mainFrame = mainFrame;
        this.view = view;
        this.gerenciadorCategorias = gerenciadorCategorias;
        initController();
    }

    private void initController() {
        carregarCategoriasExistentes();

        view.getBotaoAdicionar().addActionListener(e -> adicionarCategoria());

        if (view.getBotaoVoltar() != null) {
            view.getBotaoVoltar().addActionListener(e -> {
                view.dispose();
            });
        }
    }

    private void carregarCategoriasExistentes() {
        List<Categoria> categorias = gerenciadorCategorias.listarCategorias();
        view.limparListaCategorias();
        for (Categoria c : categorias) {
            adicionarLinhaNaView(c);
        }
    }

    private void adicionarCategoria() {
        String descricao = view.getInputNomeCategoria().getText().trim();
        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(
                view,
                "Informe o nome da categoria.",
                "Erro ao adicionar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Categoria nova = gerenciadorCategorias.adicionarCategoria(descricao);
            
            adicionarLinhaNaView(nova);

            view.getInputNomeCategoria().setText("");
            mainFrame.getTelaPrincipal().atualizarFiltroCategorias(gerenciadorCategorias.listarCategorias());

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                view,
                "Não foi possível adicionar: " + ex.getMessage(),
                "Erro ao adicionar",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void adicionarLinhaNaView(Categoria categoria) {
        Long id = categoria.getId();
        String descricao = categoria.getDescricao();

        ActionListener listenerEditar = e -> editarCategoria(id);

        ActionListener listenerExcluir = e -> excluirCategoria(id);

        view.inserirLinhaCategoria(id, descricao, listenerEditar, listenerExcluir);
    }

    private void editarCategoria(Long categoriaId) {
        Categoria atual = gerenciadorCategorias.buscarPorId(categoriaId);
        if (atual == null) {
            JOptionPane.showMessageDialog(
                view,
                "Categoria não encontrada (ID=" + categoriaId + ").",
                "Erro ao editar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String nomeAntigo = atual.getDescricao();
        String novaDescricao = JOptionPane.showInputDialog(
            view,
            "Digite a nova descrição para a categoria:",
            nomeAntigo
        );

        if (novaDescricao == null) {
            return;
        }
        novaDescricao = novaDescricao.trim();
        if (novaDescricao.isEmpty()) {
            JOptionPane.showMessageDialog(
                view,
                "A descrição não pode ficar vazia.",
                "Erro ao editar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Categoria atualizada = gerenciadorCategorias.editarCategoria(categoriaId, novaDescricao);

            view.atualizarLinhaDescricao(categoriaId, atualizada.getDescricao());
            mainFrame.getTelaPrincipal().atualizarFiltroCategorias(gerenciadorCategorias.listarCategorias());

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                view,
                "Não foi possível editar: " + ex.getMessage(),
                "Erro ao editar",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirCategoria(Long categoriaId) {
        int resp = JOptionPane.showConfirmDialog(
            view,
            "Deseja realmente excluir esta categoria?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            gerenciadorCategorias.excluirCategoria(categoriaId);
            view.removerLinhaCategoria(categoriaId);
            mainFrame.getTelaPrincipal().atualizarFiltroCategorias(gerenciadorCategorias.listarCategorias());

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                view,
                "Não foi possível excluir: " + ex.getMessage(),
                "Erro ao excluir",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
