package model.dao;

import model.entity.Categoria;
import model.util.HibernateUtil;

public class CategoriaDAO extends GenericDAO<Categoria, Integer> {
    public CategoriaDAO() {
        super(Categoria.class);
    }

    // Exemplo de método adicional: buscar por descrição
    public Categoria buscarPorDescricao(String descricao) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Categoria c where c.descricao = :d";
            return session.createQuery(hql, Categoria.class)
                    .setParameter("d", descricao)
                    .uniqueResult();
        }
    }
}
