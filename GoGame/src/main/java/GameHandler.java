import java.util.ArrayList;
import java.util.Iterator;

public class GameHandler {
    int whoseTurn;
    int[][] stoneLogicTable;
    int boardSize;
    int moveX, moveY;
    boolean isMoveAllowed;

    ArrayList<StoneChain> stoneChainList = new ArrayList<StoneChain>();
    ArrayList<StoneChain> fakeStoneChainList;

    public GameHandler(int boardSize) {
        this.boardSize = boardSize;
    }

    public int move (int moveX, int moveY, int whoseTurn, int[][] table) {
        this.moveX = moveX;
        this.moveY = moveY;
        this.whoseTurn = whoseTurn;
        stoneLogicTable = table;
        isMoveAllowed = true;

        isFieldEmpty();

        if(isMoveAllowed == true) {
            fakeStoneChainList = new ArrayList<StoneChain>();
            for (StoneChain chain: stoneChainList) {
                fakeStoneChainList.add(chain);
            }
            if (isLibertyLeft(isPartOfChainFake(fakeStoneChainList)) || doesItKill(fakeStoneChainList)) {
                isPartOfChain(stoneChainList); // dwa razy robi nowy lancuch tutaj
                stoneLogicTable[moveX][moveY] = whoseTurn;
                /* System.out.println("Przypisuje graczowi " + whoseTurn);
                System.out.println("przed zabiciem"); */
                removeDead(stoneChainList);
                System.out.println("po zabiciu");
                if (whoseTurn == 1) whoseTurn = 2;
                else whoseTurn = 1;
                System.out.println("Stan listy łańcuchów:");
                 for (StoneChain chain: stoneChainList) {
                    System.out.println("Lista: ");
                    for (Pair pair: chain.stoneChain) {
                        System.out.println("Para: " + pair.getKey() + pair.getValue());
                    }
                    System.out.println("Lista oddechów: ");
                    for (Pair pair: chain.liberties) {
                        System.out.println("Oddech: " +  pair.getKey() + pair.getValue());
                    }
                }
            }
        }

        GameServer.gameServer.setTable(stoneLogicTable);
        return whoseTurn;
    }

    private void isFieldEmpty() {
        if (stoneLogicTable[moveX][moveY] != 0) {
            isMoveAllowed = false;
        }
    }
    private StoneChain isPartOfChain(ArrayList<StoneChain> list) {
        StoneChain lastFoundIn = null;
        for (Iterator<StoneChain> it = list.iterator(); it.hasNext();) {
            StoneChain stoneChain = it.next();
            if (stoneChain.owner == whoseTurn) {
                if (stoneChain.isPartOfThisChain(moveX, moveY)) {
                    if (lastFoundIn != null) {
                        System.out.println("Lacze lancuchy");
                        lastFoundIn.mergeChains(stoneChain);
                        System.out.println("polaczylem łancuchy");
                        it.remove();
                    }
                    lastFoundIn = stoneChain;
                }
            }
            if (stoneChain.owner != whoseTurn) {
                stoneChain.removeLiberty(moveX, moveY);
            }
        }
        if (lastFoundIn == null) {
            System.out.println("Nie znalazlem łancucha dla:" + moveX + moveY + "tworze nowy");
            lastFoundIn = new StoneChain(whoseTurn, moveX, moveY);
            list.add(lastFoundIn);
        }
        return lastFoundIn;
    }

    private StoneChain isPartOfChainFake(ArrayList<StoneChain> list) {
        StoneChain lastFoundIn = null;
        for (Iterator<StoneChain> it = list.iterator(); it.hasNext();) {
            StoneChain stoneChain = it.next();
            if (stoneChain.owner == whoseTurn) {
                if (stoneChain.isPartOfThisChainFake(moveX, moveY)) {
                    if (lastFoundIn != null) {
                        System.out.println("Lacze lancuchy");
                        lastFoundIn.mergeChains(stoneChain);
                        System.out.println("polaczylem łancuchy");
                        it.remove();
                    }
                    lastFoundIn = stoneChain;
                }
            }
            if (stoneChain.owner != whoseTurn) {
                stoneChain.removeLiberty(moveX, moveY);
            }
        }
        if (lastFoundIn == null) {
            System.out.println("Nie znalazlem łancucha dla:" + moveX + moveY + "tworze nowy");
            lastFoundIn = new StoneChain(whoseTurn, moveX, moveY);
            list.add(lastFoundIn);
        }
        return lastFoundIn;
    }

    private boolean isLibertyLeft (StoneChain chain) {
        if (chain.liberties.size() > 0 ) {
            return true;
        }
        else return false;
    }

    private boolean doesItKill(ArrayList<StoneChain> fakeList) {
        for (StoneChain chain: fakeList) {
            if (chain.owner != whoseTurn && chain.liberties.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void removeDead (ArrayList<StoneChain> stoneChainList) {
        /*for (StoneChain chain: stoneChainList) {
            if (chain.owner != whoseTurn && chain.liberties.isEmpty()) {
                for(Pair pair: chain.stoneChain) {
                    stoneLogicTable[pair.getKey()][pair.getValue()] = 0;
                }
                stoneChainList.remove(chain);
                chain.restoreLibertiesToNeighbours();
            }
        }*/
        for (Iterator<StoneChain> it = stoneChainList.iterator(); it.hasNext();) {
            StoneChain chain = it.next();
            if (chain.owner != whoseTurn && chain.liberties.isEmpty()) {
                for(Pair pair: chain.stoneChain) {
                    stoneLogicTable[pair.getKey()][pair.getValue()] = 0;
                }
                StoneChain temp = chain;
                it.remove();
                temp.restoreLibertiesToNeighbours();
            }
        }
    }

    StoneChain findStonesChain(Pair pair) {
        System.out.println("szukam chaina");
        for (StoneChain chain: stoneChainList) {
            System.out.println("Lancuch: " );
            for (Pair stone: chain.stoneChain) {
                System.out.println("Para: " + stone.getKey() + stone.getValue());
                if (stone.getKey() == pair.getKey() && stone.getValue() == pair.getValue()) {
                    System.out.println("znalazlem chaina");
                    return chain;
                }
            }
        }
        return null;
    }
}
