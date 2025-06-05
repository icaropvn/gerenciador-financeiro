package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import model.entity.Categoria;
import model.entity.Transacao;
import model.entity.Usuario;
import model.service.GerenciadorCategorias;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;
import model.util.DebugDatabasePrinter;
import view.TelaEditarTransacao;
import view.TelaPrincipal;

public class EditarTransacaoController {
	private TelaEditarTransacao view;
	private GerenciadorUsuario gerenciadorUsuario;
	private GerenciadorFinanceiro gerenciadorFinanceiro;
	private GerenciadorCategorias gerenciadorCategorias;
	private TelaPrincipal telaPrincipal;
	
	public EditarTransacaoController(TelaEditarTransacao view, GerenciadorUsuario gerenciadorUsuario, GerenciadorFinanceiro gerenciadorFinanceiro, GerenciadorCategorias gerenciadorCategorias, TelaPrincipal telaPrincipal) {
		this.view = view;
		this.gerenciadorUsuario = gerenciadorUsuario;
		this.gerenciadorFinanceiro = gerenciadorFinanceiro;
		this.gerenciadorCategorias = gerenciadorCategorias;
		this.telaPrincipal = telaPrincipal;
		
		initControllers();
	}
	
	public void initControllers() {
		view.getBotaoRemover().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removerTransacao();
			}
		});
		
		view.getBotaoEditar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clickBotaoEditar(e);
			}
		});
		
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fecharJanela();
			}
		});
	}
	
	public void removerTransacao() {
		int confirmacaoRemocao = JOptionPane.showConfirmDialog(view, "Deseja realmente remover essa transação?", "Confirmar remoção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		boolean removerFlag = false;
		
		if(confirmacaoRemocao == JOptionPane.YES_OPTION)
			removerFlag = true;
		
		if(!removerFlag)
			return;
		
		Transacao transacaoRemover = view.getTransacaoEmEdicao();
		long idTransacao = transacaoRemover.getId();
		Usuario usuarioAtual = gerenciadorUsuario.getUsuarioAtual();
		
		try {
	        // CHAMADA NO SERVIÇO: ele já busca NOVAMENTE do BD
	        // (não passe a instância “detached”, apenas o ID).
	        gerenciadorFinanceiro.excluirTransacao(usuarioAtual.getId(), idTransacao);

	        // Depois de excluir, recarregue a lista de transações na tela principal:
	        telaPrincipal.removerTransacaoTabela(transacaoRemover);

	        JOptionPane.showMessageDialog(view, "Transação removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	        view.dispose();

	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(view, "Erro ao remover transação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
	    }
		
		DebugDatabasePrinter.imprimirTodasTabelas();
	}
	
	public void clickBotaoEditar(ActionEvent e) {
		JButton botaoEditar = (JButton)e.getSource();
		
		if(botaoEditar.getText().equals("Editar"))
			editarTransacao();
		else
			salvarEdicaoTransacao();
	}
	
	public void editarTransacao() {
		view.desbloquearCampos();
		view.setModoEdicao(true);
		view.setTextoBotaoEditar("Salvar");
	}
	
	public void salvarEdicaoTransacao() {
		String classificacaoSelecionada = (String)view.getSelectClassificacao().getSelectedItem();
		String categoriaSelecionada = (String)view.getSelectCategoria().getSelectedItem();
		String conteudoData = ((JSpinner.DateEditor)view.getDataInput().getEditor()).getTextField().getText().trim();
		String conteudoValor = view.getValorInput().getText().trim();
		String conteudoDescricao = view.getDescricaoInput().getText().trim();
		
		// validar campos preenchidos
		if(conteudoData.isEmpty() || conteudoValor.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Preencha todos os campos para salvar a transação.", "Erro ao salvar transação", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// validar classificacao
		if(classificacaoSelecionada.equals("Classificação")) {
			JOptionPane.showMessageDialog(view, "Defina a classificação (receita ou despesa) da transação para salvar.", "Erro ao salvar transação", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// validar categoria
		if(categoriaSelecionada.equals("Categoria")) {
			JOptionPane.showMessageDialog(view, "Defina a categoria da transação para salvar.", "Erro ao salvar transação", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// validar existência da categoria
		Categoria novaCategoria = gerenciadorCategorias.getInstanciaCategoria(categoriaSelecionada);
        if (novaCategoria == null) {
            JOptionPane.showMessageDialog(view, "Categoria não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
		
		// validar valor numérico
		double valorTransacaoDouble;
		try {
			valorTransacaoDouble = Double.parseDouble(conteudoValor.replace(",", "."));
			if (valorTransacaoDouble <= 0) {
				JOptionPane.showMessageDialog(view, "O valor deve ser maior que zero.", "Erro ao salvar transação", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Valor inválido. Use apenas números.", "Erro ao salvar transação", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			Transacao transacao = view.getTransacaoEmEdicao();
			Usuario usuarioAtual = gerenciadorUsuario.getUsuarioAtual();
			
			// Guarda os valores antigos para ajuste do saldo
			String classificacaoAntiga = transacao.getClassificacao();
			double valorAntigo = transacao.getValor();
			
			// Atualiza os campos da transação
			Categoria categoriaTransacao = gerenciadorCategorias.getInstanciaCategoria(categoriaSelecionada);
			Date dataDate = (Date)view.getDataInput().getValue();
			LocalDate dataTransacao = dataDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			transacao.setData(dataTransacao);
			transacao.setValor(valorTransacaoDouble);
			transacao.setDescricao(conteudoDescricao);
			transacao.setCategoria(categoriaTransacao);
			transacao.setClassificacao(classificacaoSelecionada);
			
			// Persiste a transação atualizada no banco
			gerenciadorFinanceiro.atualizarTransacao(transacao);
			
			// Calcula o ajuste do saldo baseado nas mudanças
			double ajusteSaldo = calcularAjusteSaldo(classificacaoAntiga, valorAntigo, 
													classificacaoSelecionada, valorTransacaoDouble);
			
			// Aplica o ajuste ao saldo do usuário
			if (ajusteSaldo != 0) {
				if (ajusteSaldo > 0) {
					gerenciadorFinanceiro.adicionarSaldo(usuarioAtual, ajusteSaldo);
				} else {
					gerenciadorFinanceiro.retirarSaldo(usuarioAtual, Math.abs(ajusteSaldo));
				}
			}
			
			// Atualiza a tabela na tela principal
			telaPrincipal.atualizarTransacaoTabela(transacao);
			
			// Atualiza o saldo visualmente
			DecimalFormat formatadorSaldo = new DecimalFormat("#,##0.00");
			String novoSaldo = formatadorSaldo.format(usuarioAtual.getSaldo());
			telaPrincipal.atualizarSaldo(novoSaldo);
			
			view.bloquearCampos();
			view.setModoEdicao(false);
			view.setTextoBotaoEditar("Editar");
			view.dispose();
			
			JOptionPane.showMessageDialog(view, "Transação atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			
			model.util.DebugDatabasePrinter.imprimirTodasTabelas();
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(view, "Erro ao salvar transação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Calcula o ajuste necessário no saldo baseado nas mudanças na transação
	 */
	private double calcularAjusteSaldo(String classificacaoAntiga, double valorAntigo, 
									  String classificacaoNova, double valorNovo) {
		double ajuste = 0;
		
		// Primeiro, desfaz o efeito da transação antiga
		if ("Receita".equalsIgnoreCase(classificacaoAntiga)) {
			ajuste -= valorAntigo; // Remove a receita antiga
		} else {
			ajuste += valorAntigo; // Remove a despesa antiga (adiciona de volta ao saldo)
		}
		
		// Depois, aplica o efeito da transação nova
		if ("Receita".equalsIgnoreCase(classificacaoNova)) {
			ajuste += valorNovo; // Adiciona a nova receita
		} else {
			ajuste -= valorNovo; // Adiciona a nova despesa (subtrai do saldo)
		}
		
		return ajuste;
	}
	
	public void fecharJanela() {
		if(!view.getModoEdicao())
			return;
		
		view.bloquearCampos();
		view.setTextoBotaoEditar("Editar");
	}
}