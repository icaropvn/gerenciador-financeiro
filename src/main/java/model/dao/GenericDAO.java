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
    
    public void salvar(T entidade) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entidade);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao salvar " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    public T atualizar(T entidade) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            @SuppressWarnings("unchecked")
            T merged = (T) session.merge(entidade);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao atualizar " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    public void excluir(T entidade) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T managed = session.contains(entidade) ? entidade : (T) session.merge(entidade);
            session.remove(managed);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao excluir " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    public T buscarPorId(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar " + entityClass.getSimpleName() + " por id: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> buscarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName();
            return session.createQuery(hql, entityClass).list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos de " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        }
    }
}
