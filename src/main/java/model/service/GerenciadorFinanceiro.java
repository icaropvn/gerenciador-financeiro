package model.service;

import java.util.List;

import model.entity.Categoria;
import model.entity.Transacao;
import model.entity.Usuario;
import model.dao.CategoriaDAO;
import model.dao.TransacaoDAO;
import model.dao.UsuarioDAO;

public class GerenciadorFinanceiro {

    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public void adicionarTransacao(Transacao t) {
        transacaoDAO.salvar(t);
    }
    
    public Usuario excluirTransacao(Long usuarioId, Long transacaoId) {
        Transacao t = transacaoDAO.buscarPorId(transacaoId);
        if (t == null || !t.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Transação não encontrada para este usuário.");
        }

        Usuario u = t.getUsuario();
        if ("Receita".equalsIgnoreCase(t.getClassificacao())) {
            u.setSaldo(u.getSaldo() - t.getValor());
        } else {
            u.setSaldo(u.getSaldo() + t.getValor());
        }
        
        usuarioDAO.atualizar(u);
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

    public double calcularTotalDespesas(List<Transacao> lista) {
        return lista.stream()
                    .filter(t -> "Despesa".equalsIgnoreCase(t.getClassificacao()))
                    .mapToDouble(Transacao::getValor)
                    .sum();
    }
}
