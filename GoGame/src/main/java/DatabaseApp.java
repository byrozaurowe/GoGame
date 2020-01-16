import org.hibernate.*;
import java.util.Date;
import java.util.List;

class DatabaseApplication {
    public static void main(String[] args) {
        if(args[0].equals("newGame")) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            SavedGames savedGames = new SavedGames();
            Date date = new Date();
            java.sql.Date sDate = new java.sql.Date(date.getTime());
            savedGames.setDate(sDate);

            session.save(savedGames);

            session.getTransaction().commit();
        }
        else {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("SELECT COUNT(*) FROM SavedGames");
            List result = query.list();
            OneGame oneGame = new OneGame();
            oneGame.setGameId(((Long) result.get(0)).intValue());
            oneGame.setMoveId(Integer.parseInt(args[0]));
            oneGame.setMoveString(args[1]);
            session.save(oneGame);
            session.getTransaction().commit();
        }
    }
}
