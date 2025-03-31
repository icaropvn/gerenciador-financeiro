package model;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import util.CriptografarSenha;

public class GerenciadorUsuario {
	private Map<String, Usuario> usuariosCadastrados;
	private Usuario usuarioAtual;
	
	public GerenciadorUsuario() {
		usuariosCadastrados = new HashMap<>();
		this.usuarioAtual = null;
	}
	
	public void adicionarUsuario(Usuario usuario) {
		usuariosCadastrados.put(usuario.getNome(), usuario);
	}
	
	public boolean validarNomeUsuario(String nome) {
		return usuariosCadastrados.containsKey(nome);
	}
	
	public boolean validarSenhaUsuario(String nome, String senha) {
		String senhaCriptografada = CriptografarSenha.criptografarSenha(senha);
		Usuario usuario = usuariosCadastrados.get(nome);
		return usuario != null ? usuario.getSenha().equals(senhaCriptografada) : false;
	}
	
	// getters e setters
	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}
	
	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}
	
	public Usuario getUsuario(String nome) {
		return usuariosCadastrados.get(nome);
	}
}
