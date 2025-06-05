package model.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import model.entity.Usuario;
import model.entity.Categoria;
import model.entity.Transacao;

import java.io.File;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Criar diretório do banco se não existir
            File dbDir = new File("database");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            // Configuração do Hibernate
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            // Adicionar as classes de entidade explicitamente
            configuration.addAnnotatedClass(Usuario.class);
            configuration.addAnnotatedClass(Categoria.class);
            configuration.addAnnotatedClass(Transacao.class);

            // Construir o ServiceRegistry
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            // Construir a SessionFactory
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            
        } catch (Throwable ex) {
            System.err.println("Falha ao criar SessionFactory: " + ex);
            ex.printStackTrace(); // Para ver o erro completo
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}