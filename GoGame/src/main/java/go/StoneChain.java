package go;

import jdk.internal.net.http.common.Pair;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class StoneChain {
    ArrayList<Pair<Integer, Integer>> stoneChain = new ArrayList<Pair<Integer, Integer>>();
    ArrayList<Pair<Integer, Integer>> liberties = new ArrayList<Pair<Integer, Integer>>();
    int owner;

    boolean isPartOfThisChain(int x, int y) {
        for (Pair<Integer, Integer> field: stoneChain) {
            if ((field.first == x && abs(field.second - y) == 1) ||  (field.second == y && abs(field.first - x) == 1)) {
                stoneChain.add(Pair.pair(x,y));
                removeLiberty(x,y);
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
