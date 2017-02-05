import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int itemNum;

    public RandomizedQueue() {
        items = (Item[]) new Object[0];
        itemNum = 0;
    }

    public boolean isEmpty() {
        return itemNum == 0;
    }

    public int size() {
        return itemNum;
    }

    private void resize(int size) {
        Item[] newQueue = (Item[]) new Object[size];
        for (int i = 0; i < itemNum; i++) {
            newQueue[i] = items[i];
        }

        items = newQueue;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException("Item is null");
        }

        final int length = items.length;
        if (length == 0) {
            resize(1);
        } else if (itemNum == length) {
            resize(length * 2);
        }

        items[itemNum++] = item;
    }

    public Item dequeue() {
        checkEmptyQueue();

        int randomNum = StdRandom.uniform(itemNum);
        Item item = items[randomNum];
        items[randomNum] = items[--itemNum];
        items[itemNum] = null;
        if (itemNum > 0 && itemNum < items.length / 4) {
            resize(items.length / 2);
        }

        return item;
    }

    public Item sample() {
        checkEmptyQueue();
        int randomNum = StdRandom.uniform(itemNum);
        return items[randomNum];
    }

    private void checkEmptyQueue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Queue is empty");
        }
    }
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] queue = (Item[]) new Object[items.length];
        private int quant = itemNum;

        public RandomizedQueueIterator() {
            for (int i = 0; i < itemNum; i++) {
                queue[i] = items[i];
            }
        }

        public boolean hasNext() {
            return quant > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator is empty");
            }

            int rand = StdRandom.uniform(quant);
            Item obj = queue[rand];
            if (rand != quant - 1) {
                queue[rand] = queue[quant - 1];
            }

            queue[quant - 1] = null;
            quant--;
            return obj;
        }
    }

    public static void main(String[] args) {
        System.out.println("RandomizedQueue main");
    }
}
