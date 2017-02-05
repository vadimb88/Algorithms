import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int n;

    public Deque() {
        first = null;
        last = first;
        n = 0;
    }

    private class Node {
        Item item;
        Node next;
        Node previous;

        public Node(Item i, Node n, Node pr) {
            item = i;
            next = n;
            previous = pr;
        }
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        checkNullItem(item);
        Node newFirst = new Node(item, null, null);
        if (n == 0) {
            first = newFirst;
            last = newFirst;
        } else {
            Node oldFirst = first;
            first = newFirst;
            first.next = oldFirst;
            oldFirst.previous = first;
        }

        n++;
    }

    public void addLast(Item item) {
        checkNullItem(item);
        Node newLast = new Node(item, null, last);
        if (n == 0) {
            first = newLast;
            last = first;
        } else {
            last.next = newLast;
            last = last.next;
        }

        n++;
    }

    private void checkAvailableToRemove() {
        if(n == 0) {
            throw new NoSuchElementException("Can't remove from empty Deque");
        }
    }

    private void checkNullItem(Item i) {
        if (i == null) {
            throw new NullPointerException("Item is null");
        }
    }

    public Item removeFirst() {
        checkAvailableToRemove();
        Item item = first.item;
        if (n > 1) {
            first.next.previous = null;
            first = first.next;
        } else {
            first = null;
            last = first;
        }

        n--;
        return item;
    }

    public Item removeLast() {
        checkAvailableToRemove();
        Item item = last.item;
        if (n > 1) {
            last = last.previous;
            last.next = null;
        } else {
            first = null;
            last = first;
        }

        n--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator is empty");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        System.out.println("Deq main");
    }
}
