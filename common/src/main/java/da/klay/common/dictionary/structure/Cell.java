package da.klay.common.dictionary.structure;

/**
 * A Cell is a portion of a trie.
 */
class Cell {
    /** next row id in this way */
    int ref = -1;
    /** command of the cell */
    int cmd = -1;
    /** how many cmd-s was in subtrie before pack() */
    int cnt = 0;
    /** how many chars would be discarded from input key in this way */
    int skip = 0;

    /** Constructor for the Cell object. */
    Cell() {}

    /**
     * Construct a Cell using the properties of the given Cell.
     *
     * @param a the Cell whose properties will be used
     */
    Cell(Cell a) {
        ref = a.ref;
        cmd = a.cmd;
        cnt = a.cnt;
        skip = a.skip;
    }

    /**
     * Return a String containing this Cell's attributes.
     *
     * @return a String representation of this Cell
     */
    @Override
    public String toString() {
        return "ref(" + ref + ")cmd(" + cmd + ")cnt(" + cnt + ")skp(" + skip + ")";
    }
}