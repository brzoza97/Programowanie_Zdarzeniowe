package MP3Player.dbConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SongEntity {
    private SessionFactory sessionFactory = Hibernate.getSessionFactory();

    public ObservableList<Song> getSongs()
    {
        ObservableList songs;
        Transaction transaction = null;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            songs =  FXCollections.observableArrayList(session.createQuery("from Song").getResultList());

            transaction.commit();

        }catch (Exception e){
            songs = null;
            if(transaction!=null){
                transaction.rollback();
            }
        }finally {
            session.close();
        }

        return songs;
    }
}
