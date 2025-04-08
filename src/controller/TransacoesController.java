package controller;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.*;

import view.TelaAdicionarTransacao;
import view.TelaPrincipal;
import model.GerenciadorUsuario;
import model.GerenciadorCategorias;
import model.GerenciadorFinanceiro;
import model.Usuario;
import model.Transacao;
import model.Categoria;



import java.util.ArrayList;

public class TransacoesController {
	private TelaAdicionarTransacao view;
	private TelaPrincipal telaPrincipal;
	private GerenciadorUsuario gerenciadorUsuario;
	private GerenciadorCategorias gerenciadorCategorias;
	private GerenciadorFinanceiro gerenciadorFinanceiro;
	
	public TransacoesController(TelaAdicionarTransacao view, TelaPrincipal telaPrincipal, GerenciadorUsuario gerenciadorUsuario, GerenciadorCategorias gerenciadorCategorias, GerenciadorFinanceiro gerenciadorFinanceiro) {
		this.view = view;
		this.telaPrincipal = telaPrincipal;
		this.gerenciadorUsuario = gerenciadorUsuario;
		this.gerenciadorCategorias = gerenciadorCategorias;
		this.gerenciadorFinanceiro = gerenciadorFinanceiro;
		
		initControllers();
	}
	
	public void initControllers() {
		view.getBotaoAdicionar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validarAdicaoCategoria();
			}
		});
	}
	
	public void validarAdicaoCategoria() {
		String classificacaoSelecionada = (String)view.getSelectClassificacao().getSelectedItem();
		String categoriaSelecionada = (String)view.getSelectCategoria().getSelectedItem();
		String conteudoData = ((JSpinner.DateEditor)view.getDataInput().getEditor()).getTextField().getText().trim();
		String conteudoValor = view.getValorInput().getText().trim();
		String conteudoDescricao = view.getDescricaoInput().getText().trim();
		
		// validar campos preenchidos
		if(conteudoData.isEmpty() || conteudoValor.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Preencha todos os campos para adicionar a transação.", "Erro ao adicionar transação", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// validar classificacao
		if(classificacaoSelecionada.equals("Classificação")) {
			JOptionPane.showMessageDialog(view, "Defina a classificação (receita ou despesa) da transação para adicionar.", "Erro ao adicionar transação", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// validar categoria
		if(categoriaSelecionada.equals("Categoria")) {
			JOptionPane.showMessageDialog(view, "Defina a categoria da transação para adicionar.", "Erro ao adicionar transação", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// validar existência da categoria
		if(!gerenciadorCategorias.isThereCategoria(categoriaSelecionada)) {
			JOptionPane.showMessageDialog(view, "Algo deu errado e não conseguimos encontrar essa categoria cadastrada, tente fechar esta tela e tentar novamente.", "Erro ao adicionar transação", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Usuario usuario = gerenciadorUsuario.getUsuarioAtual();
		
		double valorTransacaoDouble = Double.parseDouble(conteudoValor.replace(",", "."));
		Categoria categoriaTransacao = gerenciadorCategorias.getInstanciaCategoria(categoriaSelecionada);
		
		Date dataDate = (Date)view.getDataInput().getValue();
		LocalDate dataTransacao = dataDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		Transacao novaTransacao = new Transacao(classificacaoSelecionada, valorTransacaoDouble, categoriaTransacao, dataTransacao, conteudoDescricao);
		usuario.adicionarTransacao(novaTransacao);
		telaPrincipal.adicionarTransacaoTabela(novaTransacao);
		
		if(classificacaoSelecionada.equals("Despesa"))
			gerenciadorFinanceiro.retirarSaldo(usuario, valorTransacaoDouble);
		else
			gerenciadorFinanceiro.adicionarSaldo(usuario, valorTransacaoDouble);
		
		DecimalFormat formatadorSaldo = new DecimalFormat("#,##0.00");
		String saldo = formatadorSaldo.format(usuario.getSaldo());
		telaPrincipal.atualizarSaldo(saldo);
		
		view.getSelectClassificacao().setSelectedItem("Classificação");
		view.getSelectCategoria().setSelectedItem("Categoria");
		view.getValorInput().setText("");
		view.getDescricaoInput().setText("");
		
		view.dispose();
	}
}
