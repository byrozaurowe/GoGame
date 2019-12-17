import java.util.ArrayList;
import java.util.Iterator;

/** Glowna klasa obslugujaca sie logike */
class GameHandler {
    /** Id gracza czyja tura */
    private int whoseTurn;
    /** Logiczna tablica zawierajaca stan planszy */
    int[][] stoneLogicTable;
    /** Rozmiar planszy */
    int boardSize;
    /** Wspolrzedna x ruchu */
    private int moveX;
    /** Wspolrzedna y ruchu */
    private int moveY;
    /** Czy ruch jest dozwolony */
    private boolean isMoveAllowed;
    /** Ostatni zabity kamien */
    private Pair lastKilled;
    /** Ten co zabil ostatni kamien */
    private Pair killer;
    /** */
    private Pair toKill;
    /** Licznik rund */
    private int roundCounter = 0;

    /** Lista lancuchow */
    private ArrayList<StoneChain> stoneChainList = new ArrayList<>();
    /** Podrobka listy lancuchu uzywana do sprawdzania samobojczych ruchow */
    private ArrayList<StoneChain> fakeStoneChainList;

    /** Konstrukor
     * @param boardSize rozmiar planszy
     */
    GameHandler(int boardSize) {
        this.boardSize = boardSize;
    }

    /** Ruch gracza
     * @param moveX wspolrzedna x ruchu gracza
     * @param moveY wspolrzedna y ruchu gracza
     * @param whoseTurn id gracza ktorego jest tura
     * @param table stan planszy
     * @return id gracza czyjego bedzie tura
     */
    int move (int moveX, int moveY, int whoseTurn, int[][] table) {
        this.moveX = moveX;
        this.moveY = moveY;
        this.whoseTurn = whoseTurn;
        stoneLogicTable = table;
        isMoveAllowed = true;

        isFieldEmpty();

        if(isMoveAllowed) {
            fakeStoneChainList = new ArrayList<>();
            for (StoneChain chain: stoneChainList) {
                StoneChain newChain = new StoneChain(chain.owner);
                for (Pair pair: chain.stoneChain) {
                    newChain.stoneChain.add(new Pair(pair.getKey(), pair.getValue()));
                }
                for (Pair pair: chain.liberties) {
                    newChain.liberties.add(new Pair(pair.getKey(), pair.getValue()));
                }
                fakeStoneChainList.add(newChain);
            }

           // System.out.println("fejkowa tura");
            if (isLibertyLeft(isPartOfChain(fakeStoneChainList)) || doesItKill(fakeStoneChainList )) {
                if (!isKo()) {
                    // System.out.println("normalna tura");
                    isPartOfChain(stoneChainList); // dwa razy robi nowy lancuch tutaj
                    stoneLogicTable[moveX][moveY] = whoseTurn;
                    /* System.out.println("Przypisuje graczowi " + whoseTurn);
                    System.out.println("przed zabiciem"); */
                    removeDead(stoneChainList);
                    //System.out.println("po zabiciu");
                    roundCounter ++;
                    if (whoseTurn == 1) whoseTurn = 2;
                    else whoseTurn = 1;
                }
            }
        }

        GameServer.gameServer.setTable(stoneLogicTable);
        return whoseTurn;
    }

    /** Sprawdza czy to sytuacja ko
     * @return tak lub nie
     */
    private boolean isKo() {
        if (toKill != null && killer != null && lastKilled != null && roundCounter == 1) {
            return toKill.getKey() == killer.getKey() && toKill.getValue() == killer.getValue() && lastKilled.getKey() == moveX && lastKilled.getValue() == moveY;
        }
        return false;
    }

    /** Czy pole jest puste? Ustawia czy ruch jest mozliwy */
    private void isFieldEmpty() {
        if (stoneLogicTable[moveX][moveY] != 0) {
            isMoveAllowed = false;
        }
    }

    /** Sprawdza czy jest */
    private StoneChain isPartOfChain(ArrayList<StoneChain> list) {
        StoneChain lastFoundIn = null;
        for (Iterator<StoneChain> it = list.iterator(); it.hasNext();) {
            StoneChain stoneChain = it.next();
            if (stoneChain.owner == whoseTurn) {
                if (stoneChain.isPartOfThisChain(moveX, moveY)) {
                    if (lastFoundIn != null) {
                       /* System.out.println("Lacze lancuch:");
                        for (Pair pair: lastFoundIn.stoneChain) {
                            System.out.println("Para: " + pair.getKey() + pair.getValue());
                        }
                        System.out.println("Lista oddechów: ");
                        for (Pair pair: lastFoundIn.liberties) {
                            System.out.println("Oddech: " +  pair.getKey() + pair.getValue());
                        }
                        System.out.println("z łańcuchem:");
                        for (Pair pair: stoneChain.stoneChain) {
                            System.out.println("Para: " + pair.getKey() + pair.getValue());
                        }
                        System.out.println("Lista oddechów: ");
                        for (Pair pair: stoneChain.liberties) {
                            System.out.println("Oddech: " +  pair.getKey() + pair.getValue());
                        } */
                        lastFoundIn.mergeChains(stoneChain);
                        //System.out.println("polaczylem łancuchy");
                        it.remove();
                        /*System.out.println("Połączony łańcuch: ");
                        for (Pair pair: lastFoundIn.stoneChain) {
                            System.out.println("Para: " + pair.getKey() + pair.getValue());
                        }
                        System.out.println("Lista oddechów: ");
                        for (Pair pair: lastFoundIn.liberties) {
                            System.out.println("Oddech: " +  pair.getKey() + pair.getValue());
                        } */
                    }
                    else {
                        stoneChain.stoneChain.add(new Pair(moveX,moveY));
                        lastFoundIn = stoneChain;
                    }
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

    /** Sprawdza, czy zostaly jakies oddechy
     * @param chain lancuch w ktorym sprawdzamy oddechy
     * @return tak lub nie
     */
    private boolean isLibertyLeft (StoneChain chain) {
        return chain.liberties.size() > 0;
    }

    /** Sprawdza czy ginie
     * @return tak lub nie
     * */
    private boolean doesItKill(ArrayList<StoneChain> fakeList) {
        for (StoneChain chain: fakeList) {
            if (chain.owner != whoseTurn && chain.liberties.isEmpty()) {
                toKill = chain.stoneChain.get(0);
                return true;
            }
        }
        return false;
    }

    /** Usuwa z planszy martwe kamienie
     * @param stoneChainList lista wszystkich lancuchow do sprawdzenia
     */
    private void removeDead (ArrayList<StoneChain> stoneChainList) {
       for (Iterator<StoneChain> it = stoneChainList.iterator(); it.hasNext();) {
            StoneChain chain = it.next();
            if (chain.owner != whoseTurn && chain.liberties.isEmpty()) {
                for(Pair pair: chain.stoneChain) {
                    if (stoneLogicTable[pair.getKey()][pair.getValue()] !=0) {
                        GameServer.gameServer.captives[whoseTurn-1]++;
                    }
                    stoneLogicTable[pair.getKey()][pair.getValue()] = 0;
                    lastKilled = new Pair(pair.getKey(), pair.getValue());
                    killer = new Pair(moveX, moveY);
                    roundCounter = 0;
                }
                it.remove();
                chain.restoreLibertiesToNeighbours();
            }
        }
    }

    /** Szuka lancucha dla danego kamienia
     * @param pair kamien
     * @return znaleziony lancuch lub null
     */
    StoneChain findStonesChain (Pair pair) {
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
