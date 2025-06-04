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

    public Categoria adicionarCategoria(String descricao) {
        Categoria c = new Categoria(descricao);
        return categoriaDAO.salvarOuAtualizar(c);
    }

    public Categoria buscarPorCodigo(Integer codigo) {
        return categoriaDAO.buscarPorId(codigo);
    }

    public Categoria buscarPorDescricao(String descricao) {
        return categoriaDAO.buscarPorDescricao(descricao);
    }
    
    public boolean isThereCategoria(String descricao) {
        return buscarPorDescricao(descricao) != null;
    }

    public List<Categoria> listarTodasCategorias() {
        return categoriaDAO.buscarTodos();
    }

    public void editarCategoria(Integer codigo, String novaDescricao) {
        Categoria c = categoriaDAO.buscarPorId(codigo);
        if (c == null) {
            throw new IllegalArgumentException("Categoria não encontrada: código=" + codigo);
        }
        c.setDescricao(novaDescricao);
        categoriaDAO.salvarOuAtualizar(c);
    }
    
    public void editarCategoria(String antigaDescricao, String novaDescricao) {
        Categoria c = categoriaDAO.buscarPorDescricao(antigaDescricao);
        if (c == null) {
            throw new IllegalArgumentException("Categoria não encontrada: descrição=" + antigaDescricao);
        }
        c.setDescricao(novaDescricao);
        categoriaDAO.salvarOuAtualizar(c);
    }

    public void removerCategoria(Integer codigo) {
        Categoria c = categoriaDAO.buscarPorId(codigo);
        if (c != null) {
            categoriaDAO.excluir(c);
        }
    }
    
    public void removerCategoria(String categoria) {
        Categoria c = categoriaDAO.buscarPorDescricao(categoria);
        if (c != null) {
            categoriaDAO.excluir(c);
        }
    }
    
    public boolean isCategoriaDuplicada(String descCategoria, String descOriginal, TipoVerificacao tipoVerificacao) {
        List<Categoria> todasCategorias = categoriaDAO.buscarTodos();

        for (Categoria categoria : todasCategorias) {
            String descricaoAtual = categoria.getDescricao();

            if (tipoVerificacao == TipoVerificacao.EDICAO 
                    && descricaoAtual.equalsIgnoreCase(descOriginal)) {
                continue;
            }

            if (descricaoAtual.equalsIgnoreCase(descCategoria)) {
                return true;
            }
        }

        return false;
    }
    
    public Categoria getInstanciaCategoria(String descricao) {
        return categoriaDAO.buscarPorDescricao(descricao);
    }
}
