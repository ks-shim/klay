package da.klay.core.morphology.analysis.rule;

import da.klay.common.dictionary.structure.TrieResult;
import da.klay.common.parser.JasoParser;
import da.klay.core.morphology.analysis.Morph;
import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.triebase.system.EmissionTrieBaseDictionary;
import lombok.Data;
import lombok.ToString;

import java.nio.file.Paths;
import java.util.*;

public class AllPossibleCandidatesRule {

    private final EmissionTrieBaseDictionary emissionDictionary;
    private final TransitionMapBaseDictionary transitionDictionary;
    public AllPossibleCandidatesRule(EmissionTrieBaseDictionary emissionDictionary,
                                     TransitionMapBaseDictionary transitionDictionary) {
        this.emissionDictionary = emissionDictionary;
        this.transitionDictionary = transitionDictionary;
    }

    public void apply(CharSequence jaso, Map<Integer, List<CandidateNode>> candidateNodesSlot) {

        // 1. initialize
        int jasoLength = jaso.length();
        candidateNodesSlot.clear();

        for(int i=0; i<jasoLength; i++) {

            if(i > 0 && candidateNodesSlot.get(i) == null) continue;

            TrieResult[] results = emissionDictionary.getAll(jaso, i);
            if(results == null && i == 0) break;
            else if(results == null) continue;

            assignSlot(i, results, candidateNodesSlot);
        }
    }

    private void assignSlot(int curJasoPosition,
                            TrieResult[] results,
                            Map<Integer, List<CandidateNode>> candidateNodesSlot) {

        for(TrieResult result : results) {
            if(!result.hasResult()) continue;

            int insertIndex = curJasoPosition + result.length();
            List<CandidateNode> candidateNodeList = candidateNodesSlot.get(insertIndex);
            if(candidateNodeList == null) {
                candidateNodeList = new LinkedList<>();
                candidateNodesSlot.put(insertIndex, candidateNodeList);
            }

            parseTrieResultAsSave(result.getData(), candidateNodeList, result.length());
        }
    }

    private void parseTrieResultAsSave(CharSequence res,
                                      List<CandidateNode> nodeList,
                                      int jasoLength) {

        CandidateNode newNode = new CandidateNode(jasoLength);
        nodeList.add(newNode);

        // ex) 달/VV ㄴ/ETM:18	달/VA ㄴ/ETM:4
        int textStartIndex = 0;
        int slashIndex = 0;
        int colonIndex = 0;
        int resLength = res.length();
        for(int i=0; i<resLength; i++) {

            char ch = res.charAt(i);
            if(ch == '/') {
                slashIndex = i;
            } else if(ch == ' ') {
                CharSequence text = res.subSequence(textStartIndex, slashIndex);
                CharSequence pos = res.subSequence(slashIndex+1, i);
                textStartIndex = i+1;

                Morph morph = new Morph(text, pos);
                newNode.addMorph(morph);
            } else if (ch == ':') {
                CharSequence text = res.subSequence(textStartIndex, slashIndex);
                CharSequence pos = res.subSequence(slashIndex+1, i);
                colonIndex = i;

                Morph morph = new Morph(text, pos);
                newNode.addMorph(morph);
            } else if(ch == '\t' || i == resLength-1) {
                textStartIndex = i+1;
                int emissionScore = Integer.parseInt((String)res.subSequence(colonIndex+1, (i == resLength-1) ? i+1 : i));
                newNode.setEmissionScore(emissionScore);
                System.out.println(newNode);

                newNode = new CandidateNode(jasoLength);
                nodeList.add(newNode);
            }
        }
    }

    @Data
    @ToString
    private class CandidateNode {
        LinkedList<Morph> morphList;
        int emissionScore = 0;
        int totalScore = 0;
        int jasoLength;

        CandidateNode(int jasoLength) {
            this.jasoLength = jasoLength;
            this.morphList = new LinkedList<>();
        }

        void addMorph(Morph morph) {
            morphList.add(morph);
        }

        Morph firstMorph() {
            return morphList.getFirst();
        }

        Morph lastMorph() {
            return morphList.getLast();
        }
    }

    public static void main(String[] args) throws Exception {
        EmissionTrieBaseDictionary emissionDictionary =
                new EmissionTrieBaseDictionary(
                        new DictionaryBinarySource(Paths.get("data/dictionary/binary/system/emission.bin")));

        TransitionMapBaseDictionary transitionDictionary =
                new TransitionMapBaseDictionary(
                        new DictionaryBinarySource(Paths.get("data/dictionary/binary/System/transition.bin")));

        AllPossibleCandidatesRule rule = new AllPossibleCandidatesRule(emissionDictionary, transitionDictionary);

        CharSequence jaso = JasoParser.parseAsString( "나쁜사람입니다");
        rule.apply(jaso, new HashMap<>());

        int end = jaso.length();
        //rule.print(candidates, end);
        /*for(int i=end; i>=0; i--) {
            CandidateNode candidate = candidates[i];
            if(candidate == null && i == end-1) break;
            else if(candidate == null) continue;

            while(candidate != null) {
                System.out.println(candidate.data);
                candidate = candidate.verticalPreNode;
            }
            System.out.println("-----------");
        }*/
        //System.out.println(Arrays.toString(candidates));
    }

}
