package da.klay.common.dictionary.structure;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class TrieLoadSaveHelper {

    public static Trie load(Path filePath) throws Exception {

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(filePath)))) {

            return new Trie(dis);
        }
    }

    public static void store(Trie t,
                             Path filePath) throws Exception {

        t.reduce(new Lift(true));

        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(filePath)))) {

            t.store(dos);
        }
    }
}
