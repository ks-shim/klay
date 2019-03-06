package klay.common.dictionary.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Lift extends Reduce {
    boolean changeSkip;

    /**
     * Constructor for the Lift object.
     *
     * @param changeSkip when set to <tt>true</tt>, comparison of two Cells takes
     *          a skip command into account
     */
    public Lift(boolean changeSkip) {
        this.changeSkip = changeSkip;
    }

    /**
     * Optimize (eliminate rows with no content) the given Trie and return the
     * reduced Trie.
     *
     * @param orig the Trie to optimized
     * @return the reduced Trie
     */
    @Override
    public Trie optimize(Trie orig) {
        List<CharSequence> cmds = orig.cmds;
        List<Row> rows = new ArrayList<>();
        List<Row> orows = orig.rows;
        int remap[] = new int[orows.size()];

        for (int j = orows.size() - 1; j >= 0; j--) {
            liftUp(orows.get(j), orows);
        }

        Arrays.fill(remap, -1);
        rows = removeGaps(orig.root, orows, new ArrayList<Row>(), remap);

        return new Trie(orig.forward, remap[orig.root], cmds, rows);
    }

    /**
     * Reduce the trie using Lift-Up reduction.
     * <p>
     * The Lift-Up reduction propagates all leaf-values (patch commands), where
     * possible, to higher levels which are closer to the root of the trie.
     *
     * @param in the Row to consider when optimizing
     * @param nodes contains the patch commands
     */
    public void liftUp(Row in, List<Row> nodes) {
        Iterator<Cell> i = in.cells.values().iterator();
        for (; i.hasNext();) {
            Cell c = i.next();
            if (c.ref >= 0) {
                Row to = nodes.get(c.ref);
                int sum = to.uniformCmd(changeSkip);
                if (sum >= 0) {
                    if (sum == c.cmd) {
                        if (changeSkip) {
                            if (c.skip != to.uniformSkip + 1) {
                                continue;
                            }
                            c.skip = to.uniformSkip + 1;
                        } else {
                            c.skip = 0;
                        }
                        c.cnt += to.uniformCnt;
                        c.ref = -1;
                    } else if (c.cmd < 0) {
                        c.cnt = to.uniformCnt;
                        c.cmd = sum;
                        c.ref = -1;
                        if (changeSkip) {
                            c.skip = to.uniformSkip + 1;
                        } else {
                            c.skip = 0;
                        }
                    }
                }
            }
        }
    }
}
