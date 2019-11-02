import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queueArray;
    private int queueTop;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queueArray = (Item[]) new Object[2];
        queueTop = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return queueTop == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return queueTop;
    }

    private void resize(int newSize) {
        assert newSize >= queueTop;
        Item[] newArray = (Item[]) new Object[newSize];
        for (int i = 0; i < queueTop; i++) {
            newArray[i] = queueArray[i];
        }
        queueArray = newArray;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (queueTop == 0) resize(2);
        if (queueTop == queueArray.length) resize(2 * queueTop);
        queueArray[queueTop++] = item;
    }

    private void swap(int a, int b) {
        Item temp = queueArray[a];
        queueArray[a] = queueArray[b];
        queueArray[b] = temp;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int targetIndex = StdRandom.uniform(queueTop);
        swap(targetIndex, queueTop - 1);
        Item poppedItem = queueArray[queueTop - 1];
        queueArray[queueTop - 1] = null;
        queueTop--;

        if (queueTop <= queueArray.length / 4) resize(queueArray.length / 2);

        return poppedItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return queueArray[StdRandom.uniform(queueTop)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int i;
        private int[] shuffledIndex;

        public ArrayIterator() {
            shuffledIndex = StdRandom.permutation(queueTop);
            i = 0;
        }

        public boolean hasNext() {
            return i < queueTop;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = queueArray[shuffledIndex[i]];
            i++;
            return item;
        }
    }

    private void printCheck() {
        StdOut.printf("Now the size of the Queue is %d\n", size());
        StdOut.println("Now the queue is");
        for (Item i : this) {
            StdOut.println(i);
        }
        StdOut.println();
    }

    // unit testing (required)
    public static void main(String[] args) {
        StdOut.println("Constructing a new Randomized integer Queue");
        RandomizedQueue<Integer> myQueue = new RandomizedQueue<Integer>();
        myQueue.printCheck();

        StdOut.println("Adding a new number, 1");
        myQueue.enqueue(1);
        myQueue.printCheck();


        StdOut.println("Adding a new number, 2");
        myQueue.enqueue(2);
        myQueue.printCheck();

        StdOut.println("Adding a new number, 3");
        myQueue.enqueue(3);
        myQueue.printCheck();

        StdOut.println("Adding a new number, 4");
        myQueue.enqueue(4);
        myQueue.printCheck();

        StdOut.println("Adding a new number, 5");
        myQueue.enqueue(5);
        myQueue.printCheck();

        StdOut.printf("Now peeking a number, and I see %d\n", myQueue.sample());
        myQueue.printCheck();

        StdOut.printf("Now removing a number, and I get %d\n", myQueue.dequeue());
        myQueue.printCheck();
        StdOut.println();

        StdOut.println("Constructing a new Randomized string Queue");
        RandomizedQueue<String> myStringQueue
                = new RandomizedQueue<String>();
        myStringQueue.printCheck();

        StdOut.println("Now adding a new string, 'my'");
        myStringQueue.enqueue("my");
        myStringQueue.printCheck();

        StdOut.println("Now adding a new string, 'apple'");
        myStringQueue.enqueue("apple");
        myStringQueue.printCheck();

        StdOut.println("Now adding a new string, 'pear'");
        myStringQueue.enqueue("pear");
        myStringQueue.printCheck();

        StdOut.printf("Now having a peek, and I see %s \n", myStringQueue.sample());
        myStringQueue.printCheck();

        StdOut.printf("Now popping out, and I get %s\n", myStringQueue.dequeue());
        myStringQueue.printCheck();


    }


}
