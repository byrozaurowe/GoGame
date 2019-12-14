package go;

import jdk.internal.net.http.common.Pair;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class StoneChain {
    ArrayList<Pair<Integer, Integer>> stoneChain = new ArrayList<Pair<Integer, Integer>>();
    ArrayList<Pair<Integer, Integer>> liberties = new ArrayList<Pair<Integer, Integer>>();
    int owner;

    public StoneChain (int owner, int x, int y) {
        stoneChain.add(Pair.pair(x, y));
        initialLiberties(x, y);
        this.owner = owner;
    }

    void initialLiberties (int x, int y) {
        if ((x-1) >= 0 && GameServer.gameServer.gameHandler.stoneLogicTable[x-1][y] == 0) {
            liberties.add(Pair.pair(x-1, y));
        }
        if ((y-1) >= 0 && GameServer.gameServer.gameHandler.stoneLogicTable[x][y-1] == 0) {
            liberties.add(Pair.pair(x, y-1));
        }
        if ((x+1) < GameServer.gameServer.gameHandler.boardSize && GameServer.gameServer.gameHandler.stoneLogicTable[x+1][y] == 0) {
            liberties.add(Pair.pair(x+1, y));
        }
        if ((y+1) < GameServer.gameServer.gameHandler.boardSize && GameServer.gameServer.gameHandler.stoneLogicTable[x][y+1] == 0) {
            liberties.add(Pair.pair(x, y+1));
        }
    }

    boolean isPartOfThisChain(int x, int y) {
        for (Pair<Integer, Integer> field: stoneChain) {
            if ((field.first == x && abs(field.second - y) == 1) ||  (field.second == y && abs(field.first - x) == 1)) {
                stoneChain.add(Pair.pair(x, y));
                removeLiberty(x, y);
                return true;
            }
        }
        return false;
    }

    void removeLiberty (int x, int y) {
        liberties.remove(Pair.pair(x,y));
    }

    void mergeChains(StoneChain toMerge) {
        for (Pair<Integer, Integer> pair: toMerge.stoneChain) {
            stoneChain.add(pair);
        }
    }
}
