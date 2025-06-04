package model.service;

import java.time.LocalDate;
import java.util.List;

import model.entity.Transacao;
import model.entity.Usuario;
import model.entity.Categoria;
import model.dao.TransacaoDAO;
import model.dao.UsuarioDAO;
import model.dao.CategoriaDAO;

public class GerenciadorFinanceiro {
    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public void registrarTransacao(Long usuarioId, String classificacao, double valor, Integer categoriaCodigo, LocalDate data, String descricao) {
        Usuario u = usuarioDAO.buscarPorId(usuarioId);
        if (u == null) {
            throw new IllegalArgumentException("Usuário não encontrado: id=" + usuarioId);
        }

        Categoria c = categoriaDAO.buscarPorId(categoriaCodigo);
        if (c == null) {
            throw new IllegalArgumentException("Categoria não encontrada: código=" + categoriaCodigo);
        }

        Transacao t = new Transacao(classificacao, valor, c, data, descricao, u);

        if ("Receita".equalsIgnoreCase(classificacao)) {
            u.setSaldo(u.getSaldo() + valor);
        } else {
            u.setSaldo(u.getSaldo() - valor);
        }

        u.adicionarTransacao(t);
        usuarioDAO.salvarOuAtualizar(u);
    }

    public List<Transacao> listarTransacoesPorUsuario(Long usuarioId) {
        return transacaoDAO.buscarPorUsuarioEPeriodo(usuarioId, LocalDate.of(1900, 1, 1), LocalDate.now().plusDays(1));
    }
    
    public List<Transacao> listarTransacoesPorPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim) {
        return transacaoDAO.buscarPorUsuarioEPeriodo(usuarioId, inicio, fim);
    }
    
    public void excluirTransacao(Long usuarioId, Long transacaoId) {
        Usuario u = usuarioDAO.buscarPorId(usuarioId);
        if (u == null) {
            throw new IllegalArgumentException("Usuário não encontrado: id=" + usuarioId);
        }

        Transacao tRemover = null;
        for (Transacao t : u.getHistoricoTransacoes()) {
            if (t.getId() == transacaoId) {
                tRemover = t;
                break;
            }
        }
        
        if (tRemover == null) {
            throw new IllegalArgumentException("Transação não encontrada: id=" + transacaoId);
        }

        if ("Receita".equalsIgnoreCase(tRemover.getClassificacao())) {
            u.setSaldo(u.getSaldo() - tRemover.getValor());
        } else {
            u.setSaldo(u.getSaldo() + tRemover.getValor());
        }

        u.removerTransacao(tRemover);
        usuarioDAO.salvarOuAtualizar(u);
    }
    
    public List<Transacao> buscarTransacoesFiltradas(
            Long usuarioId,
            LocalDate dataIni,
            LocalDate dataFim,
            String classificacaoFiltro,
            String categoriaFiltro) {

        List<Transacao> lista = listarTransacoesPorUsuario(usuarioId);

        return lista.stream()
                .filter(t -> {
                    boolean ok = true;
                    if (dataIni != null && t.getData().isBefore(dataIni)) {
                        ok = false;
                    }
                    if (ok && dataFim != null && t.getData().isAfter(dataFim)) {
                        ok = false;
                    }
                    if (ok && classificacaoFiltro != null
                            && !t.getClassificacao().equalsIgnoreCase(classificacaoFiltro)) {
                        ok = false;
                    }
                    if (ok && categoriaFiltro != null
                            && !t.getCategoria().getDescricao().equalsIgnoreCase(categoriaFiltro)) {
                        ok = false;
                    }
                    return ok;
                })
                .toList();
    }
    
    public double somaReceitasFiltradas(Long usuarioId,
            LocalDate dataIni,
            LocalDate dataFim,
            String classificacaoFiltro,
            String categoriaFiltro) {
		// Busca a lista filtrada (via método already existente)
		List<Transacao> lista = buscarTransacoesFiltradas(usuarioId, dataIni, dataFim,
		                              classificacaoFiltro, categoriaFiltro);
		// Soma apenas as classificações "Receita"
		return lista.stream()
		.filter(t -> "Receita".equalsIgnoreCase(t.getClassificacao()))
		.mapToDouble(Transacao::getValor)
		.sum();
		}
		
		
	public double somaDespesasFiltradas(Long usuarioId,
		           LocalDate dataIni,
		            LocalDate dataFim,
		            String classificacaoFiltro,
		            String categoriaFiltro) {
		List<Transacao> lista = buscarTransacoesFiltradas(usuarioId, dataIni, dataFim,
		                              classificacaoFiltro, categoriaFiltro);
		return lista.stream()
		.filter(t -> !"Receita".equalsIgnoreCase(t.getClassificacao()))
		.mapToDouble(Transacao::getValor)
		.sum();
	}
	
	public void retirarSaldo(Usuario usuario, double valor) {
        // Ajusta o saldo em memória
        double novoSaldo = usuario.getSaldo() - valor;
        usuario.setSaldo(novoSaldo);

        // Persiste a alteração no banco (merge atualiza a entidade)
        usuarioDAO.salvarOuAtualizar(usuario);
    }

    public void adicionarSaldo(Usuario usuario, double valor) {
        // Ajusta o saldo em memória
        double novoSaldo = usuario.getSaldo() + valor;
        usuario.setSaldo(novoSaldo);

        // Persiste a alteração no banco (merge atualiza a entidade)
        usuarioDAO.salvarOuAtualizar(usuario);
    }
}
