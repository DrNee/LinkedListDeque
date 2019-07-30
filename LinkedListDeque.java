public class LinkedListDeque<T> {

    private int size;
    private Node sentinel;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        // circular implementation: sentinel <--> nodes <--> sentinel (loops back)
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node front = new Node(sentinel, item, sentinel.next);
        // sentinel <-- front --> old front node (have not updated sentinel and old front pointers)
        if (isEmpty()) {
            sentinel.prev = front;
            // if empty, no old front node so update sentinel prev: sentinel <-- front <--> sentinel
        } else {
            sentinel.next.prev = front;
            // update old front note prev: sentinel <-- front <--> old front node
        }
        sentinel.next = front;
        // sentinel <--> front <--> old front / sentinel <--> front <--> sentinel (empty case)
        size = size + 1;
    }

    public void addLast(T item) {
        if (isEmpty()) {
            addFirst(item); // empty so same as addFirst
        } else {
            Node back = new Node(sentinel.prev, item, sentinel); // old back <-- back --> sentinel
            sentinel.prev.next = back; // old back <--> back --> sentinel
            sentinel.prev = back; // old back <--> back <--> sentinel
            size += 1;
        }
    }

    public boolean isEmpty() {
        return sentinel.next.equals(sentinel);
        // sentinel <=> sentinel (empty bc no nodes between circular loop)
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node node = sentinel.next;
        while (node.next != sentinel) {
            // stop when we reach the back node (i.e. node <--> sentinel)
            System.out.print(node.item);
            System.out.print(' ');
            node = node.next;
        }
        System.out.print(node.item); // print remaining back node item
    }

    public T removeFirst() {
        // currently: sentinel <--> front <--> second node
        if (isEmpty()) {
            return null;
        } else {
            T item = sentinel.next.item;
            // store front node item (to return)
            sentinel.next = sentinel.next.next;
            // make sentinel next point to second node:
            // sentinel --== front ==--> second node (ignore front's refs)
            sentinel.next.prev = sentinel;
            // make second node's prev the sentinel:
            // sentinel <--== front ==--> second node (no more references to first node)
            size -= 1;
            return item;
        }
    }

    public T removeLast() {
        //currently: node <--> back node <--> sentinel
        if (isEmpty()) {
            return null;
        }
        T item = sentinel.prev.item;
        // store back node item
        sentinel.prev = sentinel.prev.prev;
        // node <== back node == sentinel
        sentinel.prev.next = sentinel;
        // node <== back node ==> sentinel (no refs to back node [garbage collector cleans up])
        size -= 1;
        return item;
    }

    public T get(int index) {
        if ((index < 0) || (index > size - 1)) {
            // no item exists for given index
            return null;
        } else {
            Node node = sentinel.next;
            while (index > 0) {
                node = node.next; // run to next node index times
                index -= 1;
            }
            // return item of node at index
            return node.item;
        }
    }

    private T getRecursive(Node node, int index) {
        if (index == 0) {
            return node.item;
        } else {
            return getRecursive(node.next, index - 1);
        }
    }

    public T getRecursive(int index) {
        if ((index < 0) || (index > size - 1)) {
            return null;
        } else {
            return getRecursive(sentinel.next, index);
        }
    }

    private class Node {
        Node prev;
        T item;
        Node next;


        Node(Node prev, T item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }
}
