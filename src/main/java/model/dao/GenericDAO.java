package model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import model.util.HibernateUtil;

import java.io.Serializable;
import java.util.List;

public class GenericDAO<T, ID extends Serializable> {

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T salvarOuAtualizar(T entidade) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T managed = session.merge(entidade);
            tx.commit();
            return managed;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void excluir(T entidade) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(session.contains(entidade) ? entidade : session.merge(entidade));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public T buscarPorId(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> buscarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from " + entityClass.getSimpleName();
            return session.createQuery(hql, entityClass).list();
        }
    }
}
