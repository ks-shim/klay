package klay.common.dictionary.structure;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;

public class TrieTest {

    @Test
    public void testTrie() {
        Trie t = new StringValueTrie(true);

        String[] keys = {"티라노사우르스", "스테고사우르스", "트리케라톱스"};
        String[] vals = {"육식공룡", "초식공룡", "초식공룡"};

        for(int i=0; i<keys.length; i++)
            t.add(keys[i], vals[i]);

        Assert.assertEquals(0, t.root);
        Assert.assertEquals(18, t.rows.size());
        Assert.assertEquals(2, t.cmds.size());
    }

    @Ignore
    @Test
    public void testReduce() throws Exception {
        Trie t = new StringValueTrie(true);

        String[] keys = {"티라노사우르스", "스테고사우르스", "트리케라톱스"};
        String[] vals = {"육식공룡", "초식공룡", "초식공룡"};

        for(int i=0; i<keys.length; i++)
            t.add(keys[i], vals[i]);


        TrieLoadSaveHelper.store(t, Paths.get("src/test/resources/defaultTrie.dic"));
        TrieResult cs = t.getLastOnPath("트리케라톱스");
        Assert.assertEquals("초식공룡", cs.getData());
    }

    @Test
    public void testLoadingDictionary() throws Exception {
        Trie t = TrieLoadSaveHelper.load(Paths.get("src/test/resources/defaultTrie.dic"), TrieDataType.STRING);
        TrieResult cs = t.getLastOnPath("트리케라톱스");
        Assert.assertEquals("초식공룡", cs.getData());

        t = TrieLoadSaveHelper.load(Paths.get("src/test/resources/defaultTrie.dic"), TrieDataType.STRING);
        cs = t.getLastOnPath("티라노사우르스");
        Assert.assertEquals("육식공룡", cs.getData());
    }
}