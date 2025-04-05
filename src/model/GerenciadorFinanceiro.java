package model;

public class GerenciadorFinanceiro {
	public GerenciadorFinanceiro(){}
	
	public void retirarSaldo(Usuario usuario, double despesa) {
		usuario.setSaldo(usuario.getSaldo() - despesa);
	}
	
	public void adicionarSaldo(Usuario usuario, double receita) {
		usuario.setSaldo(usuario.getSaldo() + receita);
	}
}
