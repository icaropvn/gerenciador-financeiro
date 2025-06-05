package model.service;

import java.util.List;

import model.entity.Categoria;
import model.dao.CategoriaDAO;

public class GerenciadorCategorias {

    public enum TipoVerificacao {
        ADICAO,
        EDICAO
    }

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public List<Categoria> listarCategorias() {
        return categoriaDAO.buscarTodos();
    }

    public boolean existeCategoriaComDescricao(String descricaoParaVerificar, TipoVerificacao tipo, Long idAtual) {
        List<Categoria> todas = categoriaDAO.buscarTodos();
        for (Categoria c : todas) {
            if (tipo == TipoVerificacao.ADICAO) {
                if (c.getDescricao().equalsIgnoreCase(descricaoParaVerificar.trim())) {
                    return true;
                }
            } else { // EDICAO
                // Ignora a própria categoria que está sendo editada
                if (c.getId().longValue() != idAtual.longValue() &&
                    c.getDescricao().equalsIgnoreCase(descricaoParaVerificar.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Categoria adicionarCategoria(String descricao) {
        String desc = descricao.trim();
        if (desc.isEmpty()) {
            throw new RuntimeException("A descrição da categoria não pode ser vazia.");
        }
        if (existeCategoriaComDescricao(desc, TipoVerificacao.ADICAO, null)) {
            throw new RuntimeException("Já existe uma categoria com essa descrição.");
        }
        Categoria nova = new Categoria(desc);
        categoriaDAO.salvar(nova);
        return nova;
    }

    public Categoria editarCategoria(Long idCategoria, String novaDescricao) {
        String desc = novaDescricao.trim();
        if (desc.isEmpty()) {
            throw new RuntimeException("A descrição da categoria não pode ser vazia.");
        }

        Categoria existente = categoriaDAO.buscarPorId(idCategoria);
        if (existente == null) {
            throw new RuntimeException("Categoria não encontrada (ID=" + idCategoria + ").");
        }

        // verifica duplicidade ignorando a própria categoria
        if (existeCategoriaComDescricao(desc, TipoVerificacao.EDICAO, idCategoria)) {
            throw new RuntimeException("Já existe uma outra categoria com essa descrição.");
        }

        existente.setDescricao(desc);
        return categoriaDAO.atualizar(existente);
    }

    public void excluirCategoria(Long idCategoria) {
        Categoria existente = categoriaDAO.buscarPorId(idCategoria);
        if (existente == null) {
            throw new RuntimeException("Categoria não encontrada (ID=" + idCategoria + ").");
        }
        categoriaDAO.excluir(existente);
    }

    public Categoria getInstanciaCategoria(String descricao) {
        return categoriaDAO.buscarPorDescricao(descricao.trim());
    }

    public Categoria buscarPorId(Long id) {
        return categoriaDAO.buscarPorId(id);
    }
}
