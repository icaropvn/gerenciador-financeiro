package model.service;

import model.dao.TransacaoDAO;
import model.dao.UsuarioDAO;
import model.entity.Transacao;
import model.entity.Usuario;
import util.CriptografarSenha;

import java.util.List;

public class GerenciadorUsuario {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuarioAtual;

    public Usuario adicionarUsuario(String nome, String senha, double saldoInicial) {
        Usuario u = new Usuario(nome, senha, saldoInicial);
        usuarioDAO.salvar(u);
        return u;
    }

    public Usuario buscarPorNome(String nome) {
        return usuarioDAO.buscarPorNome(nome);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }

    public Usuario validarLogin(String nome, String senha) {
        String senhaCripto = CriptografarSenha.criptografarSenha(senha);
        return usuarioDAO.buscarPorNomeESenha(nome, senhaCripto);
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

    public boolean existeTransacaoComCategoria(Long usuarioId, String descricaoCategoria) {
        return new TransacaoDAO().existePorUsuarioECategoria(usuarioId, descricaoCategoria);
    }
}
