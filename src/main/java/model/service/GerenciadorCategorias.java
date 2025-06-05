package model.service;

import java.util.List;

import model.entity.Categoria;
import model.dao.CategoriaDAO;

/**
 * Serviço para adicionar, editar e excluir categorias, garantindo
 * que não haja duplicidade de descrição (case-insensitive).
 */
public class GerenciadorCategorias {

    public enum TipoVerificacao {
        ADICAO,
        EDICAO
    }

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    /**
     * Retorna todas as categorias cadastradas, ordenadas pelo DAO.
     */
    public List<Categoria> listarCategorias() {
        return categoriaDAO.buscarTodos();
    }

    /**
     * Verifica se já existe alguma categoria com a descrição fornecida.
     *
     * @param descricaoParaVerificar Descrição nova a verificar.
     * @param tipo                   Se for ADICAO, ignora ID; se EDICAO,
     *                               ignora a própria categoria de ID passado.
     * @param idAtual                ID da categoria que está sendo editada (ou null se for ADICAO).
     * @return true se encontrar outra categoria com mesma descrição (ignora case); false caso contrário.
     */
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

    /**
     * Cria e persiste uma nova categoria, se a descrição ainda não existir.
     *
     * @param descricao Nova descrição (sem espaços em branco nas pontas).
     * @return A entidade Categoria recém-persistida (com ID gerado).
     * @throws RuntimeException se já existir categoria com mesma descrição.
     */
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

    /**
     * Edita uma categoria existente, alterando sua descrição.
     *
     * @param idCategoria   ID da categoria a ser editada.
     * @param novaDescricao Nova descrição (trim e não-vazia).
     * @return A entidade Categoria atualizada (merge).
     * @throws RuntimeException se não encontrar a categoria, ou se
     *                          outra categoria já tiver a mesma descrição.
     */
    public Categoria editarCategoria(Long idCategoria, String novaDescricao) {
        String desc = novaDescricao.trim();
        if (desc.isEmpty()) {
            throw new RuntimeException("A descrição da categoria não pode ser vazia.");
        }

        Categoria existente = categoriaDAO.buscarPorId(idCategoria);
        if (existente == null) {
            throw new RuntimeException("Categoria não encontrada (ID=" + idCategoria + ").");
        }

        // Verifica duplicidade ignorando a própria categoria
        if (existeCategoriaComDescricao(desc, TipoVerificacao.EDICAO, idCategoria)) {
            throw new RuntimeException("Já existe uma outra categoria com essa descrição.");
        }

        existente.setDescricao(desc);
        return categoriaDAO.atualizar(existente);
    }

    /**
     * Exclui a categoria de ID fornecido.
     * Se houver constraints no BD (por exemplo, transações ligadas a essa categoria),
     * a tentativa de exclusão lançará uma exceção do Hibernate, que é reempacotada
     * aqui em RuntimeException.
     *
     * @param idCategoria ID da categoria a ser removida.
     * @throws RuntimeException se a categoria não existir ou não puder ser removida.
     */
    public void excluirCategoria(Long idCategoria) {
        Categoria existente = categoriaDAO.buscarPorId(idCategoria);
        if (existente == null) {
            throw new RuntimeException("Categoria não encontrada (ID=" + idCategoria + ").");
        }
        categoriaDAO.excluir(existente);
    }

    /**
     * Retorna uma instância gerenciada de Categoria, buscando por descrição exata.
     * Útil para, ao criar uma nova transação, associar a categoria correta.
     *
     * @param descricao descrição exata.
     * @return Categoria encontrada, ou null se não existir.
     */
    public Categoria getInstanciaCategoria(String descricao) {
        return categoriaDAO.buscarPorDescricao(descricao.trim());
    }

    /**
     * Busca e devolve uma Categoria por ID.
     *
     * @param id ID da categoria.
     * @return Categoria gerenciada ou null se não existir.
     */
    public Categoria buscarPorId(Long id) {
        return categoriaDAO.buscarPorId(id);
    }
}
