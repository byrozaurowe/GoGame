package go;
import javafx.util.Pair;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class StoneChain {
    ArrayList<Pair<Integer, Integer>> stoneChain = new ArrayList<Pair<Integer, Integer>>();
    ArrayList<Pair<Integer, Integer>> liberties = new ArrayList<Pair<Integer, Integer>>();
    int owner;

    public StoneChain (int owner, int x, int y) {
        stoneChain.add(new Pair(x, y));
        findLiberties(x, y);
        this.owner = owner;
    }

    void findLiberties (int x, int y) {
        if ((x-1) >= 0 && GameServer.gameServer.gameHandler.stoneLogicTable[x-1][y] == 0) {
            liberties.add(new Pair(x-1, y));
        }
        if ((y-1) >= 0 && GameServer.gameServer.gameHandler.stoneLogicTable[x][y-1] == 0) {
            liberties.add(new Pair(x, y-1));
        }
        if ((x+1) < GameServer.gameServer.gameHandler.boardSize && GameServer.gameServer.gameHandler.stoneLogicTable[x+1][y] == 0) {
            liberties.add(new Pair(x+1, y));
        }
        if ((y+1) < GameServer.gameServer.gameHandler.boardSize && GameServer.gameServer.gameHandler.stoneLogicTable[x][y+1] == 0) {
            liberties.add(new Pair(x, y+1));
        }
    }

    boolean isPartOfThisChain(int x, int y) {
        for (Pair<Integer, Integer> field: stoneChain) {
            if ((field.getKey() == x && abs(field.getValue() - y) == 1) ||  (field.getValue() == y && abs(field.getKey() - x) == 1)) {
                stoneChain.add(new Pair(x, y));
                findLiberties(x, y);
                removeLiberty(x, y);
                return true;
            }
        }
        return false;
    }

    void removeLiberty (int x, int y) {
        for (Pair<Integer, Integer> pair: liberties) {
            if (pair.getKey() == x && pair.getValue() == y) {
                liberties.remove(pair);
            }
        }
    }

    void mergeChains(StoneChain toMerge) {
        for (Pair<Integer, Integer> pair: toMerge.stoneChain) {
            stoneChain.add(pair);
        }
        for (Pair<Integer, Integer> pair: toMerge.liberties) {
            liberties.add(pair);
        }
    }



}
