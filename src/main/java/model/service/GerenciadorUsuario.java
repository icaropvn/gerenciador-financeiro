package model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.UsuarioDAO;
import model.entity.Transacao;
import model.entity.Usuario;
import util.CriptografarSenha;

public class GerenciadorUsuario {
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private Usuario usuarioAtual;
	
	public Usuario adicionarUsuario(String nome, String senha, double saldoInicial) {
        Usuario u = new Usuario(nome, senha, saldoInicial);
        return usuarioDAO.salvarOuAtualizar(u);
    }
	
	public Usuario buscarPorNome(String nome) {
        return usuarioDAO.buscarPorNome(nome);
    }
	
	public Usuario validarLogin(String nome, String senha) {
        return usuarioDAO.buscarPorNomeESenha(nome, senha);
    }
	
	public List<Usuario> listarTodosUsuarios() {
        return usuarioDAO.buscarTodos();
    }

    public void adicionarTransacaoAoUsuario(Long usuarioId, Transacao novaTransacao) {
        Usuario u = usuarioDAO.buscarPorId(usuarioId);
        if (u == null) {
            throw new IllegalArgumentException("Usuário não encontrado: id=" + usuarioId);
        }

        u.adicionarTransacao(novaTransacao);

        if ("Receita".equalsIgnoreCase(novaTransacao.getClassificacao())) {
            u.setSaldo(u.getSaldo() + novaTransacao.getValor());
        } else {
            u.setSaldo(u.getSaldo() - novaTransacao.getValor());
        }

        usuarioDAO.salvarOuAtualizar(u);
    }

    public void removerTransacaoDoUsuario(Long usuarioId, Long transacaoId) {
        Usuario u = usuarioDAO.buscarPorId(usuarioId);
        if (u == null) {
            throw new IllegalArgumentException("Usuário não encontrado: id=" + usuarioId);
        }

        Transacao tRemover = null;
        for (Transacao t : u.getHistoricoTransacoes()) {
            if (t.getId() == transacaoId) {
                tRemover = t;
                break;
            }
        }
        if (tRemover == null) {
            throw new IllegalArgumentException("Transação não encontrada para exclusão: id=" + transacaoId);
        }

        if ("Receita".equalsIgnoreCase(tRemover.getClassificacao())) {
            u.setSaldo(u.getSaldo() - tRemover.getValor());
        } else {
            u.setSaldo(u.getSaldo() + tRemover.getValor());
        }

        u.removerTransacao(tRemover);
        usuarioDAO.salvarOuAtualizar(u);
    }


    public boolean existeTransacaoComCategoria(Long usuarioId, String descricaoCategoria) {
        return new model.dao.TransacaoDAO().existePorUsuarioECategoria(usuarioId, descricaoCategoria);
    }

    public void excluirUsuario(Long usuarioId) {
        Usuario u = usuarioDAO.buscarPorId(usuarioId);
        if (u != null) {
            usuarioDAO.excluir(u);
        }
    }
    
    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }

    public void setUsuarioAtual(Usuario usuario) {
        this.usuarioAtual = usuario;
    }
}
