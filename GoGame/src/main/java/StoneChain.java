import java.util.ArrayList;

import static java.lang.Math.abs;

public class StoneChain {
    ArrayList<Pair> stoneChain = new ArrayList<Pair>();
    ArrayList<Pair> liberties = new ArrayList<Pair>();
    int owner;

    public StoneChain (int owner, int x, int y) {
        stoneChain.add(new Pair(x, y));
        findLiberties(findNeighbours(x, y), liberties);
        this.owner = owner;
    }

    ArrayList<Pair> findNeighbours (int x, int y) {
        ArrayList<Pair> neighbours = new ArrayList<Pair>();
        if ((x-1) >= 0) {
            neighbours.add(new Pair(x-1, y));
        }
        if ((y-1) >= 0 ) {
            neighbours.add(new Pair(x, y-1));
        }
        if ((x+1) < GameServer.gameServer.gameHandler.boardSize ) {
            neighbours.add(new Pair(x+1, y));
        }
        if ((y+1) < GameServer.gameServer.gameHandler.boardSize ) {
            neighbours.add(new Pair(x, y+1));
        }
        return neighbours;
    }

    void findLiberties(ArrayList<Pair> neigbours, ArrayList<Pair> liberties) {
        for (Pair pair: neigbours) {
            if (GameServer.gameServer.gameHandler.stoneLogicTable[pair.getKey()][pair.getValue()] == 0) {
                liberties.add(new Pair(pair.getKey(), pair.getValue()));
            }
        }
    }

    boolean isPartOfThisChain(int x, int y) {
        for (Pair field: stoneChain) {
            if ((field.getKey() == x && abs(field.getValue() - y) == 1) ||  (field.getValue() == y && abs(field.getKey() - x) == 1)) {
                stoneChain.add(new Pair(x, y));
                findLiberties(findNeighbours(x, y), liberties);
                removeLiberty(x, y);
                return true;
            }
        }
        return false;
    }

    void removeLiberty (int x, int y) {
        for (Pair pair: liberties) {
            if (pair.getKey() == x && pair.getValue() == y) {
                liberties.remove(pair);
            }
        }
    }

    void mergeChains(StoneChain toMerge) {
        for (Pair pair: toMerge.stoneChain) {
            stoneChain.add(pair);
        }
        for (Pair pair: toMerge.liberties) {
            liberties.add(pair);
        }
    }

    void restoreLibertiesToNeighbours() {
        for (Pair pair: stoneChain) {
            for (Pair neighbour: findNeighbours(pair.getKey(), pair.getValue())) {
              findLiberties(findNeighbours(pair.getKey(), pair.getValue()), GameServer.gameServer.gameHandler.findStonesChain(neighbour).liberties);
            }
        }
    }

    boolean contains(Pair checkPair) {
        for (Pair pair: stoneChain) {
            if (pair == checkPair) {
                return true;
            }
        }
        return false;
    }



}
