package model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	
	@Column(nullable = false, length = 30, unique = true)
	private String descricao;
	
	public Categoria(){}
	
	public Categoria( String descricao) {
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
