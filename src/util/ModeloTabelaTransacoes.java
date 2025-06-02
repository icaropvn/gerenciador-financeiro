package util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Transacao;

public class ModeloTabelaTransacoes extends AbstractTableModel {
	private final String[] colunas = {"Data", "Valor", "Descrição", "Categoria", "Classificação"};
	private final List<Transacao> transacoes;
	
	public ModeloTabelaTransacoes(List<Transacao> transacoes) {
		this.transacoes = transacoes != null ? transacoes : new ArrayList<>();
	}
	
	@Override
    public int getRowCount() {
        return transacoes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }
    
    @Override
    public String getColumnName(int coluna) {
        return colunas[coluna];
    }

    @Override
    public Object getValueAt(int indiceLinha, int indiceColuna) {
        Transacao t = transacoes.get(indiceLinha);
        
        switch (indiceColuna) {
            case 0: return t.getData();
            case 1: return t.getValor();
            case 2: return t.getDescricao();
            case 3: return t.getCategoria();
            case 4: return t.getClassificacao();
            default: throw new IllegalArgumentException("Coluna inválida");
        }
    }
    
    @Override
    public boolean isCellEditable(int linha, int coluna) {
        return false;
    }
    
    public List<Transacao> getTransacoes() {
    	return transacoes;
    }
    
    public Transacao getTransacaoAt(int indiceLinha) {
        return transacoes.get(indiceLinha);
    }
    
    public void adicionarTransacao(Transacao novaTransacao) {
    	transacoes.add(0, novaTransacao);
        fireTableRowsInserted(0, 0);
    }

    public void removerTransacao(int indiceLinha) {
        transacoes.remove(indiceLinha);
        fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    public void atualizarTransacao(int indiceLinha, Transacao transacaoEditada) {
        transacoes.set(indiceLinha, transacaoEditada);
        fireTableRowsUpdated(indiceLinha, indiceLinha);
    }
}
