import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                BinaryStdOut.write(i);
                stringBuilder.append(s.charAt(s.length() - 1));
            }
            else stringBuilder.append(s.charAt(circularSuffixArray.index(i) - 1));
        }
        BinaryStdOut.write(stringBuilder.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int EXTENDED_ASCII = 256;
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int len = t.length();

        char[] lastColumn = t.toCharArray();

        // get the first column of the sorted suffix array list from the last column, by sorting it.
        char[] firstColumn = new char[len];
        int[] radixCount = new int[EXTENDED_ASCII + 1];
        int[] next = new int[len];
        for (int i = 0; i < len; i++) radixCount[lastColumn[i] + 1]++;
        for (int r = 0; r < EXTENDED_ASCII; r++) radixCount[r + 1] += radixCount[r];
        for (int i = 0; i < len; i++) {
            firstColumn[radixCount[lastColumn[i]]] = lastColumn[i];
            // for each original suffix row starting with c,
            // its next row will end with c.
            // As the suffix array is sorted for the first and last columns here,
            // the row A that starts with c and the row B that ends with c have the same order,
            // as the rest of the characters in A and B should be the same.
            next[radixCount[lastColumn[i]]++] = i;
        }

        int curr = first;
        for (int i = 0; i < len; i++) {
            BinaryStdOut.write(firstColumn[curr]);
            curr = next[curr];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].charAt(0) == '-') transform();
        else if (args[0].charAt(0) == '+') inverseTransform();
        else throw new IllegalArgumentException("Illegal argument for BurrowsWheeler!");
    }

}
