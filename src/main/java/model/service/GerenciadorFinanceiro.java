package model.service;

import java.util.List;

import model.entity.Categoria;
import model.entity.Transacao;
import model.entity.Usuario;
import model.dao.CategoriaDAO;
import model.dao.TransacaoDAO;
import model.dao.UsuarioDAO;

/**
 * Serviço responsável pelas operações financeiras (transações e saldo).
 */
public class GerenciadorFinanceiro {

    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public void adicionarTransacao(Transacao t) {
        // Primeiramente, persiste a transação
        transacaoDAO.salvar(t);
        // (Opcional: se quiser, podemos recarregar a entidade t com merge, mas não é obrigatório
        //  pois t já foi gerenciado pela sessão do DAO.)
    }
    
    public Usuario excluirTransacao(Long usuarioId, Long transacaoId) {
    	// 1) Busca a transação diretamente do BD, somente para obter valor e classificacao
        //    (não vamos chamar delete(aqui), mas precisamos desses dados para ajustar saldo).
        Transacao t = transacaoDAO.buscarPorId(transacaoId);
        if (t == null || !t.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Transação não encontrada para este usuário.");
        }

        // 2) Ajusta o saldo do usuário invertendo o valor da transação:
        Usuario u = t.getUsuario();
        if ("Receita".equalsIgnoreCase(t.getClassificacao())) {
            u.setSaldo(u.getSaldo() - t.getValor());
        } else { // “Despesa”
            u.setSaldo(u.getSaldo() + t.getValor());
        }
        usuarioDAO.atualizar(u);

        // 3) Executa DELETE direto por HQL, sem tentar remover a entidade em cascata
        transacaoDAO.excluirPorId(transacaoId);
        
        return u;
    }

    public List<Transacao> buscarTransacoesPorUsuario(Long usuarioId) {
        return transacaoDAO.buscarPorUsuario(usuarioId);
    }

    public void adicionarSaldo(Usuario usuario, double valor) {
        double novoSaldo = usuario.getSaldo() + valor;
        usuario.setSaldo(novoSaldo);
        usuarioDAO.atualizar(usuario);
    }

    public void retirarSaldo(Usuario usuario, double valor) {
        double novoSaldo = usuario.getSaldo() - valor;
        usuario.setSaldo(novoSaldo);
        usuarioDAO.atualizar(usuario);
    }
    
    
    public void atualizarTransacao(Transacao t) {
        new TransacaoDAO().atualizar(t);
    }
    
    public double calcularTotalReceitas(List<Transacao> lista) {
        return lista.stream()
                    .filter(t -> "Receita".equalsIgnoreCase(t.getClassificacao()))
                    .mapToDouble(Transacao::getValor)
                    .sum();
    }

    /**
     * Dado um subconjunto de Transações, calcula o total de DESPESAS
     * (soma de valores onde classificacao == "Despesa").
     */
    public double calcularTotalDespesas(List<Transacao> lista) {
        return lista.stream()
                    .filter(t -> "Despesa".equalsIgnoreCase(t.getClassificacao()))
                    .mapToDouble(Transacao::getValor)
                    .sum();
    }
}
