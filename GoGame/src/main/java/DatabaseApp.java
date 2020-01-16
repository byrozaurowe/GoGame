import org.hibernate.*;
import org.hibernate.cfg.*;

import java.util.Date;

class DatabaseApplication {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        // Add new Employee object
        SavedGames savedGames = new SavedGames();
        savedGames.setId(6);
        savedGames.setDate(new Date(20,12,2001));

        session.save(savedGames);

        session.getTransaction().commit();
        HibernateUtil.shutdown();
    }
}
