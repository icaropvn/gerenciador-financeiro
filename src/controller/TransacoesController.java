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
	
//	TESTE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//	public void validarAdicaoCategoria() {
//		// Cria a lista de transações de teste
//	    ArrayList<Transacao> transacoesTeste = new ArrayList<>();
//
//	    // Categoria: Alimentação
//	    transacoesTeste.add(0, new Transacao("Despesa", 50.0, gerenciadorCategorias.getInstanciaCategoria("Alimentação"), LocalDate.of(2025, 1, 10), "Café da manhã"));
//	    transacoesTeste.add(0, new Transacao("Despesa", 80.0, gerenciadorCategorias.getInstanciaCategoria("Alimentação"), LocalDate.of(2025, 1, 15), "Almoço"));
//	    transacoesTeste.add(0, new Transacao("Despesa", 120.0, gerenciadorCategorias.getInstanciaCategoria("Alimentação"), LocalDate.of(2025, 1, 20), "Jantar"));
//
//	    // Categoria: Transporte
//	    transacoesTeste.add(0, new Transacao("Despesa", 30.0, gerenciadorCategorias.getInstanciaCategoria("Transporte"), LocalDate.of(2025, 2, 5), "Passagem de ônibus"));
//	    transacoesTeste.add(0, new Transacao("Despesa", 40.0, gerenciadorCategorias.getInstanciaCategoria("Transporte"), LocalDate.of(2025, 2, 12), "Táxi"));
//	    transacoesTeste.add(0, new Transacao("Despesa", 100.0, gerenciadorCategorias.getInstanciaCategoria("Transporte"), LocalDate.of(2025, 2, 20), "Combustível"));
//
//	    // Categoria: Lazer
//	    transacoesTeste.add(0, new Transacao("Despesa", 150.0, gerenciadorCategorias.getInstanciaCategoria("Lazer"), LocalDate.of(2025, 3, 1), "Cinema e jantar"));
//	    transacoesTeste.add(0, new Transacao("Despesa", 200.0, gerenciadorCategorias.getInstanciaCategoria("Lazer"), LocalDate.of(2025, 3, 8), "Show de música"));
//	    transacoesTeste.add(0, new Transacao("Despesa", 50.0, gerenciadorCategorias.getInstanciaCategoria("Lazer"), LocalDate.of(2025, 3, 15), "Passeio no parque"));
//
//	    // Categoria: Educação
//	    transacoesTeste.add(0, new Transacao("Despesa", 500.0, gerenciadorCategorias.getInstanciaCategoria("Educação"), LocalDate.of(2025, 4, 3), "Curso de idiomas"));
//	    transacoesTeste.add(0, new Transacao("Despesa", 300.0, gerenciadorCategorias.getInstanciaCategoria("Educação"), LocalDate.of(2025, 4, 10), "Material escolar"));
//	    transacoesTeste.add(0, new Transacao("Despesa", 250.0, gerenciadorCategorias.getInstanciaCategoria("Educação"), LocalDate.of(2025, 4, 20), "Workshop"));
//
//	    // Categoria: Salário (Receita)
//	    transacoesTeste.add(0, new Transacao("Receita", 2500.0, gerenciadorCategorias.getInstanciaCategoria("Salário"), LocalDate.of(2025, 1, 31), "Salário mensal"));
//	    transacoesTeste.add(0, new Transacao("Receita", 2500.0, gerenciadorCategorias.getInstanciaCategoria("Salário"), LocalDate.of(2025, 2, 28), "Salário mensal"));
//	    transacoesTeste.add(0, new Transacao("Receita", 2500.0, gerenciadorCategorias.getInstanciaCategoria("Salário"), LocalDate.of(2025, 3, 31), "Salário mensal"));
//
//	    // Recupera o usuário atual
//	    Usuario usuario = gerenciadorUsuario.getUsuarioAtual();
//
//	    // Itera sobre as transações de teste e as adiciona
//	    for (Transacao transacao : transacoesTeste) {
//	        // Adiciona a transação ao histórico do usuário
//	        usuario.adicionarTransacao(transacao);
//	        // Atualiza a tabela na tela principal
//	        telaPrincipal.adicionarTransacaoTabela(transacao);
//	        
//	        // Atualiza o saldo conforme a classificação
//	        if (transacao.getClassificacao().equalsIgnoreCase("Despesa")) {
//	            gerenciadorFinanceiro.retirarSaldo(usuario, transacao.getValor());
//	        } else {
//	            gerenciadorFinanceiro.adicionarSaldo(usuario, transacao.getValor());
//	        }
//	    }
//	    
//	    // Atualiza o saldo exibido na tela principal
//	    DecimalFormat formatadorSaldo = new DecimalFormat("#,##0.00");
//	    String saldoAtualizado = formatadorSaldo.format(usuario.getSaldo());
//	    telaPrincipal.atualizarSaldo(saldoAtualizado);
//
//	    // Fecha a janela de entrada (caso seja uma janela separada)
//	    view.dispose();
//	}
}
