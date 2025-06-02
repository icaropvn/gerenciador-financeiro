package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;

import model.Categoria;
import model.GerenciadorCategorias;
import model.GerenciadorFinanceiro;
import model.GerenciadorUsuario;
import model.Transacao;
import model.Usuario;
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
		Usuario usuarioAtual = gerenciadorUsuario.getUsuarioAtual();
		
		// remove a transação da lista do usuário e da tabela na tela principal
		usuarioAtual.removerTransacao(transacaoRemover);
		telaPrincipal.removerTransacaoTabela(transacaoRemover);
		
		// atualiza o saldo do usuário de acordo com o tipo de transação
		if(transacaoRemover.getClassificacao().equals("Receita"))
			gerenciadorFinanceiro.retirarSaldo(usuarioAtual, transacaoRemover.getValor());
		else
			gerenciadorFinanceiro.adicionarSaldo(usuarioAtual, transacaoRemover.getValor());
		
		// atualiza saldo visualmente na tela principal
		DecimalFormat formatadorSaldo = new DecimalFormat("#,##0.00");
		String novoSaldo = formatadorSaldo.format(usuarioAtual.getSaldo());
		telaPrincipal.atualizarSaldo(novoSaldo);
		
		view.dispose();
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
		
		view.bloquearCampos();
		
		Transacao transacao = view.getTransacaoEmEdicao();
		Transacao transacaoAntiga = Transacao.copiarTransacao(transacao);
		Usuario usuarioAtual = gerenciadorUsuario.getUsuarioAtual();
		
		double valorTransacaoDouble = Double.parseDouble(conteudoValor.replace(",", "."));
		Categoria categoriaTransacao = gerenciadorCategorias.getInstanciaCategoria(categoriaSelecionada);
		Date dataDate = (Date)view.getDataInput().getValue();
		LocalDate dataTransacao = dataDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		transacao.setData(dataTransacao);
		transacao.setValor(valorTransacaoDouble);
		transacao.setDescricao(conteudoDescricao);
		transacao.setCategoria(categoriaTransacao);
		transacao.setClassificacao(classificacaoSelecionada);
		
		telaPrincipal.atualizarTransacaoTabela(transacao);
		
		if(transacaoAntiga.getClassificacao() != transacao.getClassificacao()) {
			if(transacao.getClassificacao().equals("Receita"))
				gerenciadorFinanceiro.adicionarSaldo(usuarioAtual, 2*transacaoAntiga.getValor());
			else
				gerenciadorFinanceiro.retirarSaldo(usuarioAtual, 2*transacaoAntiga.getValor());
		}
		
		double diferencaValor = transacaoAntiga.getValor() - transacao.getValor();
		
		if(transacao.getClassificacao().equals("Receita"))
			gerenciadorFinanceiro.retirarSaldo(usuarioAtual, diferencaValor);
		else
			gerenciadorFinanceiro.adicionarSaldo(usuarioAtual, diferencaValor);
		
		DecimalFormat formatadorSaldo = new DecimalFormat("#,##0.00");
		String novoSaldo = formatadorSaldo.format(usuarioAtual.getSaldo());
		telaPrincipal.atualizarSaldo(novoSaldo);
		
		view.setModoEdicao(false);
		view.setTextoBotaoEditar("Editar");
		view.dispose();
	}
	
	public void fecharJanela() {
		if(!view.getModoEdicao())
			return;
		
		view.bloquearCampos();
		view.setTextoBotaoEditar("Editar");
	}
}
