import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class StoneChainTest {
    StoneChain stoneChain;

    @Before
    public void settings() {
        stoneChain = new StoneChain(1);
    }

    @Test
    public void listContains() {
        ArrayList<Pair> list = new ArrayList<>();
        list.add(new Pair(1,2));
        list.add(new Pair(10, 9));
        list.add(new Pair (6, 0));

        assertTrue(stoneChain.listContains(new Pair(6,0), list));
        assertFalse(stoneChain.listContains(new Pair(1, 9), list));
    }

}
