package da.klay.common.dictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The Optimizer class is a Trie that will be reduced (have empty rows removed).
 * <p>
 * The reduction will be made by joining two rows where the first is a subset of
 * the second.
 */
public class Optimizer extends Reduce {
    /**
     * Constructor for the Optimizer object.
     */
    public Optimizer() {}

    /**
     * Optimize (remove empty rows) from the given Trie and return the resulting
     * Trie.
     *
     * @param orig the Trie to consolidate
     * @return the newly consolidated Trie
     */
    @Override
    public Trie optimize(Trie orig) {
        List<CharSequence> cmds = orig.cmds;
        List<Row> rows = new ArrayList<>();
        List<Row> orows = orig.rows;
        int remap[] = new int[orows.size()];

        for (int j = orows.size() - 1; j >= 0; j--) {
            Row now = new Remap(orows.get(j), remap);
            boolean merged = false;

            for (int i = 0; i < rows.size(); i++) {
                Row q = merge(now, rows.get(i));
                if (q != null) {
                    rows.set(i, q);
                    merged = true;
                    remap[j] = i;
                    break;
                }
            }

            if (merged == false) {
                remap[j] = rows.size();
                rows.add(now);
            }
        }

        int root = remap[orig.root];
        Arrays.fill(remap, -1);
        rows = removeGaps(root, rows, new ArrayList<Row>(), remap);

        return new Trie(orig.forward, remap[root], cmds, rows);
    }

    /**
     * Merge the given rows and return the resulting Row.
     *
     * @param master the master Row
     * @param existing the existing Row
     * @return the resulting Row, or <tt>null</tt> if the operation cannot be
     *         realized
     */
    public Row merge(Row master, Row existing) {
        Iterator<Character> i = master.cells.keySet().iterator();
        Row n = new Row();
        for (; i.hasNext();) {
            Character ch = i.next();
            // XXX also must handle Cnt and Skip !!
            Cell a = master.cells.get(ch);
            Cell b = existing.cells.get(ch);

            Cell s = (b == null) ? new Cell(a) : merge(a, b);
            if (s == null) {
                return null;
            }
            n.cells.put(ch, s);
        }
        i = existing.cells.keySet().iterator();
        for (; i.hasNext();) {
            Character ch = i.next();
            if (master.at(ch) != null) {
                continue;
            }
            n.cells.put(ch, existing.at(ch));
        }
        return n;
    }

    /**
     * Merge the given Cells and return the resulting Cell.
     *
     * @param m the master Cell
     * @param e the existing Cell
     * @return the resulting Cell, or <tt>null</tt> if the operation cannot be
     *         realized
     */
    public Cell merge(Cell m, Cell e) {
        Cell n = new Cell();

        if (m.skip != e.skip) {
            return null;
        }

        if (m.cmd >= 0) {
            if (e.cmd >= 0) {
                if (m.cmd == e.cmd) {
                    n.cmd = m.cmd;
                } else {
                    return null;
                }
            } else {
                n.cmd = m.cmd;
            }
        } else {
            n.cmd = e.cmd;
        }
        if (m.ref >= 0) {
            if (e.ref >= 0) {
                if (m.ref == e.ref) {
                    if (m.skip == e.skip) {
                        n.ref = m.ref;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                n.ref = m.ref;
            }
        } else {
            n.ref = e.ref;
        }
        n.cnt = m.cnt + e.cnt;
        n.skip = m.skip;
        return n;
    }
}
