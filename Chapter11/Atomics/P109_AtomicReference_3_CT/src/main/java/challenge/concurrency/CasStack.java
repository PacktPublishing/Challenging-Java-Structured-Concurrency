package challenge.concurrency;

import java.util.concurrent.atomic.AtomicReference;

public class CasStack<S> {

    static class Node<S> {

        final S element;
        Node<S> next;

        public Node(S element) {
            this.element = element;
        }
    }

    AtomicReference<Node<S>> headRef = new AtomicReference<>();

    public void push(S element) {
        
        Node<S> prevHead;
        Node<S> newHead = new Node<>(element);        
        
        do {
            prevHead = headRef.get();
            newHead.next = prevHead;
        } while (!headRef.compareAndSet(prevHead, newHead));
    }

    public S pop() {
        
        Node<S> prevHead;
        Node<S> newHead;
        
        do {
            prevHead = headRef.get();
            
            if (prevHead == null) {
                return null;
            }
            
            newHead = prevHead.next;
        } while (!headRef.compareAndSet(prevHead, newHead));
        
        return prevHead.element;
    }
}