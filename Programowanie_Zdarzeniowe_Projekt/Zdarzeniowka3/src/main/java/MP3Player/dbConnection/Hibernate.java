package MP3Player.dbConnection;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Hibernate {

        private static SessionFactory sessionFactory;

        private static SessionFactory buildSessionFactory() {
            try {

                Configuration configuration = new Configuration();
                configuration.addAnnotatedClass(Song.class);

                configuration.configure("hibernate.cfg.xml");
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
                        .build();

                return configuration.buildSessionFactory(serviceRegistry);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new ExceptionInInitializerError(e);
            }
        }

        public static SessionFactory getSessionFactory() {
            if (sessionFactory == null)
                sessionFactory = buildSessionFactory();
            return sessionFactory;
        }
}
