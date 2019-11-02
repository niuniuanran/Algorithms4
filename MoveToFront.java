
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {


    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int EXTENDED_ASCII = 256;
        // Your task is to maintain an ordered sequence of the 256 extended ASCII characters.
        byte[] alphabet = new byte[EXTENDED_ASCII];
        // Initialize the sequence by making the ith character in the sequence equal to the ith extended ASCII character.
        for (int i = 0; i < EXTENDED_ASCII; i++) alphabet[i] = (byte) i;
        // Now, read each 8-bit character c from standard input, one at a time;
        while (!BinaryStdIn.isEmpty()) {

            byte c = BinaryStdIn.readByte();
            // find the current position of c
            int cPos = 0;
            while (cPos >= 0 && c != alphabet[cPos]) cPos++;

            // output the 8-bit index in the sequence where c appears;
            BinaryStdOut.write(cPos, 8);
            // and move c to the front.
            if (cPos != 0) {
                System.arraycopy(alphabet, 0, alphabet, 1, cPos);
                alphabet[0] = c;
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int EXTENDED_ASCII = 256;
        //  Initialize an ordered sequence of 256 characters,
        byte[] alphabet = new byte[EXTENDED_ASCII];
        //  where extended ASCII character i appears ith in the sequence.
        for (int i = 0; i < EXTENDED_ASCII; i++) alphabet[i] = (byte) i;

        // Now, read each 8-bit character i (but treat it as an integer between 0 and 255)
        // from standard input one at a time;
        while (!BinaryStdIn.isEmpty()) {
            int ind = BinaryStdIn.readByte() & 0xff;
            byte c = alphabet[ind];

            // write the ith character in the sequence;
            BinaryStdOut.write(c);

            // and move that character to the front.
            if (ind != 0) {
                System.arraycopy(alphabet, 0, alphabet, 1, ind);
                alphabet[0] = c;
            }
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].charAt(0) == '-') encode();
        else if (args[0].charAt(0) == '+') decode();
        else throw new IllegalArgumentException();
    }

}
