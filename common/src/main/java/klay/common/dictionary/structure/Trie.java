package klay.common.dictionary.structure;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A Trie is used to store a dictionary of words and their stems.
 * <p>
 * Actually, what is stored are words with their respective patch commands. A
 * trie can be termed forward (keys read from left to right) or backward (keys
 * read from right to left). This property will vary depending on the language
 * for which a Trie is constructed.
 */
public abstract class Trie<T> {

    protected List<Row> rows = new ArrayList<>();
    protected List<T> cmds = new ArrayList<>();
    protected int root;

    protected boolean forward = false;

    protected Trie() {}

    /**
     * Constructor for the Trie object.
     *
     * @param forward set to <tt>true</tt>
     */
    public Trie(boolean forward) {
        rows.add(new Row());
        root = 0;
        this.forward = forward;
    }

    /**
     * Gets the all attribute of the Trie object
     *
     * @param key Description of the Parameter
     * @return The all value
     */
    public TrieResult<T>[] getAll(CharSequence key, int from) {
        int[] res = new int[key.length()];
        int[] resPos = new int[key.length()];
        int resc = 0;
        Row now = getRow(root);
        int w;
        StrEnum e = new StrEnum(key, from,key.length() - from, forward);
        boolean br = false;

        int i = from;
        for (; i < key.length() - 1; i++) {
            char ch = Character.toLowerCase(e.next());
            w = now.getCmd(ch);
            if (w >= 0) {
                res[resc] = w;
                resPos[resc] = i;
                ++resc;
            }
            w = now.getRef(ch);
            if (w >= 0) {
                now = getRow(w);
            } else {
                br = true;
                break;
            }
        }
        if (br == false) {
            w = now.getCmd(Character.toLowerCase(e.next()));
            if (w >= 0) {
                res[resc] = w;
                resPos[resc] = i;
                ++resc;
            }
        }

        if (resc < 1) return null;

        TrieResult ret[] = new TrieResult[resc];
        for (int j = 0; j < resc; j++)
            ret[j] = new TrieResult(cmds.get(res[j]), from, resPos[j]+1);

        return ret;
    }

    /**
     * Return the number of cells in this Trie object.
     *
     * @return the number of cells
     */
    public int getCells() {
        int size = 0;
        for (Row row : rows)
            size += row.getCells();
        return size;
    }

    /**
     * Gets the cellsPnt attribute of the Trie object
     *
     * @return The cellsPnt value
     */
    public int getCellsPnt() {
        int size = 0;
        for (Row row : rows)
            size += row.getCellsPnt();
        return size;
    }

    /**
     * Gets the cellsVal attribute of the Trie object
     *
     * @return The cellsVal value
     */
    public int getCellsVal() {
        int size = 0;
        for (Row row : rows)
            size += row.getCellsVal();
        return size;
    }

    public T getFully(CharSequence key, int from, int keyLength) {
        Row now = getRow(root);
        int w;
        Cell c;
        int cmd = -1;
        StrEnum e = new StrEnum(key, from, keyLength, forward);
        Character ch = null;
        Character aux = null;

        for (int i = from; i < from + keyLength;) {
            ch = e.next();
            i++;

            c = now.at(ch);
            if (c == null) {
                return null;
            }

            cmd = c.cmd;

            for (int skip = c.skip; skip > 0; skip--) {
                if (i < key.length()) {
                    aux = e.next();
                } else {
                    return null;
                }
                i++;
            }

            w = now.getRef(ch);
            if (w >= 0) {
                now = getRow(w);
            } else if (i < e.length()) {
                return null;
            }
        }
        return (cmd == -1) ? null : cmds.get(cmd);
    }

    /**
     * Return the element that is stored in a cell associated with the given key.
     *
     * @param key the key
     * @return the associated element
     */
    public T getFully(CharSequence key) {
        return getFully(key, 0, key.length());
    }

    public TrieResult<T> getLastOnPath(CharSequence key) {
        return getLastOnPath(key, 0);
    }

    /**
     * Return the element that is stored as last on a path associated with the
     * given key.
     *
     * @param key the key associated with the desired element
     * @return the last on path element
     */
    public TrieResult<T> getLastOnPath(CharSequence key,
                                       int from) {
        Row now = getRow(root);
        int w;
        T last = null;
        StrEnum e = new StrEnum(key, from, key.length() - from, forward);

        int i = from;
        int lastIndex = 0;
        for (; i < key.length() - 1; i++) {
            Character ch = Character.toLowerCase(e.next());
            w = now.getCmd(ch);
            if (w >= 0) {
                last = cmds.get(w);
                lastIndex = i;
            }
            w = now.getRef(ch);
            if (w >= 0) {
                now = getRow(w);
            } else {
                return new TrieResult(last, from, lastIndex+1);
            }
        }
        w = now.getCmd(Character.toLowerCase(e.next()));
        if(w >= 0) lastIndex = i;
        return new TrieResult((w >= 0) ? cmds.get(w) : last, from, lastIndex+1);
    }

    /**
     * Return the Row at the given index.
     *
     * @param index the index containing the desired Row
     * @return the Row
     */
    private Row getRow(int index) {
        if (index < 0 || index >= rows.size()) {
            return null;
        }
        return rows.get(index);
    }

    public abstract void store(DataOutput os) throws IOException;

    /**
     * Add the given key associated with the given patch command. If either
     * parameter is null this method will return without executing.
     *
     * @param key the key
     * @param cmd the patch command
     */
    protected void add(CharSequence key, T cmd, boolean addIfNotExist) {

        int id_cmd = cmds.indexOf(cmd);
        if (id_cmd == -1) {
            id_cmd = cmds.size();
            cmds.add(cmd);
        }

        int node = root;
        Row r = getRow(node);

        StrEnum e = new StrEnum(key, forward);

        for (int i = 0; i < e.length() - 1; i++) {
            Character ch = e.next();
            node = r.getRef(ch);
            if (node >= 0) {
                r = getRow(node);
            } else {
                node = rows.size();
                Row n;
                rows.add(n = new Row());
                r.setRef(ch, node);
                r = n;
            }
        }

        if(addIfNotExist) r.setCmdIfNotExist(e.next(), id_cmd);
        else r.setCmd(e.next(), id_cmd);
    }

    public void add(CharSequence key, T cmd) {
        add(key, cmd, false);
    }

    public void addIfNotExist(CharSequence key, T cmd) {
        add(key, cmd, true);
    }

     /** writes debugging info to the printstream */
    public void printInfo(PrintStream out, CharSequence prefix) {
        out.println(prefix + "nds " + rows.size() + " cmds " + cmds.size()
                + " cells " + getCells() + " valcells " + getCellsVal() + " pntcells "
                + getCellsPnt());
    }

    /**
     * This class is part of the Egothor Project
     */
    class StrEnum {
        CharSequence s;
        int from;
        int length;
        int by;

        StrEnum(CharSequence s,
                boolean up) {
            this(s, 0, up);
        }

        StrEnum(CharSequence s,
                int from,
                boolean up) {
            this(s, from, s.length(), up);
        }

        StrEnum(CharSequence s,
                int from,
                int length,
                boolean up) {
            this.s = s;
            this.length = length;
            if (up) {
                this.from = from;
                by = 1;
            } else {
                this.from = length - 1 - from;
                by = -1;
            }
        }

        int length() {
            return length;
        }

        char next() {
            char ch = s.charAt(from);
            from += by;
            return ch;
        }
    }
}