package controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import view.MainFrame;
import view.TelaPrincipal;
import view.TelaResumoFinanceiro;
import view.TelaEditarCategorias;
import view.TelaEditarTransacao;
import view.TelaAdicionarTransacao;
import model.entity.Transacao;
import model.entity.Usuario;
import model.service.GerenciadorCategorias;
import model.service.GerenciadorFinanceiro;
import model.service.GerenciadorUsuario;
import util.ModeloTabelaTransacoes;

public class PrincipalController {
	private MainFrame mainFrame;
	private TelaPrincipal view;
	private TelaResumoFinanceiro telaResumo;
	private TelaEditarCategorias telaCategorias;
	private TelaAdicionarTransacao telaTransacao;
	private TelaEditarTransacao telaEditarTransacao;
	private GerenciadorCategorias gerenciadorCategorias;
	private GerenciadorFinanceiro gerenciadorFinanceiro;
	private GerenciadorUsuario gerenciadorUsuario;
	private CategoriasController categoriasController;
	
	public PrincipalController(MainFrame mainFrame, TelaPrincipal telaPrincipal, TelaResumoFinanceiro telaResumo, TelaEditarCategorias telaCategorias, TelaAdicionarTransacao telaTransacao, TelaEditarTransacao telaEditarTransacao, GerenciadorCategorias gerenciadorCategorias, GerenciadorFinanceiro gerenciadorFinanceiro, GerenciadorUsuario gerenciadorUsuario, CategoriasController categoriasController) {
		this.mainFrame = mainFrame;
		this.view = telaPrincipal;
		this.telaResumo = telaResumo;
		this.telaCategorias = telaCategorias;
		this.telaTransacao = telaTransacao;
		this.telaEditarTransacao = telaEditarTransacao;
		this.gerenciadorCategorias = gerenciadorCategorias;
		this.gerenciadorFinanceiro = gerenciadorFinanceiro;
		this.gerenciadorUsuario = gerenciadorUsuario;
		this.categoriasController = categoriasController;
		
		initControllers();
	}
	
	public void initControllers() {
		carregarTabelaTransacoes();
		
		view.getBotaoSair().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmarSaida();
			}
		});
		
		view.getBotaoResumoFinanceiro().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarTelaResumoFinanceiro();
			}
		});
		
		view.getBotaoEditarCategorias().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarTelaEditarCategorias();
			}
		});
		
		view.getBotaoAdicionarTransacao().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				telaTransacao.atualizarListaCategorias(gerenciadorCategorias.listarTodasCategorias());
				telaTransacao.setVisible(true);
			}
		});
		
		view.getBotaoLimparFiltros().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				limparFiltros();
			}
		});
		
		view.getBotaoAplicarFiltros().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aplicarFiltros();
			}
		});
		
		view.getTabelaTransacoes().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				telaEditarTransacao.atualizarListaCategorias(gerenciadorCategorias.listarTodasCategorias());
				abrirTelaEditarTransacao(e);
			}
		});
	}
	
	private void carregarTabelaTransacoes() {
        Usuario u = gerenciadorUsuario.getUsuarioAtual();
        
        if (u == null) {
        	return;
        }

        List<Transacao> todas = gerenciadorFinanceiro.listarTransacoesPorUsuario(u.getId());

        view.substituirTabelaTransacoes(todas);

        double saldo = gerenciadorUsuario.getUsuarioAtual().getSaldo();
        view.setSaldo(formatarMoeda(saldo));
    }
	
	private String formatarMoeda(double valor) {
        DecimalFormat df = new DecimalFormat("R$ #,##0.00");
        return df.format(valor);
    }
	
	public void confirmarSaida() {
		int confirmarSaida = JOptionPane.showConfirmDialog(view, "Tem certeza que deseja sair da sua conta?", "Confirmação de saída", JOptionPane.YES_NO_OPTION);
		
		if(confirmarSaida == JOptionPane.YES_OPTION) {
			gerenciadorUsuario.setUsuarioAtual(null);				
			mainFrame.mostrarTela("login");
		}
	}
	
	public void mostrarTelaResumoFinanceiro() {
		telaResumo.inicializarSelectCategorias(gerenciadorCategorias.listarTodasCategorias());
		telaResumo.setVisible(true);
	}
	
	public void mostrarTelaEditarCategorias() {
		categoriasController.carregarCategoriasExistentes(gerenciadorCategorias.listarTodasCategorias());
		telaCategorias.setVisible(true);
	}
	
	public void limparFiltros() {
		view.getInputDataInicial().setText("");
        view.getInputDataFinal().setText("");
        view.getSelectClassificacao().setSelectedIndex(0);
        view.getSelectCategoria().setSelectedIndex(0);

        carregarTabelaTransacoes();
	}
	
	public void aplicarFiltros() {
		String conteudoDataInicial = view.getInputDataInicial().getText();
		String conteudoDataFinal = view.getInputDataFinal().getText();
		String conteudoSelectClassificacao = (String)view.getSelectClassificacao().getSelectedItem();
		String conteudoSelectCategoria = (String)view.getSelectCategoria().getSelectedItem();
		
		if(conteudoDataInicial.isEmpty() &&
		   conteudoDataFinal.isEmpty() &&
		   conteudoSelectClassificacao.equals("Classificação") &&
		   conteudoSelectCategoria.equals("Categoria")) {
			JOptionPane.showMessageDialog(view, "Forneça algum parâmetro para filtrar as transações.", "Nenhum filtro selecionado", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(!conteudoSelectCategoria.equals("Categoria") && !gerenciadorCategorias.isThereCategoria(conteudoSelectCategoria)) {
			JOptionPane.showMessageDialog(view, "Algo deu errado ao filtrar por essa categoria.", "Erro ao filtrar por categoria", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		LocalDate dataIni = null, dataFim = null;
		DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
        try {
            if (!conteudoDataInicial.isEmpty()) {
                dataIni = LocalDate.parse(conteudoDataInicial, dtFormatter);
            }
            if (!conteudoDataFinal.isEmpty()) {
                dataFim = LocalDate.parse(conteudoDataFinal, dtFormatter);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Formato de data inválido. Use dd/MM/yyyy", "Erro de Filtro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dataIni != null && dataFim != null && dataFim.isBefore(dataIni)) {
        	JOptionPane.showMessageDialog(view, "Data final não pode ser anterior à data inicial.", "Erro de Filtro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String categoriaFiltro = null;
        if (!"Categoria".equals(conteudoSelectClassificacao)) {
            categoriaFiltro = conteudoSelectClassificacao;
        }

        String classificacaoFiltro = null;
        if (!"Classificação".equals(conteudoSelectCategoria)) {
            classificacaoFiltro = conteudoSelectCategoria;
        }
        
        Usuario u = gerenciadorUsuario.getUsuarioAtual();
        List<Transacao> filtradas = gerenciadorFinanceiro.buscarTransacoesFiltradas(
                u.getId(),
                dataIni, dataFim,
                classificacaoFiltro,
                categoriaFiltro
        );

        view.substituirTabelaTransacoes(filtradas);
        
        double saldo = gerenciadorUsuario.getUsuarioAtual().getSaldo();
        view.setSaldo(formatarMoeda(saldo));
        
        double receitasIntervalo = 0.0;
        double despesasIntervalo = 0.0;

        for (Transacao t : filtradas) {
            if ("Receita".equalsIgnoreCase(t.getClassificacao())) {
                receitasIntervalo += t.getValor();
            } else {
                despesasIntervalo += t.getValor();
            }
        }
	    
	    view.setDespesasIntervalo("R$ " + formatarMoeda(despesasIntervalo));
	    view.setReceitasIntervalo("R$ " + formatarMoeda(receitasIntervalo));
	}
	
	public void abrirTelaEditarTransacao(MouseEvent e) {
		JTable tabela = view.getTabelaTransacoes();
		
		if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
			e.consume();
			
			int linhaSelecionada = tabela.getSelectedRow();
			
			if(linhaSelecionada < 0) {
				return;
			}
			
			int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);
			ModeloTabelaTransacoes model = (ModeloTabelaTransacoes) tabela.getModel();
		    Transacao transacao = model.getTransacaoAt(linhaModelo);
			
			telaEditarTransacao.inicializarInputs(transacao);
			telaEditarTransacao.setTransacaoEmEdicao(transacao);
			telaEditarTransacao.setVisible(true);
		}
	}
}
