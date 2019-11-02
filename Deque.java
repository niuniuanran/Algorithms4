
/******************************************************************************
 *  Dependencies: StdIn.java StdOut.java
 *  A generic double-ended queue, implemented using a linked list. Each queue
 *  element is of type Item.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;

    private class Node {
        private Item item;
        private Node after;
        private Node before;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            first = new Node();
            first.item = item;
            first.after = null;
            first.before = null;
            last = first;
            size++;
            return;
        }

        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.before = null;
        newFirst.after = first;
        first.before = newFirst;

        first = newFirst;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            first = new Node();
            first.item = item;
            first.after = null;
            first.before = null;
            last = first;
            size++;
            return;
        }
        Node newLast = new Node();
        newLast.item = item;
        newLast.after = null;
        newLast.before = last;
        last.after = newLast;
        last = newLast;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.after;
        if (isEmpty()) {
            size--;
            last = null;
            return item;
        }
        first.before = null;
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = last.item;
        last = last.before;
        if (last == null) {
            first = null;
            size--;
            return item;
        }
        last.after = null;
        size--;
        return item;

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.after;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    // unit testing (required)
    public static void main(String[] args) {
        StdOut.println("constructing a new dequeue...");
        Deque<String> strDeque = new Deque<String>();
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println();

        StdOut.println("Adding a new string, 'first', to the start of the queue");
        strDeque.addFirst("first");
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();


        StdOut.println("Adding a new string, 'apple', to the start of the queue");
        strDeque.addFirst("apple");
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();

        StdOut.println("Now popping a string from the end");
        String lastS = strDeque.removeLast();
        StdOut.printf("The string popped is '%s'\n", lastS);
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();

        StdOut.println("Adding a new string, 'pen', to the end of the queue");
        strDeque.addLast("pen");
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();

        StdOut.println("Adding a new string, 'pineapple', to the end of the queue");
        strDeque.addLast("pineapple");
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();

        StdOut.println("Now popping a string from the start");
        String firstS = strDeque.removeFirst();
        StdOut.printf("The string popped is '%s'\n", firstS);
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();

        StdOut.println("Now popping a string from the start");
        firstS = strDeque.removeFirst();
        StdOut.printf("The string popped is '%s'\n", firstS);
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();

        StdOut.println("Now popping a string from the start");
        firstS = strDeque.removeFirst();
        StdOut.printf("The string popped is '%s'\n", firstS);
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();

        StdOut.println("Adding a new string, 'pineapple', to the end of the queue");
        strDeque.addLast("pineapple");
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();

        StdOut.println("Now popping a string from the start");
        firstS = strDeque.removeFirst();
        StdOut.printf("The string popped is '%s'\n", firstS);
        StdOut.printf("The current size is %d\n", strDeque.size());
        StdOut.println("Now the deque is:");
        for (String s : strDeque) {
            StdOut.println(s);
        }
        StdOut.println();


    }

}
