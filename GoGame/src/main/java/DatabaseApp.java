import org.hibernate.*;
import org.hibernate.boot.model.relational.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class DatabaseApplication {

    DatabaseApplication() {}

    public static List queries(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        if(args[0].equals("date list")) {
            Query query = session.createQuery("SELECT date FROM SavedGames");
            List result = query.list();
            return result;
        }
        if(args[0].equals("newGame")) {
            SavedGames savedGames = new SavedGames();
            Date date = new Date();
            java.sql.Date sDate = new java.sql.Date(date.getTime());
            savedGames.setDate(sDate);
            session.save(savedGames);
        }
        else if(args[0].equals("data")) {

            Query query = null;
            query = session.createQuery("SELECT id FROM SavedGames WHERE date = '" + args[1] + "'");
            List result = query.list();
            query = session.createQuery("SELECT moveString FROM OneGame WHERE gameId = " + result.get(0) + " ORDER BY id");
            result = query.list();
            return result;
        }
        else if(args[0].equals("list of moves")) {
            Query query = session.createQuery("SELECT COUNT(*) FROM SavedGames");
            List result = query.list();
            OneGame oneGame = new OneGame();
            oneGame.setGameId(((Long) result.get(0)).intValue());
            oneGame.setMoveId(Integer.parseInt(args[1]));
            oneGame.setMoveString(args[2]);
            session.save(oneGame);
        }
        session.getTransaction().commit();
        return null;
    }
}
