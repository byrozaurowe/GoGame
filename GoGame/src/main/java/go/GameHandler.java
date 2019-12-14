package go;

import jdk.internal.util.xml.impl.Pair;

import java.util.ArrayList;

public class GameHandler {
    int whoseTurn;
    int[][] stoneLogicTable;
    int boardSize;
    int moveX, moveY;
    boolean isMoveAllowed;

    ArrayList<StoneChain> stoneChainList = new ArrayList<StoneChain>();

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
        if(isMoveAllowed == true) isPartOfChain();

        GameServer.gameServer.setTable(stoneLogicTable);
        return whoseTurn;
    }

    private void isFieldEmpty() {
        if (stoneLogicTable[moveX][moveY] != 0) {
            isMoveAllowed = false;
        }
    }
    private void isPartOfChain() {
        StoneChain lastFoundIn = null;
        for (StoneChain stoneChain: stoneChainList) {
            if (stoneChain.isPartOfThisChain(moveX, moveY)) {
                if (lastFoundIn == null) lastFoundIn = stoneChain;
                else {
                    stoneChain.mergeChains(lastFoundIn);
                    stoneChainList.remove(lastFoundIn);
                    lastFoundIn = stoneChain;
                }
            }
        }
     //utworz nowy lancuch bo nie bylo, wyżej sprawdz czy to twoj lancuch
    }
}
