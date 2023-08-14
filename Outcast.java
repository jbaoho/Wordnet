/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException("Wordnet is null");
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxdist = Integer.MIN_VALUE;
        String oc = null;
        for (String noun : nouns) {
            int sum = sum(noun, nouns);
            if (sum > maxdist) {
                maxdist = sum;
                oc = noun;
            }
        }
        return oc;
    }

    // helper methods
    private int sum(String noun, String[] nouns) {
        if (!wordnet.isNoun(noun)) throw new IllegalArgumentException("Not a wordnet noun");
        int sum = 0;
        for (int i = 0; i < nouns.length; i++) {
            sum += this.wordnet.distance(noun, nouns[i]);
        }
        return sum;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
