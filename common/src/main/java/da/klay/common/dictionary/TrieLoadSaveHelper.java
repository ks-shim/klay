package da.klay.common.dictionary;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class TrieLoadSaveHelper {

    public static Trie load(Path filePath) throws Exception {

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(filePath)))) {

            String method = dis.readUTF().toUpperCase(Locale.ROOT);
            if(method.indexOf('M') >= 0) throw new UnsupportedOperationException();

            return new Trie(dis);
        }
    }

    public static void store(Trie t,
                             Path filePath) throws Exception {

        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(filePath)))) {

            t.store(dos);
        }
    }
}
