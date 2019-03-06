package klay.common.dictionary.structure;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(filePath)))) {

            t.store(dos);
        }
    }
}
