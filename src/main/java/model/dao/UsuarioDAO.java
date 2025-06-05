package model.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import model.entity.Usuario;
import model.util.HibernateUtil;

public class UsuarioDAO extends GenericDAO<Usuario, Long> {
    public UsuarioDAO() {
        super(Usuario.class);
    }
    
    public Usuario buscarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Usuario u where u.nome = :n";
            return session.createQuery(hql, Usuario.class).setParameter("n", nome).uniqueResult();
        }
    }

    public Usuario buscarPorNomeESenha(String nome, String senha) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Usuario u where u.nome = :n and u.senha = :s";
            Usuario usuario = session.createQuery(hql, Usuario.class)
                    .setParameter("n", nome)
                    .setParameter("s", senha)
                    .uniqueResult();
            
            // Inicializar a coleção antes de fechar a sessão
            if (usuario != null) {
                Hibernate.initialize(usuario.getHistoricoTransacoes());
            }
            
            return usuario;
        }
    }
}