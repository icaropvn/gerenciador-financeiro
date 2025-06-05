package model.dao;

import model.entity.Categoria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import model.util.HibernateUtil;

public class CategoriaDAO extends GenericDAO<Categoria, Long> {

    public CategoriaDAO() {
        super(Categoria.class);
    }

    public Categoria buscarPorDescricao(String descricao) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Categoria c WHERE c.descricao = :d";
            return session.createQuery(hql, Categoria.class)
                          .setParameter("d", descricao)
                          .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Categoria por descrição: " + e.getMessage(), e);
        }
    }
    
    public Categoria atualizar(Categoria categoria) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Categoria merged = (Categoria) session.merge(categoria);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao atualizar Categoria: " + e.getMessage(), e);
        }
    }
}
