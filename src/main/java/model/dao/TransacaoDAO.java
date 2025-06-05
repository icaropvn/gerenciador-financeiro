package model.dao;

import model.entity.Transacao;
import org.hibernate.Session;
import model.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class TransacaoDAO extends GenericDAO<Transacao, Long> {

    public TransacaoDAO() {
        super(Transacao.class);
    }

    public List<Transacao> buscarPorUsuario(Long usuarioId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Transacao t WHERE t.usuario.id = :uid ORDER BY t.data DESC";
            return session.createQuery(hql, Transacao.class)
                          .setParameter("uid", usuarioId)
                          .list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar transações por usuário: " + e.getMessage(), e);
        }
    }

    public List<Transacao> buscarPorUsuarioEPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Transacao t "
                       + "WHERE t.usuario.id = :uid "
                       + "  AND t.data BETWEEN :ini AND :fim "
                       + "ORDER BY t.data DESC";
            return session.createQuery(hql, Transacao.class)
                          .setParameter("uid", usuarioId)
                          .setParameter("ini", inicio)
                          .setParameter("fim", fim)
                          .list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar transações por período: " + e.getMessage(), e);
        }
    }

    public boolean existePorUsuarioECategoria(Long usuarioId, String descricaoCategoria) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transacao t "
                       + "WHERE t.usuario.id = :uid "
                       + "  AND t.categoria.descricao = :dc";
            Long qtd = session.createQuery(hql, Long.class)
                              .setParameter("uid", usuarioId)
                              .setParameter("dc", descricaoCategoria)
                              .uniqueResult();
            return (qtd != null && qtd > 0);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar existência de transação: " + e.getMessage(), e);
        }
    }
}
