/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class WordNet {

    // HashMap relating nouns with their related synset ids;
    private final HashMap<String, Bag<Integer>> synMap;
    // HashMap relating a synset to its id
    private final HashMap<Integer, String> synSets;
    // digraph that shows relationship between synsets and their hypernyms
    private final Digraph wordNet;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        synSets = new HashMap<>();
        synMap = new HashMap<>();
        int count = readSynsets(synsets);
        wordNet = new Digraph(count);
        readHypernyms(hypernyms);

        // check that word net is acyclic
        DirectedCycle dc = new DirectedCycle(wordNet);
        if (dc.hasCycle()) throw new IllegalArgumentException("Word net has cycle");

        // check that only one vertex has 0 out degrees
        int rootCount = 0;
        for (int v = 0; v < count; v++) {
            if (wordNet.outdegree(v) == 0) rootCount++;
        }
        if (rootCount != 1) throw new IllegalArgumentException("DAG root count is wrong");

        sap = new SAP(wordNet);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Noun is null");
        return synMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not a wordnet noun");

        Bag<Integer> idsa = synMap.get(nounA);
        Bag<Integer> idsb = synMap.get(nounB);

        return sap.length(idsa, idsb);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not a wordnet noun");

        Bag<Integer> idsa = synMap.get(nounA);
        Bag<Integer> idsb = synMap.get(nounB);

        int id = sap.ancestor(idsa, idsb);
        return synSets.get(id);
    }

    // helper methods
    private int readSynsets(String synset) {
        In in = new In(synset);
        int count = 0;
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String syn = parts[1];
            synSets.put(id, syn);
            count++;
            String[] nouns = parts[1].split(" ");
            for (String noun : nouns) {
                if (synMap.get(noun) != null) {
                    Bag<Integer> bag = synMap.get(noun);
                    bag.add(id);
                }
                else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(id);
                    synMap.put(noun, bag);
                }
            }
        }
        return count;
    }

    private void readHypernyms(String hypernym) {
        In in = new In(hypernym);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int v = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int w = Integer.parseInt(parts[i]);
                wordNet.addEdge(v, w);
            }
        }
    }

}
