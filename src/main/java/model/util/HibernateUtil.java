package model.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            File dbDir = new File("database");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            Configuration cfg = new Configuration();
            cfg.configure();

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
            sessionFactory = cfg.buildSessionFactory(builder.build());
        } catch (Throwable ex) {
            System.err.println("Falha ao criar SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
