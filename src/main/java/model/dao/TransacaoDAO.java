// src/main/java/dao/TransacaoDAO.java
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

    public boolean existePorUsuarioECategoria(Long usuarioId, String descricaoCategoria) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(t.id) from Transacao t "
                       + "where t.usuario.id = :uid and t.categoria.descricao = :descCat";
            Long count = session.createQuery(hql, Long.class)
                                .setParameter("uid", usuarioId)
                                .setParameter("descCat", descricaoCategoria)
                                .uniqueResult();
            return count != null && count > 0;
        }
    }

    public List<Transacao> buscarPorUsuarioEPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Transacao t where t.usuario.id = :uid "
                       + "and t.data between :ini and :fim order by t.data desc";
            return session.createQuery(hql, Transacao.class)
                          .setParameter("uid", usuarioId)
                          .setParameter("ini", inicio)
                          .setParameter("fim", fim)
                          .list();
        }
    }
}
