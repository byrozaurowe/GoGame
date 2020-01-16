import org.hibernate.*;
import org.hibernate.boot.model.relational.Database;

import java.util.Date;
import java.util.List;

class DatabaseApplication {

    DatabaseApplication() {}

    public static List queries(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        if(args[0].equals("newGame")) {
            SavedGames savedGames = new SavedGames();
            Date date = new Date();
            java.sql.Date sDate = new java.sql.Date(date.getTime());
            savedGames.setDate(sDate);
            session.save(savedGames);
        }
        if(args[0].equals("data")) {
            Query query = session.createQuery("FROM OneGame WHERE gameId = (SELECT id FROM SavedGames WHERE date =" + args[1] + ")");
            List result = query.list();
            return result;
        }
        else {
            Query query = session.createQuery("SELECT COUNT(*) FROM SavedGames");
            List result = query.list();
            OneGame oneGame = new OneGame();
            oneGame.setGameId(((Long) result.get(0)).intValue());
            oneGame.setMoveId(Integer.parseInt(args[0]));
            oneGame.setMoveString(args[1]);
            session.save(oneGame);
        }
        session.getTransaction().commit();
        return null;
    }
}
