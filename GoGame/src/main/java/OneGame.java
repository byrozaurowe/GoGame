import org.hibernate.annotations.OptimisticLockType;

import javax.persistence.*;
import java.util.Date;

@Entity
//@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.ALL)
@Table(name = "onegame", uniqueConstraints = {
        @UniqueConstraint(columnNames = "gameId"),
        @UniqueConstraint(columnNames = "moveId"),
        @UniqueConstraint(columnNames = "moveString")})
public class OneGame {
    @Column(name = "gameId", unique = true, nullable = false)
    private int gameId;

    @Column(name = "moveId", nullable = false)
    private int moveId;

    @Column(name = "moveString", nullable = false)
    private String moveString;

    public OneGame() {
        super();
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int id) {
        this.gameId = id;
    }

    public int getMoveId() {
        return moveId;
    }

    public void setMoveId(int id) {
        this.moveId = id;
    }

    public String getMoveString() {
        return moveString;
    }

    public void setMoveString(String moveString) {
        this.moveString = moveString;
    }
}
