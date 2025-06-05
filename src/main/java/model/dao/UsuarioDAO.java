package model.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import model.entity.Usuario;
import model.util.HibernateUtil;
import java.util.List;

public class UsuarioDAO extends GenericDAO<Usuario, Long> {

    public UsuarioDAO() {
        super(Usuario.class);
    }

    public Usuario buscarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Usuario u WHERE u.nome = :n";
            Usuario usuario = session.createQuery(hql, Usuario.class)
                                     .setParameter("n", nome)
                                     .uniqueResult();
            if (usuario != null) {
                Hibernate.initialize(usuario.getHistoricoTransacoes());
            }
            return usuario;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Usuario por nome: " + e.getMessage(), e);
        }
    }

    public Usuario buscarPorNomeESenha(String nome, String senha) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Usuario u WHERE u.nome = :n AND u.senha = :s";
            Usuario usuario = session.createQuery(hql, Usuario.class)
                                     .setParameter("n", nome)
                                     .setParameter("s", senha)
                                     .uniqueResult();
            if (usuario != null) {
                Hibernate.initialize(usuario.getHistoricoTransacoes());
            }
            return usuario;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Usuario por nome e senha: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return super.buscarPorId(id);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return super.buscarTodos();
    }
}
