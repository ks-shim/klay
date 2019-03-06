package klay.common.dictionary.structure;

public class Optimizer2 extends Optimizer {

    public Optimizer2() {}

    /**
     * Merge the given Cells and return the resulting Cell.
     *
     * @param m the master Cell
     * @param e the existing Cell
     * @return the resulting Cell, or <tt>null</tt> if the operation cannot be
     *         realized
     */
    @Override
    public Cell merge(Cell m, Cell e) {
        if (m.cmd == e.cmd && m.ref == e.ref && m.skip == e.skip) {
            Cell c = new Cell(m);
            c.cnt += e.cnt;
            return c;
        } else {
            return null;
        }
    }
}
