import java.util.ArrayList;
import java.util.Iterator;

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
    public StoneChain (int owner) {
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

    void findLiberties(ArrayList<Pair> neighbours, ArrayList<Pair> liberties) {
        for (Pair pair: neighbours) {
            if (GameServer.gameServer.gameHandler.stoneLogicTable[pair.getKey()][pair.getValue()] == 0) {
                if (!listContains((pair), liberties)) {
                    liberties.add(new Pair(pair.getKey(), pair.getValue()));
                }
            }
        }
    }

    boolean isPartOfThisChain(int x, int y) {
        for (Pair field: stoneChain) {
            if ((field.getKey() == x && abs(field.getValue() - y) == 1) ||  (field.getValue() == y && abs(field.getKey() - x) == 1)) {
                if (!listContains(new Pair(x,y), stoneChain)) {
                    findLiberties(findNeighbours(x, y), liberties);
                    removeLiberty(x, y);
                    System.out.println("Found chain of: " + x + y);
                    return true;
                }
            }
        }
        return false;
    }

    /*boolean isPartOfThisChainFake(int x, int y) {
        for (Pair field: stoneChain) {
            if ((field.getKey() == x && abs(field.getValue() - y) == 1) ||  (field.getValue() == y && abs(field.getKey() - x) == 1)) {
                if (!listContains(new Pair(x,y), stoneChain)) {
                    System.out.println("Found chain of: " + x + y);
                    return true;
                }
            }
        }
        return false;
    } */

    void removeLiberty (int x, int y) {
        // zle bo nie mozesz usuwac w foreachu
        /*for (Pair pair: liberties) {
            if (pair.getKey() == x && pair.getValue() == y) {
                liberties.remove(pair);
            }
        }*/
        /* dobrze ale dlugo, uzywamy remove iteratora
        for (Iterator<Pair> it = liberties.iterator(); it.hasNext();) {
            Pair pair = it.next();
            if (pair.getKey() == x && pair.getValue() == y) {
                it.remove();
            }
        }*/
        liberties.removeIf(pair -> pair.getKey() == x && pair.getValue() == y);
    }

    void mergeChains(StoneChain toMerge) {
        for (Pair pair: toMerge.stoneChain) {
            this.stoneChain.add(pair);
        }
        for (Pair pair: toMerge.liberties) {
            this.liberties.add(pair);
        }
    }

    void restoreLibertiesToNeighbours() {
        for (Pair pair: stoneChain) {
            System.out.println("Restoring Liberties to neighbours of:" + pair.getKey() + pair.getValue());
            for (Pair neighbour: findNeighbours(pair.getKey(), pair.getValue())) {
                System.out.println("Neigbour: " + neighbour.getKey() + neighbour.getValue());
                if (!listContains(neighbour, stoneChain) && GameServer.gameServer.gameHandler.stoneLogicTable[neighbour.getKey()][neighbour.getValue()] != owner && GameServer.gameServer.gameHandler.stoneLogicTable[neighbour.getKey()][neighbour.getValue()] != 0) {
                    System.out.println("jestem w ifie w restoreliberties");
                    GameServer.gameServer.gameHandler.findStonesChain(neighbour).liberties.add(pair);
                }
            }
        }
    }

    boolean listContains(Pair checkPair, ArrayList<Pair> list) {
        for (Pair pair: list) {
            if (pair.getKey() == checkPair.getKey() && pair.getValue() == checkPair.getValue()) {
                return true;
            }
        }
        return false;
    }



}
