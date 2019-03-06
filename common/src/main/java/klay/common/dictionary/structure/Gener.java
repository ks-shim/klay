package klay.common.dictionary.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Gener extends Reduce {

    public Gener() {}

    /**
     * Return a Trie with infrequent values occurring in the given Trie removed.
     *
     * @param orig the Trie to optimize
     * @return a new optimized Trie
     */
    @Override
    public Trie optimize(Trie orig) {
        List<CharSequence> cmds = orig.cmds;
        List<Row> rows = new ArrayList<>();
        List<Row> orows = orig.rows;
        int remap[] = new int[orows.size()];

        Arrays.fill(remap, 1);
        for (int j = orows.size() - 1; j >= 0; j--) {
            if (eat(orows.get(j), remap)) {
                remap[j] = 0;
            }
        }

        Arrays.fill(remap, -1);
        rows = removeGaps(orig.root, orows, new ArrayList<Row>(), remap);

        return new Trie(orig.forward, remap[orig.root], cmds, rows);
    }

    /**
     * Test whether the given Row of Cells in a Trie should be included in an
     * optimized Trie.
     *
     * @param in the Row to test
     * @param remap Description of the Parameter
     * @return <tt>true</tt> if the Row should remain, <tt>false
     *      </tt> otherwise
     */
    public boolean eat(Row in, int remap[]) {
        int sum = 0;
        for (Iterator<Cell> i = in.cells.values().iterator(); i.hasNext();) {
            Cell c = i.next();
            sum += c.cnt;
            if (c.ref >= 0) {
                if (remap[c.ref] == 0) {
                    c.ref = -1;
                }
            }
        }
        int frame = sum / 10;
        boolean live = false;
        for (Iterator<Cell> i = in.cells.values().iterator(); i.hasNext();) {
            Cell c = i.next();
            if (c.cnt < frame && c.cmd >= 0) {
                c.cnt = 0;
                c.cmd = -1;
            }
            if (c.cmd >= 0 || c.ref >= 0) {
                live |= true;
            }
        }
        return !live;
    }
}
