import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {
    private final Trie dictTrie;
    private final int[] stepX = { -1, 0, 1, 0, 1, -1, 1, -1 };
    private final int[] stepY = { 0, 1, 0, -1, 1, -1, -1, 1 };
    private int maxSize = 10;
    private boolean[][] visited = new boolean[maxSize][maxSize];
    private int currSizeRow, currSizeCol;

    private void checkBigBoard() {
        if (currSizeRow <= maxSize && currSizeCol <= maxSize) return;
        maxSize = Math.max(currSizeRow, currSizeCol);
        visited = new boolean[maxSize][maxSize];
    }

    private class Trie {
        private static final int R = 26;
        private Node root;

        private class Node {
            private String word;
            private Node[] next = new Node[R];
        }

        public Node root() {
            return root;
        }

        public void put(String word) {
            root = put(root, word, 0);
        }

        private Node put(Node x, String word, int d) {
            if (x == null) x = new Node();
            if (d == word.length()) {
                x.word = word;
                return x;
            }
            char c = word.charAt(d);
            x.next[ord(c)] = put(x.next[ord(c)], word, d + 1);
            return x;
        }

        public boolean contains(String word) {
            if (word == null) throw new IllegalArgumentException("null input for contains");
            if (get(root, word, 0) != null) return true;
            return false;
        }

        private Node get(Node x, String word, int d) {
            if (x == null) return null;
            if (d == word.length()) {
                if (x.word != null) return x;
                else return null;
            }
            char c = word.charAt(d);
            return get(x.next[ord(c)], word, d + 1);
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException("Null Dictionary");
        dictTrie = new Trie();
        for (String word : dictionary)
            dictTrie.put(word);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return search(board);
    }

    private Iterable<String> search(BoggleBoard board) {
        HashSet<String> ansWords = new HashSet<String>();
        currSizeCol = board.cols();
        currSizeRow = board.rows();
        checkBigBoard();

        for (int i = 0; i < currSizeRow; i++) {
            for (int j = 0; j < currSizeCol; j++) {
                visited[i][j] = true;
                char currC = board.getLetter(i, j);
                if (currC == 'Q') {
                    if (dictTrie.root.next[ord('Q')] != null)
                        search(ansWords, board, i, j,
                               dictTrie.root.next[ord('Q')].next[ord('U')],
                               2);
                }
                else search(ansWords, board, i, j, dictTrie.root.next[ord(currC)],
                            1);
                visited[i][j] = false;
            }
        }
        return ansWords;
    }

    private void search(HashSet<String> ansWords, BoggleBoard board, int i, int j, Trie.Node x,
                        int currLength) {
        int nextX, nextY;
        if (x == null) return;
        if (currLength >= 3 && x.word != null) {
            ansWords.add(x.word);
        }

        for (int d = 0; d < 8; d++) {
            nextX = i + stepX[d];
            nextY = j + stepY[d];
            if (!valid(nextX, nextY)) continue;
            char currC = board.getLetter(nextX, nextY);
            visited[nextX][nextY] = true;
            if (currC == 'Q') {
                if (x.next[ord('Q')] != null)
                    search(ansWords, board, nextX, nextY, x.next[ord('Q')].next[ord('U')],
                           currLength + 2);
            }
            else search(ansWords, board, nextX, nextY, x.next[ord(currC)], currLength + 1);
            visited[nextX][nextY] = false;
        }

    }

    private boolean valid(int x, int y) {
        if (x < 0 || y < 0) return false;
        if (x >= currSizeRow) return false;
        if (y >= currSizeCol) return false;
        if (visited[x][y]) return false;
        return true;
    }

    private int ord(char c) {
        return (int) c - 65;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word.length() < 3) return 0;
        if (!dictTrie.contains(word)) return 0;
        switch (word.length()) {
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
