package da.klay.common.dictionary;

import org.junit.jupiter.api.Test;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    @Test
    public void testTrie() {
        Trie t = new Trie(true);

        String[] keys = {"티라노사우르스", "스테고사우르스", "트리케라톱스"};
        String[] vals = {"육식공룡", "초식공룡", "초식공룡"};

        for(int i=0; i<keys.length; i++)
            t.add(keys[i], vals[i]);

        assertEquals(0, t.root);
        assertEquals(18, t.rows.size());
        assertEquals(2, t.cmds.size());
        //assertTrieContents(t, keys, vals);
    }

    @Test
    public void testReduce() throws Exception {
        Trie t = new Trie(true);

        String[] keys = {"티라노사우르스", "스테고사우르스", "트리케라톱스"};
        String[] vals = {"육식공룡", "초식공룡", "초식공룡"};

        for(int i=0; i<keys.length; i++)
            t.add(keys[i], vals[i]);


        TrieLoadSaveHelper.store(t, Paths.get("src/test/resources/defaultTrie.dic"));

        t = t.reduce(new Optimizer());
        TrieLoadSaveHelper.store(t, Paths.get("src/test/resources/reducedTrie.dic"));
    }
}