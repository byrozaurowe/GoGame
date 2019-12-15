package go;
import javafx.util.Pair;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class StoneChain {
    ArrayList<Pair<Integer, Integer>> stoneChain = new ArrayList<Pair<Integer, Integer>>();
    ArrayList<Pair<Integer, Integer>> liberties = new ArrayList<Pair<Integer, Integer>>();
    int owner;

    public StoneChain (int owner, int x, int y) {
        stoneChain.add(new Pair<Integer, Integer>(x, y));
        findLiberties(findNeighbours(x, y), liberties);
        this.owner = owner;
    }

    ArrayList<Pair<Integer, Integer>> findNeighbours (int x, int y) {
        ArrayList<Pair<Integer, Integer>> neighbours = new ArrayList<Pair<Integer, Integer>>();
        if ((x-1) >= 0) {
            neighbours.add(new Pair<Integer, Integer>(x-1, y));
        }
        if ((y-1) >= 0 ) {
            neighbours.add(new Pair<Integer, Integer>(x, y-1));
        }
        if ((x+1) < GameServer.gameServer.gameHandler.boardSize ) {
            neighbours.add(new Pair<Integer, Integer>(x+1, y));
        }
        if ((y+1) < GameServer.gameServer.gameHandler.boardSize ) {
            neighbours.add(new Pair<Integer, Integer>(x, y+1));
        }
        return neighbours;
    }

    void findLiberties(ArrayList<Pair<Integer, Integer>> neigbours, ArrayList<Pair<Integer, Integer>> liberties) {
        for (Pair<Integer, Integer> pair: neigbours) {
            if (GameServer.gameServer.gameHandler.stoneLogicTable[pair.getKey()][pair.getValue()] == 0) {
                liberties.add(new Pair<Integer, Integer>(pair.getKey(), pair.getValue()));
            }
        }
    }

    boolean isPartOfThisChain(int x, int y) {
        for (Pair<Integer, Integer> field: stoneChain) {
            if ((field.getKey() == x && abs(field.getValue() - y) == 1) ||  (field.getValue() == y && abs(field.getKey() - x) == 1)) {
                stoneChain.add(new Pair<Integer, Integer>(x, y));
                findLiberties(findNeighbours(x, y), liberties);
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

    void restoreLibertiesToNeighbours() {
        for (Pair<Integer, Integer> pair: stoneChain) {
            for (Pair<Integer, Integer> neigbour: findNeighbours(pair.getKey(), pair.getValue())) {
              findLiberties(findNeighbours(pair.getKey(), pair.getValue()), GameServer.gameServer.gameHandler.findStonesChain(neigbour).liberties);
            }
        }
    }

    boolean contains(Pair<Integer, Integer> checkPair) {
        for (Pair<Integer, Integer> pair: stoneChain) {
            if (pair == checkPair) {
                return true;
            }
        }
        return false;
    }



}
