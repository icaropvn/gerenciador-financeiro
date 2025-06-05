package controller;

import java.util.List;
import java.awt.event.*;
import javax.swing.*;

import view.MainFrame;
import view.TelaEditarCategorias;

import model.entity.Categoria;
import model.service.GerenciadorCategorias;

/**
 * Controller para gerenciar operações de adicionar, editar e excluir categorias
 * na tela de edição de categorias.
 */
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
        // 1) Carregar todas as categorias existentes quando a tela for exibida
        carregarCategoriasExistentes();

        // 2) Escuta o botão “Adicionar”
        view.getBotaoAdicionar().addActionListener(e -> adicionarCategoria());

        // 3) Se houver botão “Voltar”, vincula ação (retornar para a tela principal)
        if (view.getBotaoVoltar() != null) {
            view.getBotaoVoltar().addActionListener(e -> {
                view.dispose();
            });
        }
    }

    /**
     * Carrega todas as categorias do banco e delega à View a atualização do painel.
     */
    private void carregarCategoriasExistentes() {
        List<Categoria> categorias = gerenciadorCategorias.listarCategorias();
        view.limparListaCategorias();
        for (Categoria c : categorias) {
            adicionarLinhaNaView(c);
        }
    }

    /**
     * Tenta adicionar nova categoria usando o texto do campo de entrada.
     * Exibe erro em JOptionPane se falhar.
     */
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
            // Serviço retorna a entidade persistida (com ID)
            Categoria nova = gerenciadorCategorias.adicionarCategoria(descricao);

            // Adiciona apenas a nova linha na View (sem recarregar tudo)
            adicionarLinhaNaView(nova);

            // Limpa campo de texto para próxima inclusão
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

    /**
     * Cria uma linha gráfica na View para a categoria informada,
     * incluindo os botões “Editar” e “Excluir” com os listeners corretos.
     */
    private void adicionarLinhaNaView(Categoria categoria) {
        Long id = categoria.getId();
        String descricao = categoria.getDescricao();

        // Listener para editar essa categoria
        ActionListener listenerEditar = e -> editarCategoria(id);

        // Listener para excluir essa categoria
        ActionListener listenerExcluir = e -> excluirCategoria(id);

        view.inserirLinhaCategoria(id, descricao, listenerEditar, listenerExcluir);
    }

    /**
     * Altera a descrição da categoria com o ID informado.
     * Abre um diálogo (JOptionPane.showInputDialog) para obter novo nome.
     */
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
            // Usuário cancelou o prompt
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
            // Tenta salvar a alteração
            Categoria atualizada = gerenciadorCategorias.editarCategoria(categoriaId, novaDescricao);

            // Atualiza o texto do JLabel na mesma linha
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

    /**
     * Exclui a categoria com o ID informado, após confirmação do usuário.
     */
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
            // Se tudo der certo, remove a linha gráfica da View
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
