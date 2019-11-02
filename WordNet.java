import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private final ST<String, Bag<Integer>> wordST;
    private final SAP wordSAP;
    private final String[] synsetDictionary;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {


        // construct the symbol table that connects all the words with their id.
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Illegal argument: null input!");

        String[] currTokens;
        String[] currWords;
        wordST = new ST<String, Bag<Integer>>();
        In synsetsIn = new In(synsets);
        String[] lines = synsetsIn.readAllLines();
        int vNum = lines.length;
        synsetDictionary = new String[vNum];

        for (int i = 0; i < vNum; i++) {
            currTokens = lines[i].split(",");
            int synsetNum = Integer.parseInt(currTokens[0]);
            synsetDictionary[i] = currTokens[1];
            currWords = currTokens[1].split(" ");
            for (String word : currWords) {
                if (wordST.contains(word)) {
                    wordST.get(word).add(synsetNum);
                }
                else {
                    wordST.put(word, new Bag<Integer>());
                    wordST.get(word).add(synsetNum);
                }
            }
        }
        synsetsIn.close();

        // construct the digraph.
        int root = -1;
        Digraph wordDigraph = new Digraph(vNum);
        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            currTokens = hypernymsIn.readLine().split(",");
            if (currTokens.length == 1) {
                if (root != -1) throw new IllegalArgumentException("Disconnected vertex!");
                root = Integer.parseInt(currTokens[0]);
            }
            else if (currTokens.length > 1) {
                int v = Integer.parseInt(currTokens[0]);
                for (int i = 1; i < currTokens.length; i++) {
                    int w = Integer.parseInt(currTokens[i]);
                    wordDigraph.addEdge(v, w);
                }
            }
        }

        // There has to be a vertex that does not have any hypernyms.
        if (root == -1) throw new IllegalArgumentException("Illegal Digraph: No root found!");
        // All the vertex has to have a path to root.

        // There has to be not circle.
        DirectedCycle cycleFinder = new DirectedCycle(wordDigraph);
        if (cycleFinder.hasCycle())
            throw new IllegalArgumentException("Illegal Digraph: contains cycle!");


        wordSAP = new SAP(wordDigraph);
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        Stack<String> nouns = new Stack<String>();
        for (String key : wordST) {
            nouns.push(key);
        }
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("null argument for isNoun");
        if (wordST.contains(word)) return true;
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Illegal word for distance calculation");
        Iterable<Integer> v = wordST.get(nounA);
        Iterable<Integer> w = wordST.get(nounB);
        return wordSAP.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Illegal word for sap calculation");
        Iterable<Integer> v = wordST.get(nounA);
        Iterable<Integer> w = wordST.get(nounB);
        int ancestor = wordSAP.ancestor(v, w);
        return synsetDictionary[ancestor];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        Iterable<String> nouns = wordNet.nouns();
        for (String noun : nouns) {
            StdOut.print(noun);
            StdOut.print(' ');
        }
        StdOut.println();
        while (!StdIn.isEmpty()) {
            String a = StdIn.readString();
            String b = StdIn.readString();
            StdOut.printf("a=%s\n", a);
            StdOut.printf("b=%s\n", b);

            int length = wordNet.distance(a, b);
            String ancestor = wordNet.sap(a, b);
            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }
    }
}
