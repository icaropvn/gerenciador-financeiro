package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;

public class GerenciadorCategorias {
	private List<Categoria> listaCategorias;
	private List<Categoria> listCategoriasTemporaria;
	private int proximoCodigoCategoria = 1;
	
	public enum TipoVerificacao {
		EDICAO, ADICAO;
	}
	
	public GerenciadorCategorias() {
		listaCategorias = new ArrayList<>();
		listCategoriasTemporaria = new ArrayList<>();
	}
	
	public void adicionarCategoria(String descCategoria) {
		int novoCodigo = getProximoCodigo();
		
		Categoria novaCategoria = new Categoria(novoCodigo, descCategoria);
		listaCategorias.add(novaCategoria);
	}
	
	public void removerCategoria(String descCategoriaRemover) {
		Iterator<Categoria> iterador = listaCategorias.iterator();
		
		while(iterador.hasNext()) {
			Categoria categoria = iterador.next();
			
			if(categoria.getDescricao().equals(descCategoriaRemover)) {
				iterador.remove();
				break;
			}
		}
	}
	
	public void editarCategoria(String novoNomeCategoria, String descricaoOriginal) {		
		Iterator<Categoria> iterador = listaCategorias.iterator();
		
		while(iterador.hasNext()) {
			Categoria categoria = iterador.next();
			
			if(categoria.getDescricao().equals(descricaoOriginal)) {
				categoria.setDescricao(novoNomeCategoria);
				break;
			}
		}
	}
	
	public boolean isCategoriaDuplicada(String descCategoria, String descOriginal, TipoVerificacao tipoVerificacao) {
		for(Categoria categoria : listaCategorias) {
			if(tipoVerificacao == TipoVerificacao.EDICAO && categoria.getDescricao().equals(descOriginal))
				continue;
			
			if(categoria.getDescricao().equals(descCategoria))
				return true;
		}
		
		return false;
	}
	
	// getters e setters
	public List<Categoria> getListaCategorias() {
		return listaCategorias;
	}
	
	private int getProximoCodigo() {
		return proximoCodigoCategoria++;
	}
}
