import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameHandlerTest {
    GameHandler gameHandler;

    @Before
    public void settings() {
        gameHandler = new GameHandler(13);
    }
   /* @Test
    public void doesItKill() {
        gameHandler.whoseTurn = 2;
        int owner = 1;
        StoneChain stoneChain = new StoneChain(owner, 2, 3);
        ArrayList<StoneChain> fakeList = new ArrayList<>();
        fakeList.add(stoneChain);

        assertTrue(gameHandler.doesItKill(fakeList));
    } */

    @Test
    public void isLibertyLeft() {
        StoneChain chain = new StoneChain(1);
        chain.liberties.add(new Pair(4,5));

        assertTrue(gameHandler.isLibertyLeft(chain));
    }
}

