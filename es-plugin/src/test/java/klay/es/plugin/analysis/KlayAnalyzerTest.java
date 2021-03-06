package klay.es.plugin.analysis;

import klay.core.Klay;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class KlayAnalyzerTest {

    static KlayAnalyzer klayAnalyzer;

    @Before
    public void beforeAll() throws Exception {

        Properties config = new Properties();
        config.put("dictionary.emission.path", "../data/dictionary/binary/system/emission.bin");
        config.put("dictionary.transition.path", "../data/dictionary/binary/system/transition.bin");
        config.put("dictionary.user.path", "../data/dictionary/text/user/dic.user");
        config.put("dictionary.fwd.path", "../data/dictionary/text/user/fwd.user");
        config.put("tokenization.token.length_limit", "-1");

        klayAnalyzer = new KlayAnalyzer.Builder()
                .setKlay(new Klay(config))
                .usePosFilter(true)
                .build();
    }

    @Test
    public void indexingTest() throws Exception {

        IndexWriterConfig config = new IndexWriterConfig(klayAnalyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setRAMBufferSizeMB(1024.0);

        try (Directory directory = FSDirectory.open(Paths.get("src/test/resources/index"));
             IndexWriter writer = new IndexWriter(directory, config)) {

            // 1. define file type ...
            FieldType fieldType = new FieldType();
            fieldType.setStoreTermVectors(true);
            fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
            fieldType.setTokenized(true);
            fieldType.setStored(true);
            fieldType.freeze();

            // 2. add two documents ...
            Document doc = new Document();
            doc.add(new Field("field1", "안녕하세요 심강섭 입니다.", fieldType));
            doc.add(new Field("field2", "안녕하세요 하임준 입니다.", fieldType));
            doc.add(new Field("field3", "KLAY an es-plugin을개발하고 있습니다.", fieldType));
            writer.addDocument(doc);

            doc = new Document();
            doc.add(new Field("field1", "안녕하세요 Dwayne 입니다.", fieldType));
            doc.add(new Field("field2", "안녕하세요 Andrew 입니다.", fieldType));
            doc.add(new Field("field3", "형태소 분석기로 기여하고 싶습니다.", fieldType));
            writer.addDocument(doc);

            writer.flush();
        }
    }

    @Test
    public void readingIndexFileTest() throws Exception {
        Directory directory = FSDirectory.open(Paths.get("src/test/resources/index"));
        IndexReader reader = DirectoryReader.open(directory);

        int nDoc = reader.maxDoc();
        for(int i=0; i<nDoc; i++) {
            Terms terms = reader.getTermVector(i, "field3");
            if(terms == null) continue;

            TermsEnum iter = terms.iterator();
            BytesRef term = null;
            while((term = iter.next()) != null) {
                String termText = term.utf8ToString();
                long termFreq = iter.totalTermFreq();
                System.out.println(termText + " : " + termFreq);
            }
            System.out.println();
        }
    }
}
