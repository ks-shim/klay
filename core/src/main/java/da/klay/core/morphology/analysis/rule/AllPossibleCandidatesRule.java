package da.klay.core.morphology.analysis.rule;

import da.klay.common.dictionary.structure.TrieResult;
import da.klay.common.parser.JasoParser;
import da.klay.core.tokenization.TokenResult;
import da.klay.dictionary.Dictionary;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.triebase.system.EmissionTrieBaseDictionary;
import lombok.Data;
import lombok.ToString;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class AllPossibleCandidatesRule {

    private final EmissionTrieBaseDictionary dictionary;
    public AllPossibleCandidatesRule(EmissionTrieBaseDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void apply(CharSequence jaso, Vertical[] candidates) {

        // 1. initialize
        int jasoLength = jaso.length();
        Arrays.fill(candidates, 0, jasoLength, null);

        for(int i=0; i<jasoLength; i++) {

            if(i > 0 && candidates[i] == null) continue;

            TrieResult[] results = dictionary.getAll(jaso, i);
            if(results == null && i == 0) break;
            else if(results == null) continue;

            for(TrieResult result : results) {
                if(!result.hasResult()) continue;

                int insertIndex = i + result.length();
                Vertical vertical = candidates[insertIndex];
                if(vertical == null) {
                    vertical = new Vertical(result.getData());
                    candidates[insertIndex] = vertical;
                    continue;
                }
                Vertical nextOne = new Vertical(result.getData());
                vertical.addNext(nextOne);
                candidates[insertIndex] = nextOne;
            }
        }
    }

    @Data
    private class Vertical {
        Vertical previous;
        Vertical next;
        CharSequence data;

        Vertical(CharSequence data) {
            this.data = data;
        }

        void addNext(Vertical nextOne) {
            this.next = nextOne;
            nextOne.previous = this;
        }
    }

    public static void main(String[] args) throws Exception {
        EmissionTrieBaseDictionary dictionary =
                new EmissionTrieBaseDictionary(
                        new DictionaryBinarySource(Paths.get("data/dictionary/binary/system/emission.bin")));

        AllPossibleCandidatesRule rule = new AllPossibleCandidatesRule(dictionary);

        CharSequence jaso = JasoParser.parseAsString( "대구일보기자입니다");
        Vertical[] candidates = new Vertical[32 * 4];
        rule.apply(jaso, candidates);

        for(int i=0; i<jaso.length(); i++) {
            Vertical candidate = candidates[i];
            if(candidate == null) continue;

            while(candidate != null) {
                System.out.println(candidate.data);
                candidate = candidate.previous;
            }
            System.out.println("-----------");
        }
        //System.out.println(Arrays.toString(candidates));
    }
}
